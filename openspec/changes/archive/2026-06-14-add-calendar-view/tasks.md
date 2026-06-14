## 1. Navigation Setup

- [x] 1.1 Add a `Calendar` app destination with label and Material icon.
- [x] 1.2 Add Calendar to `bottomBarDestinations` between Routines and Tasks.
- [x] 1.3 Register the Calendar route in `AppNavHost`.
- [x] 1.4 Add the Calendar tab string resource.

## 2. Calendar State Model

- [x] 2.1 Create `ui/calendar` package with route, screen, ViewModel, and UI state files.
- [x] 2.2 Model visible month, selected date, today, month grid days, loading state, selected-day routines, and selected-day tasks.
- [x] 2.3 Implement previous-month, next-month, date-selection, and task-toggle ViewModel events.
- [x] 2.4 Derive task date mapping from existing due labels for today and tomorrow without changing repository interfaces.
- [x] 2.5 Derive selected-day routine context from active routines in the existing routine dashboard stream.

## 3. Calendar UI

- [x] 3.1 Build a Material 3 Calendar route that collects ViewModel state with `collectAsStateWithLifecycle`.
- [x] 3.2 Build a month header with previous and next month controls.
- [x] 3.3 Build a stable month grid with weekday headers, today highlight, selected-date highlight, and activity indicators.
- [x] 3.4 Build a selected-day agenda showing routine context, task rows, task completion state, and empty states.
- [x] 3.5 Keep the layout usable across compact and larger screens without overlapping bottom navigation.

## 4. Verification

- [x] 4.1 Add focused unit tests for month grid construction and selected-date changes.
- [x] 4.2 Add focused unit tests for today/tomorrow task mapping and task toggle delegation.
- [x] 4.3 Run `./gradlew testDebugUnitTest`.
- [x] 4.4 Run `./gradlew lintDebug`.
- [x] 4.5 Run `./gradlew assembleDebug`.
- [x] 4.6 Run `openspec validate --all`.
