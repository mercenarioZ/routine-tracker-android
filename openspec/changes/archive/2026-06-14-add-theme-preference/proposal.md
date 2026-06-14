## Why

The app already defines light and dark color schemes, but users cannot choose an appearance mode from Settings. Adding an explicit theme preference makes the Settings experience more complete and lets users control the app's visual comfort independent of device state.

## What Changes

- Add a persisted appearance preference with System, Light, and Dark options.
- Show the current appearance mode in Settings and let users change it from the Settings route.
- Apply the selected appearance mode at the root app theme so the whole Compose app updates consistently.
- Preserve the current system-following behavior as the default.

## Capabilities

### New Capabilities

### Modified Capabilities
- `settings-experience`: Adds an appearance preference requirement for selecting and preserving the app theme mode.

## Impact

- Affected domain/data areas: settings models, `SettingsRepository`, and DataStore-backed settings persistence.
- Affected UI areas: `MainActivity`, Settings ViewModel/state/route/screen, shared theme usage, and strings.
- No backend API, Room schema, or navigation graph changes are expected.
