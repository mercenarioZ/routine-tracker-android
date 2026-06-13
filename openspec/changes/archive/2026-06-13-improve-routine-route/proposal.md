## Why

The primary navigation currently includes a Stats tab even though the screen is a placeholder, which makes the bottom bar feel heavier than the app's active workflow. The Routine route also has the right data but needs a stronger management experience so users can understand their routine system, filter quickly, and confidently create the next routine.

## What Changes

- Simplify the bottom tab bar to Home, Routines, Tasks, and Settings.
- Keep the existing Stats route available internally, but remove it from primary bottom navigation until it has meaningful product behavior.
- Redesign the Routine route with a summary header, active routine metrics, category insights, improved filtering, richer routine rows, and clearer empty/error states.
- Preserve existing Routine route behavior for loading data, filtering by category, and navigating to Create routine.

## Capabilities

### New Capabilities
- `primary-navigation`: Covers the app's bottom tab destinations and rules for what belongs in primary navigation.
- `routine-route-experience`: Covers the Routine route's user-visible management, filtering, summary, and empty/error presentation behavior.

### Modified Capabilities
- None.

## Impact

- Affected UI/navigation files: `app/src/main/java/com/nai/routinetracker/ui/navigation/**`.
- Affected Routine route files: `app/src/main/java/com/nai/routinetracker/ui/routines/**`.
- Affected resources: `app/src/main/res/values/strings.xml`.
- No new external dependencies, backend API changes, or persistence changes are expected.
