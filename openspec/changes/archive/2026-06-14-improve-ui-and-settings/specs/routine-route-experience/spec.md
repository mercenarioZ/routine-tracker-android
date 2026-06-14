## ADDED Requirements

### Requirement: Routine route adopts the refreshed visual direction
The Routine route SHALL adopt the refreshed routine-tracking visual direction while preserving filtering, summary, item comparison, empty-state, and create-routine behavior.

#### Scenario: User opens refreshed Routines
- **WHEN** routines are loaded
- **THEN** the route displays the same routine summary, category filtering, routine list context, and create action with improved visual polish

#### Scenario: User starts routine creation from refreshed Routines
- **WHEN** the user activates the create routine action
- **THEN** the app navigates to the Create routine route using the existing callback
