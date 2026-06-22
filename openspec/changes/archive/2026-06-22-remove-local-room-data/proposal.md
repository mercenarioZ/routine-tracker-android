## Why

The app now uses backend repositories for routines and tasks, so the Room-backed local routine/task mode is stale complexity. Removing it keeps the data flow backend-first and avoids maintaining unused DAOs, migrations, local seed/reset behavior, and tests.

## What Changes

- Remove Room database, DAO, entity, mapper, seeder, and local repository implementations for routine/task data.
- Remove the Settings local data reset feature because there will no longer be local routine/task data to reset.
- Remove Room dependencies and tests that only verify the deleted local SQLite path.
- Preserve backend-backed routine/task repositories and DataStore-backed auth/settings preferences.

## Capabilities

### New Capabilities

- None.

### Modified Capabilities

- `settings-experience`: Settings no longer exposes local routine/task data reset.
- `fake-routine-task-flow`: Backend-independent SQLite routine/task flow is removed.

## Impact

- Affected code: `data/local`, local repository implementations, Hilt database/repository bindings, Settings UI/ViewModel state, Room-only tests, and Gradle Room dependencies.
- Data behavior: routine/task data is backend-only at runtime; auth session and user settings remain locally persisted through DataStore.
