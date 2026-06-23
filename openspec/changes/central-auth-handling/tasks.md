## 1. Central Network Auth

- [ ] 1.1 Add an OkHttp auth interceptor that reads `AuthSessionStore` and attaches `Authorization: Bearer <accessToken>` when a session exists.
- [ ] 1.2 In the interceptor, clear `AuthSessionStore` when a protected response returns HTTP `401`.
- [ ] 1.3 Register the auth interceptor in `NetworkModule` before creating Retrofit services.

## 2. Protected API Cleanup

- [ ] 2.1 Remove explicit `@Header("Authorization")` parameters from protected routine Retrofit service methods and their `RoutineApi` wrapper calls.
- [ ] 2.2 Remove explicit `@Header("Authorization")` parameters from protected task Retrofit service methods and their `TaskApi` wrapper calls.
- [ ] 2.3 Update `RemoteRoutineRepository` to stop reading `AuthSessionStore` only to pass authorization headers.
- [ ] 2.4 Update `RemoteTaskRepository` to stop reading `AuthSessionStore` only to pass authorization headers.

## 3. Session Loss Navigation

- [ ] 3.1 Update `AppNavHost` to navigate to Login when observed auth state becomes logged out after initial loading.
- [ ] 3.2 Ensure Login is not pushed repeatedly when the current destination is already Login.
- [ ] 3.3 Ensure authenticated destinations are removed from the back stack after session invalidation.

## 4. Regression Coverage

- [ ] 4.1 Add focused unit tests for auth-header attachment when a saved session exists.
- [ ] 4.2 Add focused unit tests for no auth header when no saved session exists.
- [ ] 4.3 Add focused unit tests for `401` clearing the saved session.
- [ ] 4.4 Add or update navigation/ViewModel tests covering session loss returning to Login where practical.

## 5. Verification

- [ ] 5.1 Run `./gradlew testDebugUnitTest`.
- [ ] 5.2 Run `./gradlew lintDebug`.
- [ ] 5.3 Run `./gradlew assembleDebug`.
- [ ] 5.4 Run `openspec validate --all`.
