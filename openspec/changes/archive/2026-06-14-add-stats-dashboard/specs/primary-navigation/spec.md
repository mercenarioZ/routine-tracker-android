## MODIFIED Requirements

### Requirement: Four primary bottom destinations
The app SHALL present Home, Routines, Tasks, Stats, and Settings as the primary bottom navigation destinations.

#### Scenario: Bottom bar displays active product areas
- **WHEN** the user is on a primary app destination after login
- **THEN** the bottom navigation shows Home, Routines, Tasks, Stats, and Settings

### Requirement: Existing non-primary routes remain reachable by graph logic
The app SHALL keep non-primary routes out of the bottom bar without requiring them to be deleted from the navigation graph.

#### Scenario: Create routine remains non-primary
- **WHEN** the app defines a Create routine destination
- **THEN** Create routine is not included in the primary bottom navigation list
- **AND** the route can remain registered for internal navigation from Routines
