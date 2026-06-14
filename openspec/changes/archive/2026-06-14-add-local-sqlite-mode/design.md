## Context

The current app already hides data sources behind domain repository interfaces. Hilt currently binds `AuthRepository`, `RoutineRepository`, and `TaskRepository` to fake implementations, allowing the app to run without backend services. Those fake routine/task repositories use `MutableStateFlow`, so their state is lost on process restart.

The desired behavior is a backend-independent local app mode with durable routine and task data. Android's standard SQLite abstraction for this project is Room, using KSP for generated DAO/database code.

## Goals / Non-Goals

**Goals:**

- Persist routines and tasks locally using SQLite through Room.
- Preserve the existing ViewModel and UI contracts.
- Seed the local database once with current sample routines/tasks.
- Keep task generation from newly created routines.
- Keep fake auth/session behavior sufficient for offline app access.

**Non-Goals:**

- Backend synchronization, conflict resolution, or offline queueing.
- Multi-user local account separation.
- Schema migrations beyond the initial local database version.
- Replacing DataStore-backed auth session persistence.

## Decisions

- Use Room as the SQLite access layer.
  - Rationale: Room provides typed entities, DAO validation, `Flow` query support, and KSP integration that matches the existing Gradle setup.
  - Alternative considered: direct `SQLiteOpenHelper`; rejected because it would add manual cursor mapping and less compile-time validation.

- Add local entities and DAOs under `data/local`.
  - Rationale: This matches the existing separation where remote DTO/API classes live under `data/remote` and repository implementations live under `data/repository`.
  - Alternative considered: placing Room models beside domain models; rejected to keep persistence schema concerns out of domain models.

- Implement `LocalRoutineRepository` and `LocalTaskRepository` behind existing domain interfaces.
  - Rationale: ViewModels already depend on interfaces, so swapping the data source should not require UI changes.
  - Alternative considered: adding new local-only interfaces; rejected because that would leak implementation mode upward.

- Seed the database lazily from repository initialization through a dedicated seeder.
  - Rationale: Seeding before first query ensures the existing demo experience remains available while avoiding prepackaged database assets.
  - Alternative considered: Room callback seeding; rejected because dependency-injected string resources and existing seed builders are easier to use from an injected seeder.

- Keep `FakeAuthRepository` as the default auth implementation.
  - Rationale: The user asked to run without backend; fake auth already writes an `AuthSession` so the app can enter authenticated routes without a remote login call.
  - Alternative considered: removing login entirely; rejected because it would change navigation/auth behavior outside the storage goal.

## Risks / Trade-offs

- Room dependency download or generated-code configuration can fail on older cache state -> verify with Gradle build/test and adjust version catalog/KSP wiring if needed.
- One-time seeding can race with first observers -> serialize seeding behind a mutex before DAO reads/writes.
- Task and routine data can diverge if create flow inserts one but not the other -> perform routine creation and generated task insertion in a single Room transaction.
- Future backend sync may need additional metadata -> keep version 1 schema simple and add migrations when sync requirements exist.
