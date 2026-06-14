## Why

The Stats route currently renders placeholder text, so users cannot review routine or task progress beyond the Home summary. Adding a real stats dashboard gives users a compact way to compare completion, category mix, and routine streak health.

## What Changes

- Replace the placeholder Stats screen with a data-backed dashboard.
- Show multiple visual summaries, including a completion pie-style chart and column-style comparisons.
- Derive stats from the existing routine and task repositories without introducing new backend APIs.
- Add Stats to the primary bottom navigation so users can reach the dashboard.

## Capabilities

### New Capabilities
- `stats-dashboard`: Defines the Stats route's visual summaries and data-backed empty/loading behavior.

### Modified Capabilities
- `primary-navigation`: Stats becomes a primary bottom navigation destination.

## Impact

- Affected UI: `ui/stats`, `ui/navigation`, string resources.
- Affected state: new stats ViewModel/UI state combining existing routine and task repository flows.
- No new network APIs, database schema, or external dependencies are expected.
