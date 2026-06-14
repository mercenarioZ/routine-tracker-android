# primary-navigation Specification

## Purpose
Defines which destinations belong in the app's primary bottom navigation and how non-primary routes are handled.
## Requirements
### Requirement: Primary bottom destinations
The app SHALL present Home, Routines, Calendar, Tasks, Stats and Settings as the primary bottom navigation destinations.

#### Scenario: Bottom bar displays active product areas
- **WHEN** the user is on a primary app destination after login
- **THEN** the bottom navigation shows Home, Routines, Calendar, Tasks, Stats, and Settings
- **AND** the bottom navigation does not show Stats

### Requirement: Existing non-primary routes remain reachable by graph logic
The app SHALL keep non-primary routes out of the bottom bar without requiring them to be deleted from the navigation graph.

#### Scenario: Create routine remains non-primary
- **WHEN** the app defines a Create routine destination
- **THEN** Create routine is not included in the primary bottom navigation list
- **AND** the route can remain registered for internal navigation from Routines

### Requirement: Settings destination opens a functional experience
The primary Settings destination SHALL open the functional settings experience.

#### Scenario: User taps Settings tab
- **WHEN** the user activates Settings from the primary bottom navigation
- **THEN** the app navigates to the Settings route
- **AND** the route displays editable settings and account actions instead of placeholder content
