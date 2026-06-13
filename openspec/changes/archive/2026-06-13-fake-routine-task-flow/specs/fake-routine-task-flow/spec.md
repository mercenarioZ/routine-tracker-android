## ADDED Requirements

### Requirement: Fake routine creation updates local routine state
The fake repository flow SHALL allow routine creation without backend routine-create support and SHALL expose the newly created routine through the routine dashboard stream.

#### Scenario: Routine is created locally
- **WHEN** a valid routine is created through the fake routine repository
- **THEN** the routine dashboard stream includes a routine with the submitted title, schedule, category, and description

#### Scenario: Routine creation does not require backend create endpoint
- **WHEN** the app uses the fake repository flow
- **THEN** routine creation completes without calling the remote routine create endpoint

### Requirement: Fake routine creation generates a matching task
The fake repository flow SHALL create one pending task for each newly created routine.

#### Scenario: Task is generated from created routine
- **WHEN** a valid routine is created through the fake routine repository
- **THEN** the task stream includes a pending task whose routine id, title, time label, category, and description match the created routine

#### Scenario: Generated task is due today
- **WHEN** the fake repository flow creates a task from a newly created routine
- **THEN** the generated task uses the local "today" due label

### Requirement: Fake app flow can be tested without backend services
The fake repository flow SHALL allow developers to exercise create-routine and generated-task behavior without a live backend.

#### Scenario: Developer reaches routine flow without backend
- **WHEN** backend services are unavailable during local app-flow testing
- **THEN** the app can still reach the authenticated routine and task screens using local fake behavior

#### Scenario: Existing UI contracts remain unchanged
- **WHEN** the fake repository flow is enabled
- **THEN** routine and task screens continue using existing ViewModels and domain repository interfaces
