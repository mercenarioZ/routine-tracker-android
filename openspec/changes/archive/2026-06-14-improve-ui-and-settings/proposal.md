## Why

The app is now usable without a backend, so the primary screens should feel more polished and intentional for repeated local use. Settings is already a primary destination, but it currently renders as a placeholder and needs basic controls that make the app feel complete.

## What Changes

- Refresh the core app UI with a more distinctive, routine-focused visual style while preserving existing Home, Routines, Tasks, and create-routine behavior.
- Replace the placeholder Settings screen with basic local settings sections and actions.
- Add settings controls for reminder preference, reminder time, local data reset, app information, and sign out.
- Keep Settings reachable from the existing primary bottom navigation.

## Capabilities

### New Capabilities
- `settings-experience`: Defines the Settings route, local controls, destructive data reset affordance, and sign-out behavior.

### Modified Capabilities
- `home-dashboard-visuals`: Strengthens the dashboard visual polish requirements beyond scanability.
- `routine-route-experience`: Requires the routine route to retain its create action and list behavior while adopting the refreshed visual direction.
- `primary-navigation`: Requires Settings to open a functional settings experience instead of a placeholder.

## Impact

- Affected UI areas: `ui/home`, `ui/routines`, `ui/tasks`, `ui/settings`, `ui/navigation`, and shared theme resources as needed.
- Affected data/session areas: local routine/task repositories or DAOs only if required for the reset action; auth/session flow only for sign out.
- No backend API changes are expected.
