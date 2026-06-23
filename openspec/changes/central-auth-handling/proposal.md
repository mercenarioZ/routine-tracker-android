## Why

Unauthorized API responses are currently handled inconsistently across remote repositories and ViewModels. This can leave users stuck on authenticated screens with stale tokens, empty content, or silent failures instead of returning them to login.

## What Changes

- Add centralized handling for authenticated API calls.
- Ensure protected requests attach the current access token consistently.
- Ensure backend `401 Unauthorized` responses clear the saved session.
- Make navigation react to session loss and return the user to Login.
- Keep remote repository code focused on domain operations instead of repeated auth-header/session-failure plumbing.
- Preserve existing login, logout, routine, task, and settings behavior where possible.

## Capabilities

### New Capabilities

- `central-auth-handling`: Defines consistent authenticated request behavior, unauthorized-session handling, and login redirection after session invalidation.

### Modified Capabilities

- None.

## Impact

- Affected code areas:
  - `data/remote/**`
  - `data/repository/Remote*Repository.kt`
  - `domain/session/AuthSessionStore.kt`
  - `di/NetworkModule.kt`
  - `ui/auth/AuthViewModel.kt`
  - `ui/navigation/AppNavHost.kt`
- Network behavior changes for protected routine/task endpoints.
- No backend API changes are required.
