## MODIFIED Requirements

### Requirement: Fake routine creation updates local routine state
The backend-independent local flow SHALL allow routine creation without backend routine-create support and SHALL persist the newly created routine in local SQLite storage before exposing it through the routine dashboard stream.

#### Scenario: Routine is created locally
- **WHEN** a valid routine is created through the backend-independent local routine repository
- **THEN** the routine dashboard stream includes a routine with the submitted title, schedule, category, and description

#### Scenario: Routine creation does not require backend create endpoint
- **WHEN** the app uses the backend-independent local repository flow
- **THEN** routine creation completes without calling the remote routine create endpoint

#### Scenario: Created routine survives restart
- **WHEN** a routine is created locally and the app process restarts
- **THEN** the routine dashboard stream includes the created routine after the local database is reopened

### Requirement: Fake routine creation generates a matching task
The backend-independent local flow SHALL create and persist one pending task for each newly created routine.

#### Scenario: Task is generated from created routine
- **WHEN** a valid routine is created through the backend-independent local routine repository
- **THEN** the task stream includes a pending task whose routine id, title, time label, category, and description match the created routine

#### Scenario: Generated task is due today
- **WHEN** the backend-independent local flow creates a task from a newly created routine
- **THEN** the generated task uses the local "today" due label

#### Scenario: Generated task survives restart
- **WHEN** a routine is created locally and the app process restarts
- **THEN** the generated task remains available from the local task stream after the local database is reopened

### Requirement: Fake app flow can be tested without backend services
The backend-independent local flow SHALL allow developers to exercise create-routine, generated-task, and task-toggle behavior without a live backend while preserving local data across app process restarts.

#### Scenario: Developer reaches routine flow without backend
- **WHEN** backend services are unavailable during local app-flow testing
- **THEN** the app can still reach the authenticated routine and task screens using local behavior

#### Scenario: Existing UI contracts remain unchanged
- **WHEN** the backend-independent local repository flow is enabled
- **THEN** routine and task screens continue using existing ViewModels and domain repository interfaces

#### Scenario: Task toggle survives restart
- **WHEN** a task is toggled locally and the app process restarts
- **THEN** the task stream exposes the toggled task status after the local database is reopened

#### Scenario: Sample data is available on first launch
- **WHEN** the app launches with an empty local database
- **THEN** the backend-independent local flow seeds the database with sample routines and tasks
