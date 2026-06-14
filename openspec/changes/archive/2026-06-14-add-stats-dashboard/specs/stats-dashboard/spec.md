## ADDED Requirements

### Requirement: Stats route summarizes routine and task progress
The Stats route SHALL present data-backed summary metrics for the loaded routines and tasks.

#### Scenario: Stats data is loaded
- **WHEN** the user opens Stats after routine and task data have loaded
- **THEN** the route displays task completion, open task count, active routine count, and streak information derived from the loaded data

### Requirement: Stats route includes multiple chart views
The Stats route SHALL include at least one pie-style chart and one column-style chart for fast visual comparison.

#### Scenario: User scans visual stats
- **WHEN** routines or tasks exist
- **THEN** the route displays a pie-style completion or category chart
- **AND** the route displays column-style comparisons for category or routine activity

### Requirement: Stats route handles loading and empty data
The Stats route SHALL communicate loading and empty states without showing misleading chart values.

#### Scenario: Stats data is loading
- **WHEN** routine and task data are still loading
- **THEN** the route displays a loading indicator

#### Scenario: No stats data exists
- **WHEN** loading is complete and no routines or tasks exist
- **THEN** the route displays an empty state instead of populated charts
