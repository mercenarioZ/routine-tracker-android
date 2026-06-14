## Why

The calendar can only show routine dots accurately if routines expose recurrence as structured data instead of display-only schedule text. Adding recurrence now lets Calendar distinguish daily, weekly, and custom routine dates without guessing from labels.

## What Changes

- Add structured routine recurrence with three modes: daily, weekly, and custom.
- Persist recurrence in local routine storage and expose it through `RoutineItem`.
- Parse remote routine recurrence-like fields into the domain model when available.
- Let users choose recurrence when creating routines.
- Update Calendar routine dots and agenda routine context to use recurrence date matching.
- Preserve existing routines by defaulting missing recurrence to daily.

## Capabilities

### New Capabilities
- `routine-recurrence`: Defines routine recurrence modes, persistence, DTO mapping, create-routine selection, and calendar date matching.

### Modified Capabilities
- `fake-routine-task-flow`: Created and seeded local routines include recurrence while preserving existing local flow behavior.
- `routine-route-experience`: Create routine flow captures recurrence as part of routine details.

## Impact

- Model: add routine recurrence types to `RoutineItem`.
- Data: add local Room recurrence columns with migration/defaults; map recurrence in local and remote DTO layers.
- Domain: extend `RoutineRepository.createRoutine` with recurrence.
- UI: add recurrence controls to Create Routine and show recurrence context where routines are displayed.
- Calendar: use recurrence to decide which dates receive routine indicators and selected-day routine rows.
- Tests: add coverage for recurrence mapping, persistence/defaults, create UI state, and calendar matching.
