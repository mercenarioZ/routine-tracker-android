## Why

The Home screen currently presents too many similarly weighted text blocks, which makes it harder to scan daily progress quickly. Improving hierarchy and adding icon-led cues will make the first screen feel calmer, more useful, and more modern.

## What Changes

- Redesign the Home screen header to be more compact and action-oriented.
- Replace text-heavy summary cards with icon-supported dashboard sections.
- Make routine and task sections easier to scan with concise titles, visual metadata, and stronger spacing.
- Preserve existing Home screen behavior, navigation, task toggling, and data sources.

## Capabilities

### New Capabilities
- `home-dashboard-visuals`: Covers the visual hierarchy, icon usage, and scanability requirements for the Home dashboard.

### Modified Capabilities

## Impact

- Affected UI code is primarily under `app/src/main/java/com/nai/routinetracker/ui/home/**`.
- Shared task card styling under `app/src/main/java/com/nai/routinetracker/ui/tasks/components/**` may be adjusted only if needed for Home consistency.
- No API, repository, database, navigation route, or dependency changes are expected.
