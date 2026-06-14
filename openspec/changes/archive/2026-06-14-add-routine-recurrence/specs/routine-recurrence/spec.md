## ADDED Requirements

### Requirement: Routines expose structured recurrence
Routines SHALL expose structured recurrence with daily, weekly, and custom modes.

#### Scenario: Routine has daily recurrence
- **WHEN** a routine is daily
- **THEN** the routine matches every calendar date

#### Scenario: Routine has weekly recurrence
- **WHEN** a routine is weekly for a weekday
- **THEN** the routine matches dates on that weekday
- **AND** the routine does not match dates on other weekdays

#### Scenario: Routine has custom recurrence
- **WHEN** a routine is custom for multiple weekdays
- **THEN** the routine matches dates on any selected weekday
- **AND** the routine does not match dates on unselected weekdays

### Requirement: Missing recurrence defaults safely
The app SHALL default missing or unrecognized routine recurrence to daily.

#### Scenario: Existing routine has no recurrence
- **WHEN** a locally persisted or remotely parsed routine lacks recurrence data
- **THEN** the domain routine exposes daily recurrence

### Requirement: Calendar uses routine recurrence
The Calendar route SHALL use structured routine recurrence to decide which dates show routine activity and selected-day routine rows.

#### Scenario: Selected day matches routine recurrence
- **WHEN** the selected calendar date matches an active routine's recurrence
- **THEN** the date shows routine activity
- **AND** the routine appears in the selected-day routine context

#### Scenario: Selected day does not match routine recurrence
- **WHEN** the selected calendar date does not match an active routine's recurrence
- **THEN** the routine does not contribute a routine activity indicator for that date
- **AND** the routine does not appear in the selected-day routine context
