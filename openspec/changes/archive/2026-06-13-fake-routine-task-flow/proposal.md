## Why

Routine creation cannot currently be tested end-to-end without backend support because the active routine repository is remote and its create operation is not wired. A local fake flow will let the app exercise create-routine behavior and automatic task generation during development and verification.

## What Changes

- Add a backend-independent fake repository flow for routine creation.
- Ensure creating a routine through the fake flow updates the routine dashboard data.
- Ensure creating a routine automatically creates a pending task using the routine's title, schedule, category, and description.
- Keep the flow local to repository wiring and fake data behavior so UI screens continue using existing ViewModels and domain interfaces.
- Add regression coverage for the routine-to-task generation behavior.

## Capabilities

### New Capabilities
- `fake-routine-task-flow`: Defines backend-independent fake repository behavior for creating routines and generating matching tasks.

### Modified Capabilities
- None.

## Impact

- Affected code:
  - `app/src/main/java/com/nai/routinetracker/data/repository/**`
  - `app/src/main/java/com/nai/routinetracker/di/RepositoryModule.kt`
  - `app/src/test/**`
- No backend API contract changes.
- No new runtime dependencies expected.
- Verification should include focused unit tests plus the standard Gradle and OpenSpec checks.
