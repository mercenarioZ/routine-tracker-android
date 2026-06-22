## REMOVED Requirements

### Requirement: Fake routine creation updates local routine state
**Reason**: The Room-backed backend-independent local routine flow is being removed so routines are managed through backend repositories.
**Migration**: Use backend-backed routine creation for runtime behavior and repository fakes only in tests that require isolated ViewModel behavior.

### Requirement: Fake routine creation generates a matching task
**Reason**: Local SQLite routine creation and generated-task persistence are being removed with the Room-backed local flow.
**Migration**: Generated task behavior must come from backend-backed task data or explicit test doubles in tests.

### Requirement: Fake app flow can be tested without backend services
**Reason**: The app no longer keeps a SQLite-backed backend-independent runtime mode for routines and tasks.
**Migration**: Use backend services for end-to-end app flow testing, or use focused in-memory fakes in unit tests.

### Requirement: Local routine creation preserves recurrence
**Reason**: Local SQLite routine creation is being removed; recurrence persistence belongs to the backend routine API.
**Migration**: Verify recurrence through backend-backed DTO/repository behavior and UI state tests.

### Requirement: Seed routines include recurrence
**Reason**: Local sample-data seeding is being removed with the Room database.
**Migration**: Seed/sample routine data should come from backend fixtures or test doubles where needed.
