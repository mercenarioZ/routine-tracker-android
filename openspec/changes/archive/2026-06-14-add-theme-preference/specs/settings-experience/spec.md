## ADDED Requirements

### Requirement: Appearance preference is locally editable
The Settings route SHALL let users choose whether the app follows the system theme or uses an explicit light or dark theme.

#### Scenario: User opens appearance setting
- **WHEN** the user opens the appearance control from Settings
- **THEN** the app presents System, Light, and Dark theme mode choices
- **AND** the currently selected mode is indicated

#### Scenario: User selects a theme mode
- **WHEN** the user selects System, Light, or Dark from the appearance choices
- **THEN** the selected mode is reflected in the Settings UI
- **AND** the selected mode is preserved locally for future app launches

#### Scenario: Selected theme mode is applied globally
- **WHEN** a saved appearance mode exists
- **THEN** the app applies that mode to the root app theme across routes
- **AND** System mode follows the device dark theme setting
