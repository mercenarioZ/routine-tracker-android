# routine-route-experience Specification

## Purpose
Defines the Routine route's user-visible management, filtering, summary, empty-state, and create-action behavior.

## Requirements
### Requirement: Routine route summarizes the routine system
The Routine route SHALL present a summary of the user's routine system before the routine list.

#### Scenario: Routines are loaded
- **WHEN** routines have loaded
- **THEN** the Routine route displays total routines, active routines, and best streak information derived from the loaded routines

#### Scenario: No routines are loaded
- **WHEN** loading is complete and there are no routines
- **THEN** the Routine route displays an empty state that encourages creating a routine

### Requirement: Routine filtering remains clear and usable
The Routine route SHALL let users filter routines by category while clearly showing the current category context.

#### Scenario: All categories selected
- **WHEN** no category filter is selected
- **THEN** all routines are visible
- **AND** the filter summary communicates that all categories are being shown

#### Scenario: Specific category selected
- **WHEN** a category filter is selected
- **THEN** only routines from that category are visible
- **AND** the filter summary communicates the selected category

### Requirement: Routine list items are scannable
The Routine route SHALL display routine items with enough context to compare routines at a glance.

#### Scenario: Routine item displayed
- **WHEN** a routine item is visible in the list
- **THEN** the item displays the routine title, description, category, schedule, streak, and active status context

### Requirement: Create routine action remains available
The Routine route SHALL provide a prominent action to create a routine.

#### Scenario: User starts creating a routine
- **WHEN** the user activates the create routine action
- **THEN** the app navigates to the Create routine route using the existing callback
