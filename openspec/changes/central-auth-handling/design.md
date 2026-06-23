## Context

The app now uses remote repositories for auth, routines, and tasks. Protected repository calls manually read `AuthSessionStore`, pass `Authorization` headers into Retrofit services, and allow `ApiException(401)` to surface through individual flows or ViewModel guards.

This creates duplicated auth plumbing and inconsistent behavior when an access token is expired, revoked, or otherwise invalid. The backend already treats `401 Unauthorized` as the stable signal for invalid authentication.

## Goals / Non-Goals

**Goals:**

- Attach the active access token to protected API calls from one central network layer.
- Clear the saved session when any protected API response returns `401`.
- Make navigation return to Login when the saved session is cleared.
- Reduce repeated auth-header handling in `RemoteRoutineRepository` and `RemoteTaskRepository`.
- Keep API response parsing and DTO mapping behavior intact.

**Non-Goals:**

- Implement refresh token rotation in this change.
- Redesign login/register UX.
- Change backend API routes or response envelopes.
- Remove local/fake repositories.
- Add global user-visible error rendering for every network failure.

## Decisions

### Use an OkHttp interceptor for auth headers and 401 handling

Protected calls already go through the shared `OkHttpClient` in `NetworkModule`. An interceptor gives one place to add `Authorization` and observe `401` responses.

Alternative considered: keep handling auth in each repository. That is simpler locally but keeps duplication and makes future endpoints easy to wire incorrectly.

### Keep auth endpoints unauthenticated

The interceptor should not require a session for auth endpoints. If no session is available, it should allow the request without an `Authorization` header.

Alternative considered: separate authenticated and unauthenticated Retrofit clients. That is cleaner for strict separation but adds more DI surface than this app currently needs.

### Clear session on 401, do not refresh yet

The backend supports refresh tokens, but adding refresh safely requires serialized refresh requests and retry behavior. This change should make expired sessions safe and predictable first.

Alternative considered: implement refresh immediately. That increases scope and risk because refresh token rotation can invalidate concurrent refresh attempts.

### Let existing auth state drive navigation

`AuthViewModel` already observes `AuthSessionStore`. `AppNavHost` should react when `isLoggedIn` becomes false and navigate to Login with the authenticated back stack cleared.

Alternative considered: repositories or interceptors directly triggering navigation. That would couple data/network code to UI concerns.

## Risks / Trade-offs

- Interceptors are synchronous while `AuthSessionStore` is suspend/Flow-based -> use a small blocking bridge only for reading or clearing the session.
- Clearing the session on every 401 may log the user out for transient backend auth bugs -> acceptable because 401 is the backend contract for invalid auth.
- Without refresh support, valid users with expired access tokens must log in again -> documented as a known limitation for a future change.
- If multiple requests receive 401 concurrently, session clearing may run more than once -> safe because clearing DataStore is idempotent.

## Migration Plan

1. Add a central auth interceptor or equivalent network component.
2. Register it in `NetworkModule`.
3. Remove manual authorization-header parameters from protected Retrofit services and API wrappers.
4. Update remote repositories to call protected APIs without direct session/header plumbing.
5. Update navigation to react to session loss.
6. Verify login, protected fetch, unauthorized redirect, logout, and build/test/lint checks.
