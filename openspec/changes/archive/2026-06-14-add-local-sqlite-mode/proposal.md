## Why

The app can currently run without backend services only through in-memory fake repositories, so created routines and task state disappear when the process restarts. A local SQLite-backed mode lets developers and testers use the app without a backend while preserving routine and task data across launches.

## What Changes

- Replace the runtime fake routine/task data source with a Room-backed local SQLite data source.
- Seed the local database with the existing sample routines and tasks on first launch so the app still opens with useful demo data.
- Persist locally created routines, generated tasks, and task completion toggles across process restarts.
- Keep the existing repository interfaces and ViewModel/UI contracts unchanged.
- Keep authentication backend-independent through the existing fake auth/session flow.

## Capabilities

### New Capabilities

- None.

### Modified Capabilities

- `fake-routine-task-flow`: The backend-independent local flow persists routines and tasks in SQLite instead of keeping them only in memory.

## Impact

- Adds Room/SQLite dependencies and generated code through KSP.
- Adds local database entities, DAOs, and Room database setup under the data layer.
- Replaces Hilt bindings for routine/task repositories with local persistent implementations.
- Keeps remote API classes available but unused by the default repository bindings.
