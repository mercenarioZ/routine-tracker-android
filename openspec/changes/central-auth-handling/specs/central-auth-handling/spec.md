## ADDED Requirements

### Requirement: Protected API calls use active session authorization
The system SHALL attach the active access token as a Bearer authorization header to protected routine and task API calls when a saved session exists.

#### Scenario: Protected request has saved session
- **WHEN** a protected routine or task API request is executed and `AuthSessionStore` contains an access token
- **THEN** the request includes `Authorization: Bearer <accessToken>`

#### Scenario: Request has no saved session
- **WHEN** a protected routine or task API request is executed and `AuthSessionStore` has no session
- **THEN** the request is sent without an authorization header and downstream handling treats any unauthorized response as session loss

### Requirement: Unauthorized responses invalidate the saved session
The system SHALL clear the saved auth session when a protected API response returns HTTP `401 Unauthorized`.

#### Scenario: Routine request returns unauthorized
- **WHEN** a protected routine API request returns HTTP `401`
- **THEN** the saved auth session is cleared

#### Scenario: Task request returns unauthorized
- **WHEN** a protected task API request returns HTTP `401`
- **THEN** the saved auth session is cleared

### Requirement: Session loss returns the user to Login
The system SHALL navigate to the Login destination and clear authenticated navigation history when the observed auth state changes from logged in to logged out.

#### Scenario: Session cleared while on authenticated screen
- **WHEN** the user is on Home, Routines, Calendar, Tasks, or Settings and the saved auth session becomes null
- **THEN** the app navigates to Login and removes authenticated destinations from the back stack

#### Scenario: User already on Login
- **WHEN** the saved auth session is null and the current destination is Login
- **THEN** the app remains on Login without adding duplicate Login destinations

### Requirement: Auth endpoints remain usable without saved session
The system SHALL allow login requests to run without requiring an existing auth session.

#### Scenario: User logs in after session expiration
- **WHEN** the saved auth session has been cleared and the user submits valid login credentials
- **THEN** the login request is sent without requiring an authorization header and the returned session is saved
