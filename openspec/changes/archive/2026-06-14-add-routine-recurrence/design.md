## Context

`RoutineItem` currently exposes only `scheduleLabel`, so recurrence-like backend fields are flattened into display text and local routines cannot preserve structured recurrence. Calendar now needs recurrence to decide whether a routine belongs on a selected date.

The app already has a Room database with local routine/task persistence, Hilt repositories, and a Compose create-routine flow. Existing local data must continue to load after the model changes.

## Goals / Non-Goals

**Goals:**
- Add domain recurrence types for daily, weekly, and custom weekday schedules.
- Persist recurrence in local Room routine rows with a default for existing data.
- Parse remote recurrence-like fields into the domain model when available.
- Capture recurrence in the create-routine flow.
- Use recurrence in Calendar routine dots and selected-day routine rows.

**Non-Goals:**
- Do not implement date-range end dates, interval counts, exceptions, skipped dates, or timezone rules.
- Do not require backend create-routine support.
- Do not implement full recurrence editing for existing routines.

## Decisions

1. Model recurrence as `RoutineRecurrence`.
   - `Daily` matches every date.
   - `Weekly(dayOfWeek)` matches one weekday.
   - `Custom(daysOfWeek)` matches one or more weekdays.
   - Rationale: this exactly covers the requested daily, weekly, and custom modes while keeping date matching deterministic.

2. Store recurrence as mode plus weekday CSV in Room.
   - Rationale: the current database is simple and the recurrence shape is small. A separate recurrence table would add complexity before needed.
   - Existing rows default to daily through migration/default mapping.

3. Use Monday-first weekday values internally.
   - Rationale: the Calendar grid is Monday-first, so recurrence matching stays aligned with the UI.

4. Preserve repository boundaries.
   - `RoutineRepository.createRoutine` accepts recurrence.
   - UI ViewModels pass recurrence through repositories.
   - Concrete repositories map to local/remote formats.

5. Calendar filters selected-day routines by recurrence.
   - Rationale: this makes the routine dot date-specific instead of showing all active routines on every date.

## Risks / Trade-offs

- Existing backend recurrence field names may vary -> Parse common names and default safely to daily.
- Weekly recurrence needs a weekday -> Default newly created weekly routines to the current weekday until the user changes it.
- Local migration mistakes could drop routine data -> Add a Room migration from version 1 to 2 and tests for persistence/defaults.
- Custom recurrence with no selected days is invalid -> UI state must require at least one selected day for custom recurrence before saving.

## Migration Plan

1. Add recurrence types to the domain model.
2. Add Room columns and migration with daily defaults.
3. Update local/remote mappers and repositories.
4. Add create-routine controls for daily, weekly, and custom weekdays.
5. Update Calendar state mapping to match routines by selected date.
6. Add focused tests and run the standard verification commands.
