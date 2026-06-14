## 1. Domain and Persistence

- [x] 1.1 Add routine recurrence domain types and date matching helpers.
- [x] 1.2 Extend `RoutineItem` and `RoutineRepository.createRoutine` with recurrence.
- [x] 1.3 Add Room recurrence columns, version migration, and local mapper serialization.
- [x] 1.4 Update sample seed data and local repositories to provide recurrence.
- [x] 1.5 Parse remote recurrence fields into the domain model with daily defaults.

## 2. Create Routine UI

- [x] 2.1 Add recurrence state and validation to `CreateRoutineUiState`.
- [x] 2.2 Add daily, weekly, and custom weekday controls to `CreateRoutineScreen`.
- [x] 2.3 Pass selected recurrence from `RoutineViewModel` to the repository.
- [x] 2.4 Add string resources for recurrence labels and weekday controls.

## 3. Calendar Integration

- [x] 3.1 Update calendar month-day routine counts to use recurrence matching.
- [x] 3.2 Update selected-day routine agenda to include only matching active routines.
- [x] 3.3 Update calendar tests to cover daily, weekly, custom, and inactive routine matching.
- [x] 3.4 Update the active calendar-view spec to reflect recurrence-based routine indicators.

## 4. Verification

- [x] 4.1 Add or update focused unit tests for recurrence defaults, local persistence/migration, DTO parsing, and create-routine validation.
- [x] 4.2 Run `./gradlew testDebugUnitTest`.
- [x] 4.3 Run `./gradlew lintDebug`.
- [x] 4.4 Run `./gradlew assembleDebug`.
- [x] 4.5 Run `openspec validate --all`.
