## 1. Room Setup

- [x] 1.1 Add Room runtime, KTX, and compiler dependencies to the version catalog and app module.
- [x] 1.2 Create Room database, entities, DAOs, and type mapping helpers for routines and tasks.
- [x] 1.3 Add a Hilt module that provides the Room database and DAOs.

## 2. Local Repository Flow

- [x] 2.1 Add a local database seeder that inserts the existing sample routines and tasks when the database is empty.
- [x] 2.2 Implement `LocalTaskRepository` with persisted task observation, creation, and toggle behavior.
- [x] 2.3 Implement `LocalRoutineRepository` with persisted dashboard observation and transactional routine/task creation.
- [x] 2.4 Bind default routine/task repositories to local persistent implementations while keeping fake auth for backend-independent access.

## 3. Verification

- [x] 3.1 Add focused unit tests for local repository persistence behavior using a Room test database.
- [x] 3.2 Run OpenSpec validation and Gradle checks for the changed data layer.
