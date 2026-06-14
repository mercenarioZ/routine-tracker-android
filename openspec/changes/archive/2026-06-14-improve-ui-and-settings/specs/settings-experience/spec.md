## ADDED Requirements

### Requirement: Settings route presents app preferences
The Settings route SHALL present a functional settings experience with grouped controls and app context instead of a placeholder.

#### Scenario: User opens Settings
- **WHEN** the user navigates to the Settings tab
- **THEN** the screen displays settings sections for routine reminders, local data, app information, and account actions
- **AND** the screen uses the app's refreshed visual style

### Requirement: Reminder preferences are locally editable
The Settings route SHALL let users edit local reminder preferences without requiring backend services.

#### Scenario: User toggles reminders
- **WHEN** the user changes the reminder enabled control
- **THEN** the new reminder preference is reflected in the Settings UI
- **AND** the preference is preserved locally for future app launches

#### Scenario: User updates reminder time
- **WHEN** the user changes the reminder time setting
- **THEN** the selected time is reflected in the Settings UI
- **AND** the selected time is preserved locally for future app launches

### Requirement: Local data can be reset deliberately
The Settings route SHALL provide a local data reset action that requires confirmation before clearing stored routine and task data.

#### Scenario: User confirms local data reset
- **WHEN** the user confirms the local data reset action
- **THEN** the app clears locally persisted routine and task data
- **AND** the local sample data is available again after reset

#### Scenario: User cancels local data reset
- **WHEN** the user opens the reset confirmation and cancels it
- **THEN** locally persisted routine and task data remains unchanged

### Requirement: Settings can sign out
The Settings route SHALL expose a sign-out action that uses the app's existing auth/session flow.

#### Scenario: User signs out from Settings
- **WHEN** the user activates sign out from Settings
- **THEN** the current session is cleared
- **AND** the app navigates to the login route using the same post-logout behavior as other sign-out entry points
