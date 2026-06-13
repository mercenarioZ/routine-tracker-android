## Context

The app already has fake task and routine repository implementations, and `FakeRoutineRepository.createRoutine()` already calls `TaskRepository.createTaskFromRoutine(routine)`. However, Hilt currently binds `RoutineRepository` to `RemoteRoutineRepository`, whose create operation fails because backend routine creation is not wired. This blocks local end-to-end testing of the create-routine path.

The app also uses auth state to decide whether to show the login flow or the main app. If the test flow must avoid backend support entirely, auth should also be satisfiable locally so developers can reach the Routines and Tasks screens without a live login endpoint.

## Goals / Non-Goals

**Goals:**
- Enable backend-independent testing of routine creation and automatic task generation.
- Reuse the existing `RoutineRepository` and `TaskRepository` interfaces so UI and ViewModel code do not need special fake-mode branches.
- Preserve the existing fake task generation behavior: generated tasks inherit routine title, schedule, category, and description.
- Add focused regression coverage around the fake routine-to-task behavior.

**Non-Goals:**
- Implement real backend routine creation.
- Persist fake routines or generated tasks across app restarts.
- Change the create-routine UI, navigation structure, or task rendering design.
- Add new product scheduling rules beyond one generated pending task for the created routine.

## Decisions

1. Use repository binding to activate the fake flow.

   The implementation should wire `RoutineRepository` to `FakeRoutineRepository` for the local fake flow and keep `TaskRepository` wired to `FakeTaskRepository`. This uses the same dependency path the app already uses in production code:

   ```text
   CreateRoutineScreen -> RoutineViewModel -> RoutineRepository -> TaskRepository
   ```

   Alternative considered: add fake-specific branches in `RoutineViewModel`. That would make UI state harder to reason about and bypass the domain abstraction the project already uses.

2. Add fake auth only if needed to test without backend.

   If the app must be launched from a clean install without backend services, add a `FakeAuthRepository` that accepts valid non-empty credentials and saves a local `AuthSession`. This keeps `AuthViewModel` and `LoginViewModel` unchanged while allowing the user to reach the authenticated app shell.

   Alternative considered: bypass auth navigation in `AppNavHost`. That would mix test setup with navigation behavior and could hide auth regressions.

3. Keep fake state in memory.

   Fake routines and tasks should continue using in-memory `MutableStateFlow` state. This is enough for app-flow testing and avoids introducing persistence or migration concerns.

   Alternative considered: persist fake data in DataStore. That would make manual testing stateful across runs, but it would add cleanup complexity and is not necessary for the requested flow.

4. Test repository behavior directly.

   The first regression test should instantiate the fake repositories, create a routine, and assert that both the routine dashboard and task stream reflect the new routine. This tests the core behavior without requiring Compose or instrumentation.

   Alternative considered: only add an instrumentation test. That would be closer to the manual flow, but it is slower and more brittle than a focused repository test.

## Risks / Trade-offs

- Fake bindings could accidentally replace remote behavior in a build intended for backend testing -> Keep the implementation explicit and easy to revert or isolate behind debug-only wiring if needed.
- In-memory fake state resets when the process restarts -> Acceptable for local flow testing; persistence is out of scope.
- Fake auth may make login appear more complete than it is -> Keep naming and implementation clearly fake, and avoid changing remote auth code.
- Repository unit tests may need lightweight Android context support because fake seed data reads string resources -> Prefer focused tests using available Android/JVM test tooling; if not available, add only the minimal test dependency needed.

## Migration Plan

1. Add or update fake repository wiring for the local app-flow test.
2. Add fake auth support if the flow must start from login without backend.
3. Add focused tests for routine creation and generated task creation.
4. Run `./gradlew testDebugUnitTest`, `./gradlew lintDebug`, `./gradlew assembleDebug`, and `openspec validate --all`.
5. To return to remote behavior, restore `RoutineRepository` and `AuthRepository` bindings to the remote implementations or move fake bindings behind a debug-only source set.

## Open Questions

- Should fake repository bindings be the default debug behavior, or should they be isolated behind a dedicated product flavor/build type?
- Should fake auth be included in this change, or is the intended test path to use an existing saved session?
