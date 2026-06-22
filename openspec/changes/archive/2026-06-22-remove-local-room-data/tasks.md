## 1. Remove Room Persistence Path

- [x] 1.1 Remove Room dependencies, compiler configuration, and database Hilt providers.
- [x] 1.2 Delete Room entities, DAOs, database, mapper, seeder, and local repository implementations.
- [x] 1.3 Remove tests that only cover the deleted Room/local repository behavior.

## 2. Remove Settings Local Data Reset

- [x] 2.1 Remove LocalDataRepository wiring and SettingsViewModel reset state/actions.
- [x] 2.2 Remove Settings UI copy, controls, dialogs, and messages for local data reset.
- [x] 2.3 Remove string resources that only support the deleted local data reset feature.

## 3. Verification

- [x] 3.1 Run focused search for stale Room/DAO/local reset references.
- [x] 3.2 Run `openspec validate --all`.
- [x] 3.3 Run the narrowest useful Gradle checks for the refactor.
