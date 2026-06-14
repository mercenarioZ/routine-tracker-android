## ADDED Requirements

### Requirement: Create routine captures recurrence
The Create routine route SHALL allow users to choose daily, weekly, or custom recurrence before saving.

#### Scenario: User creates a daily routine
- **WHEN** the user saves a routine with daily recurrence selected
- **THEN** the created routine is submitted with daily recurrence

#### Scenario: User creates a weekly routine
- **WHEN** the user saves a routine with weekly recurrence selected
- **THEN** the created routine is submitted with a selected weekday

#### Scenario: User creates a custom routine
- **WHEN** the user saves a routine with custom recurrence selected
- **THEN** the created routine is submitted with the selected weekdays

#### Scenario: Custom recurrence has no selected weekdays
- **WHEN** the user selects custom recurrence without any weekdays
- **THEN** the save action is unavailable
