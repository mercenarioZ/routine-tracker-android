## ADDED Requirements

### Requirement: Calendar route presents a month view
The Calendar route SHALL present a month-oriented calendar grid that allows users to scan routine and task activity by date.

#### Scenario: User opens Calendar
- **WHEN** the user opens the Calendar destination after login
- **THEN** the route displays the current month
- **AND** the current date is visually distinguishable
- **AND** the selected date is visually distinguishable

#### Scenario: User changes visible month
- **WHEN** the user activates month navigation controls
- **THEN** the calendar grid updates to the previous or next month
- **AND** the selected date remains within the visible month when possible

### Requirement: Calendar route supports day selection
The Calendar route SHALL let users select a day from the visible month and review date-specific routine and task context.

#### Scenario: User selects a day
- **WHEN** the user selects a date in the visible month grid
- **THEN** the selected-day agenda updates to that date
- **AND** the selected date is visually indicated in the grid

#### Scenario: Selected day has no matching tasks
- **WHEN** the selected date has no tasks mapped from existing due labels
- **THEN** the selected-day agenda displays an empty task state for that date

### Requirement: Calendar route maps existing tasks onto calendar days
The Calendar route SHALL derive day-level task indicators and selected-day task lists from the existing task stream without requiring new backend date fields.

#### Scenario: Task is due today
- **WHEN** a task's due label maps to the device's current date
- **THEN** the current date shows task activity in the calendar grid
- **AND** the task appears in the selected-day agenda when the current date is selected

#### Scenario: Task is due tomorrow
- **WHEN** a task's due label maps to the day after the device's current date
- **THEN** the next date shows task activity in the calendar grid
- **AND** the task appears in the selected-day agenda when that date is selected

### Requirement: Calendar route includes routine context
The Calendar route SHALL include active routine context in the selected-day agenda using existing routine dashboard data and structured routine recurrence.

#### Scenario: Active routines are available
- **WHEN** routine dashboard data contains active routines whose recurrence matches the selected date
- **THEN** the selected-day agenda displays matching routine titles, categories, and schedule labels as routine context

#### Scenario: Active routine does not match selected date
- **WHEN** routine dashboard data contains an active routine whose recurrence does not match the selected date
- **THEN** the selected-day agenda does not display that routine

#### Scenario: Routines and tasks are loading
- **WHEN** routine or task data is still loading
- **THEN** the Calendar route displays a loading state instead of stale agenda content

### Requirement: Calendar route preserves task toggle behavior
The Calendar route SHALL allow task completion changes through the existing task repository behavior.

#### Scenario: User toggles a task from the selected-day agenda
- **WHEN** the user toggles a task shown in the Calendar agenda
- **THEN** the app invokes the existing task toggle behavior for that task
- **AND** the updated task status is reflected when the task stream emits the change
