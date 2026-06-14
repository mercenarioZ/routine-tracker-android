## ADDED Requirements

### Requirement: Local routine creation preserves recurrence
The backend-independent local flow SHALL persist recurrence when creating routines and expose it through the routine dashboard stream.

#### Scenario: Routine is created with recurrence
- **WHEN** a valid routine is created locally with daily, weekly, or custom recurrence
- **THEN** the routine dashboard stream includes the created routine with the submitted recurrence

#### Scenario: Existing local routines have recurrence after migration
- **WHEN** existing local routine rows are loaded after the recurrence schema change
- **THEN** those routines expose daily recurrence

### Requirement: Seed routines include recurrence
The backend-independent local flow SHALL seed sample routines with recurrence values.

#### Scenario: Sample data is seeded
- **WHEN** the app launches with an empty local database
- **THEN** seeded routines expose recurrence values usable by Calendar
