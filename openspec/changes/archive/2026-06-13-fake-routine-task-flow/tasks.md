## 1. Fake Repository Wiring

- [x] 1.1 Add a `FakeAuthRepository` that implements `AuthRepository` and saves a local fake `AuthSession` for valid login input.
- [x] 1.2 Update repository bindings so the local fake flow uses `FakeAuthRepository`, `FakeRoutineRepository`, and `FakeTaskRepository`.
- [x] 1.3 Confirm `FakeRoutineRepository.createRoutine()` preserves the submitted routine fields and calls `TaskRepository.createTaskFromRoutine()`.
- [x] 1.4 Confirm `FakeTaskRepository.createTaskFromRoutine()` creates one pending task with matching routine id, title, time label, category, and description.

## 2. Regression Coverage

- [x] 2.1 Add a focused test for creating a routine through the fake routine repository.
- [x] 2.2 Assert the routine dashboard stream includes the newly created routine.
- [x] 2.3 Assert the task stream includes the generated pending task with matching routine details and today's due label.
- [x] 2.4 Add minimal test support dependencies only if required by the existing Android/JVM test setup.

## 3. Verification

- [x] 3.1 Run `./gradlew testDebugUnitTest`.
- [x] 3.2 Run `./gradlew lintDebug`.
- [x] 3.3 Run `./gradlew assembleDebug`.
- [x] 3.4 Run `openspec validate --all`.
