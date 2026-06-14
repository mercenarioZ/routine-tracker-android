## Context

The app already uses `RoutineTrackerTheme` with Material 3 light and dark color schemes, defaulting to `isSystemInDarkTheme()`. Settings preferences are stored locally through `SettingsRepository` and `DataStoreSettingsRepository`, then exposed to the Settings UI through `SettingsViewModel`.

## Goals / Non-Goals

**Goals:**
- Add a persisted app appearance preference with System, Light, and Dark modes.
- Apply the selected mode at the Compose root so all routes update consistently.
- Surface the mode in Settings using the same grouped preference pattern as reminders and local data.
- Preserve current behavior by making System the default for new and existing installs.
- Add focused persistence coverage for the new preference.

**Non-Goals:**
- No dynamic color, custom palette editor, or per-screen theme overrides.
- No backend synchronization for appearance settings.
- No migration of Room data or navigation structure.

## Decisions

- Store appearance mode in the existing settings DataStore.
  - Rationale: Theme mode is a local user preference and belongs with reminder settings.
  - Alternative considered: separate DataStore. Rejected because it adds setup and injection surface without isolation benefits.
- Represent the preference as a domain enum with stable persisted keys.
  - Rationale: The UI and root app can consume a typed value while DataStore stores a compact string.
  - Alternative considered: store a Boolean `darkTheme`. Rejected because it cannot represent "follow system" without a second field.
- Collect settings in `MainActivity` through the existing repository.
  - Rationale: The root theme is owned at activity content setup, and repository collection keeps theme application reactive.
  - Alternative considered: pass theme state through navigation routes. Rejected because appearance is global and not route-specific.
- Use a dialog with radio options for Settings selection.
  - Rationale: It matches the existing reminder time picker pattern and keeps the Settings list compact.
  - Alternative considered: inline segmented controls. Rejected because Material 3 segmented controls are not currently used in this app and would add more UI surface than needed.
- Tune the app theme around the existing Planning routine yellow.
  - Rationale: The product already has a warm yellow visual language in the Planning routine, and using it as the main accent makes the light/dark theme feel consistent with existing routine cards.
  - Alternative considered: keep the green primary accent. Rejected because the requested direction is explicitly closer to the Planning routine yellow.

## Risks / Trade-offs

- [Risk] Injecting `SettingsRepository` into `MainActivity` could expose a loading-frame default before DataStore emits. -> Mitigation: use System as the initial mode, matching current behavior.
- [Risk] Persisted string values can become stale if enum names change. -> Mitigation: use explicit stable keys and fall back to System for unknown values.
- [Risk] Settings preview state needs new callbacks and state fields. -> Mitigation: update previews with defaults so component previews remain simple.
- [Risk] Lighter text can reduce readability. -> Mitigation: reduce font weight and soften light-mode text colors while keeping Material color roles contrast-oriented.
