## ADDED Requirements

### Requirement: Four primary bottom destinations
The app SHALL present Home, Routines, Tasks, and Settings as the primary bottom navigation destinations.

#### Scenario: Bottom bar displays active product areas
- **WHEN** the user is on a primary app destination after login
- **THEN** the bottom navigation shows Home, Routines, Tasks, and Settings
- **AND** the bottom navigation does not show Stats

### Requirement: Existing non-primary routes remain reachable by graph logic
The app SHALL keep non-primary routes out of the bottom bar without requiring them to be deleted from the navigation graph.

#### Scenario: Stats remains non-primary
- **WHEN** the app defines a Stats destination
- **THEN** Stats is not included in the primary bottom navigation list
- **AND** the route can remain registered for future internal or deep-link usage
