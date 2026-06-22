## MODIFIED Requirements

### Requirement: Settings route presents app preferences
The Settings route SHALL present a functional settings experience with grouped controls and app context instead of a placeholder.

#### Scenario: User opens Settings
- **WHEN** the user navigates to the Settings tab
- **THEN** the screen displays settings sections for routine reminders, appearance, app information, and account actions
- **AND** the screen uses the app's refreshed visual style

## REMOVED Requirements

### Requirement: Local data can be reset deliberately
**Reason**: Routine and task data are backend-backed at runtime, and the Room-backed local routine/task store is being removed.
**Migration**: Users continue managing routine and task data through backend-backed routine/task flows; Settings no longer exposes a local routine/task reset action.
