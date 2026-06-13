## ADDED Requirements

### Requirement: Home dashboard supports fast scanning
The Home dashboard SHALL present the day's key routine and task information with clear visual hierarchy and reduced text density.

#### Scenario: User opens Home with routines and tasks
- **WHEN** the Home screen renders with routine and task data
- **THEN** the user can identify the date, next routine, active routine count, streak total, and today's task list without reading long paragraphs.

### Requirement: Home dashboard uses visual cues
The Home dashboard SHALL use icons or compact visual markers for key summary areas and list metadata.

#### Scenario: User scans Home summary content
- **WHEN** the Home summary and list sections are visible
- **THEN** icons or compact markers distinguish routine focus, active routines, streak progress, and task timing/category metadata.

### Requirement: Home dashboard preserves existing behavior
The Home dashboard SHALL preserve existing loading, logout, routine display, task display, and task toggle behavior.

#### Scenario: User interacts with Home after visual refresh
- **WHEN** the user logs out or toggles a task from the Home screen
- **THEN** the same callbacks and state transitions used before the visual refresh are invoked.
