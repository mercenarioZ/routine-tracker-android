## Why

The app currently separates routines and tasks into list-based views, which makes it hard for users to understand what is scheduled across nearby days. A calendar view will give users a time-oriented way to scan routine and task commitments without leaving the main app flow.

## What Changes

- Add a Calendar primary destination that presents a month-oriented calendar with day-level routine/task indicators.
- Let users select a day and review the routines and tasks scheduled for that date in a compact agenda.
- Reuse existing routine and task data through ViewModels/repositories instead of introducing a separate calendar data source.
- Add Calendar to the bottom navigation while preserving existing Home, Routines, Tasks, and Settings destinations.
- Keep Stats registered as a non-primary route.

## Capabilities

### New Capabilities
- `calendar-view`: Defines the user-visible calendar destination, month navigation, day selection, and selected-day agenda behavior.

### Modified Capabilities
- `primary-navigation`: Adds Calendar to the primary bottom navigation destination set.

## Impact

- UI: new `ui/calendar` route, screen, ViewModel, and calendar-specific UI state.
- Navigation: add a Calendar destination, string resource, Material icon, NavHost route, and bottom navigation entry.
- Domain/data: read existing routine and task streams through current repository interfaces; no backend API changes are expected.
- Tests: focused ViewModel/unit coverage for date selection, month navigation, and calendar agenda mapping.
