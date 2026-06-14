## 1. Settings Model and Persistence

- [x] 1.1 Add a typed theme mode setting to the domain settings model and repository contract.
- [x] 1.2 Persist and read the theme mode from the existing DataStore settings repository with System as the fallback.

## 2. Theme Application

- [x] 2.1 Collect settings in the app root and pass the selected mode into `RoutineTrackerTheme`.
- [x] 2.2 Resolve System, Light, and Dark modes consistently inside the theme layer.

## 3. Settings UI

- [x] 3.1 Add theme mode to Settings UI state and ViewModel update handling.
- [x] 3.2 Add a Settings appearance section with a selectable System, Light, and Dark mode dialog.

## 4. Verification

- [x] 4.1 Add focused tests for theme mode persistence.
- [x] 4.2 Run OpenSpec and Android verification checks.

## 5. Visual Tuning

- [x] 5.1 Tune the app theme toward the Planning routine yellow accent.
- [x] 5.2 Lighten text weight and soften text colors while preserving contrast.
