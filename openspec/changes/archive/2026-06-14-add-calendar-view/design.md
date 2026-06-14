## Context

The app is a single-module Kotlin/Compose Android app with primary destinations wired through `AppDestination`, `bottomBarDestinations`, and `AppNavHost`. Routine and task data already flows through domain repository interfaces into screen ViewModels.

The current domain models do not expose structured recurrence rules or due dates. Routines have `scheduleLabel`, and tasks have `dueLabel` plus `timeLabel`. The calendar view therefore needs to provide a useful calendar-oriented UI while staying honest about the available data.

## Goals / Non-Goals

**Goals:**
- Add a Calendar destination as a first-class bottom navigation tab.
- Show a month grid with today highlighting, selected-day state, and day-level indicators for routine/task activity.
- Show a selected-day agenda using existing routine and task streams.
- Keep calendar UI state in a Hilt ViewModel with immutable `StateFlow`.
- Preserve existing repository interfaces and backend/local data behavior.

**Non-Goals:**
- Do not introduce real recurrence-rule parsing, backend calendar APIs, or database date columns in this change.
- Do not implement routine editing, task creation, or drag-and-drop scheduling from the calendar.
- Do not remove or repurpose the existing Routines, Tasks, Home, Settings, or Stats routes.

## Decisions

1. Implement Calendar as a Compose route backed by `CalendarViewModel`.
   - Rationale: this follows the existing route/screen/ViewModel pattern used by Home, Routines, Tasks, and Settings.
   - Alternative considered: build calendar state inside `AppNavHost`. That would mix navigation and business state, so it is rejected.

2. Use existing `RoutineRepository.observeDashboard()` and `TaskRepository.observeTasks()` as the data source.
   - Rationale: the proposal does not require a data model migration, and repository reuse keeps the calendar compatible with both local and remote modes.
   - Alternative considered: create a new calendar repository. That would add an abstraction before the app has dedicated calendar persistence semantics.

3. Derive calendar agenda membership from current labels.
   - Tasks with a due label matching today appear on the device's current date; tasks matching tomorrow appear on the next date when recognizable.
   - Active routines are shown as routine context for the selected date using their existing schedule labels.
   - Rationale: this gives users a useful time-oriented view without fabricating unsupported recurrence data.
   - Alternative considered: parse all schedule labels into dates. The labels are display text, not stable recurrence rules, so full parsing would be brittle.

4. Keep the month grid implementation local to the app using platform date APIs.
   - Rationale: the app does not currently configure Java time desugaring for minSdk 24, and the calendar needs only month/day calculations.
   - Alternative considered: add a calendar UI dependency. That is unnecessary for the initial month grid and would increase dependency surface.

5. Add Calendar to bottom navigation between Routines and Tasks.
   - Rationale: Calendar is a planning surface that naturally sits between routine management and task execution.
   - Alternative considered: put Calendar after Tasks. That is workable, but the Routines -> Calendar -> Tasks order better matches planning flow.

## Risks / Trade-offs

- Limited date semantics → Be clear in implementation that task placement is based on existing labels and keep richer date storage as future work.
- Five bottom tabs on smaller devices → Use short label text and the Material 3 bottom navigation component already in use.
- Label matching may be locale-sensitive → Centralize the mapping logic in the ViewModel or a small helper so it can be replaced when structured due dates exist.
- Month grid edge cases → Add focused unit tests for month start offset, selected-day agenda updates, and today/tomorrow task mapping.

## Migration Plan

1. Add calendar UI state, ViewModel, route, and screen under `ui/calendar`.
2. Add `AppDestination.Calendar`, the `tab_calendar` string resource, bottom navigation entry, and NavHost composable.
3. Verify existing routes still navigate and Calendar renders for logged-in users.
4. Add focused unit tests for calendar state derivation.
5. Rollback by removing the Calendar destination and `ui/calendar` package if needed; no persistent data migration is involved.

## Open Questions

- Should future work add structured task due dates and routine recurrence rules to local/remote models?
- Should the calendar eventually support weekly agenda mode or only month view?
