## Context

The app currently binds routine and task repositories to remote Retrofit implementations, while a separate Room-backed local routine/task path remains in the codebase. That local path includes DAOs, entities, migrations, seed data, local repositories, local reset settings behavior, and tests, but it is no longer the runtime source of truth for routine/task screens.

## Goals / Non-Goals

**Goals:**

- Remove unused Room-backed routine/task storage and related Hilt bindings.
- Keep the active backend-backed routine/task flow unchanged.
- Keep DataStore-backed auth session and settings preferences.
- Remove Settings UI and ViewModel behavior that resets local routine/task sample data.

**Non-Goals:**

- Do not add offline sync or local caching.
- Do not change backend API contracts.
- Do not remove fake in-memory test helpers unless they depend on Room.

## Decisions

- Remove Room instead of leaving unused DAOs. The active app flow is backend-first, and unused local persistence adds dependency, migration, and test maintenance cost.
- Remove the local data reset action from Settings. Without local routine/task storage, the action would either do nothing or imply offline data that no longer exists.
- Keep DataStore dependencies. Auth session and app settings are still intentionally local and are unrelated to Room/DAO removal.

## Risks / Trade-offs

- Existing tests that exercised local SQLite behavior will be deleted, reducing coverage for a feature that is intentionally removed.
- Developers lose the SQLite-backed backend-independent app flow; backend-dependent screens will require a working backend or separate fake/test repository setup.
