## ADDED Requirements

### Requirement: Home dashboard has a distinctive routine-tracking visual identity
The Home dashboard SHALL use a polished visual direction that feels specific to personal routine tracking while preserving a dense, scannable application layout.

#### Scenario: User opens refreshed Home
- **WHEN** the Home screen renders with routine and task data
- **THEN** the dashboard uses elevated visual hierarchy, routine-specific summary treatment, and refined spacing/color choices
- **AND** the user can still identify the same key routine and task information as before

#### Scenario: User interacts with refreshed Home
- **WHEN** the user toggles a task or signs out from Home
- **THEN** the existing callbacks and state transitions are preserved
