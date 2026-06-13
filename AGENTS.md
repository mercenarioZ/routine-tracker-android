# Agent Workflow

This project is a single-module Android app written in Kotlin with Jetpack Compose, Hilt, Navigation Compose, DataStore, Retrofit, OkHttp, kotlinx.serialization, and Gradle Kotlin DSL.

Use this file as the shared operating contract for Codex and any helper agents working in this repository.

## Default Flow

For non-trivial work, use OpenSpec as the coordination layer:

1. Start with `/opsx:explore` when the goal, architecture, or acceptance criteria are unclear.
2. Use `/opsx:propose <change-id>` for feature work, behavior changes, refactors, or bug fixes with user-visible impact.
3. Use `/opsx:apply <change-id>` to implement the approved tasks.
4. Use `/opsx:sync <change-id>` if implementation diverges from the proposal, design, or tasks.
5. Use `/opsx:archive <change-id>` after implementation and verification are complete.

Small mechanical edits may skip OpenSpec, but they must still follow the project architecture and verification rules below.

Please don't use Graphiti for this project!

## Multi-Agent Roles

Use one coordinator agent and only the specialist agents needed for the task. Keep ownership boundaries narrow so agents can work in parallel without editing the same files.

- **Coordinator Agent**
  - Owns requirements, OpenSpec changes, task decomposition, final integration, and final user response.
  - Creates or updates `openspec/changes/<change-id>/` artifacts before implementation when needed.
  - Assigns file ownership to specialist agents before parallel work begins.
  - Resolves conflicts and verifies the final build.

- **Android UI Agent**
  - Owns Compose screens, routes, UI state rendering, previews, theme usage, and navigation wiring.
  - Primary paths: `app/src/main/java/com/nai/routinetracker/ui/**`.
  - Must keep composables stateless where practical and move business logic into ViewModels.

- **Domain and Data Agent**
  - Owns repositories, DTO mapping, Retrofit APIs, session persistence, Hilt bindings, and model changes.
  - Primary paths: `app/src/main/java/com/nai/routinetracker/domain/**`, `app/src/main/java/com/nai/routinetracker/data/**`, `app/src/main/java/com/nai/routinetracker/di/**`, and `app/src/main/java/com/nai/routinetracker/model/**`.
  - Must keep domain interfaces stable unless the OpenSpec change explicitly calls for an API change.

- **Testing Agent**
  - Owns unit tests, instrumentation tests, test doubles, and regression coverage.
  - Primary paths: `app/src/test/**` and `app/src/androidTest/**`.
  - Adds focused tests for changed behavior instead of broad snapshot-style coverage.

- **Review Agent**
  - Reviews for correctness, architecture drift, lifecycle issues, coroutine/Flow mistakes, Hilt wiring, and missing tests.
  - Does not make broad refactors during review unless the coordinator explicitly assigns them.
  - Reports findings with file and line references, ordered by severity.

## Handoff Rules

Before starting parallel work, the coordinator must write down:

- The active OpenSpec change ID, if any.
- The user-visible behavior being changed.
- The files or directories each agent owns.
- The verification commands required before final response.

Agents must not edit files outside their assigned ownership without checking back with the coordinator. If two agents need the same file, the coordinator serializes that work.

Each specialist handoff should include:

- Files changed.
- Behavior implemented.
- Tests or checks run.
- Remaining risks, unknowns, or follow-up tasks.

## Architecture Rules

- Keep package structure aligned with the existing app: `ui`, `domain`, `data`, `di`, and `model`.
- UI code should call ViewModels, not repositories or Retrofit services directly.
- ViewModels should expose immutable `StateFlow`/`SharedFlow` surfaces and keep mutable flows private.
- Repository interfaces live under `domain/repository`; concrete implementations live under `data/repository`.
- Hilt bindings belong in `di` modules. Prefer constructor injection for classes.
- Network DTOs belong under `data/remote/dto`; convert DTOs to domain models before exposing them to UI.
- Keep blocking or network work off the main thread. Use repository-level dispatching when needed.
- Prefer Kotlin idioms already present in the project over introducing new libraries or architecture layers.

## Compose Rules

- Keep screen routes responsible for ViewModel collection, event handling, and navigation callbacks.
- Keep screen composables focused on rendering state and emitting UI events.
- Use Material 3 components and the existing theme.
- Use `collectAsStateWithLifecycle` for collecting ViewModel flows in composables.
- Avoid embedding long-lived business state directly in composables.
- Add string resources for user-visible text when the surrounding file already uses resources or when text is shared.

## Data and Network Rules

- Preserve auth/session flow through `AuthSessionStore`.
- Pass authorization headers from the active session when calling authenticated APIs.
- Keep API response parsing and error handling out of UI code.
- Do not hardcode environment-specific backend values in UI classes.
- If backend support is missing, make that explicit in repository behavior or OpenSpec tasks instead of silently faking success.

## Verification

Run the narrowest useful checks while developing, then run broader checks before final handoff.

Preferred commands:

```bash
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew assembleDebug
openspec validate --all
```

Use instrumentation tests only when behavior depends on Android framework/UI runtime:

```bash
./gradlew connectedDebugAndroidTest
```

If a command cannot be run, report why and name the residual risk.

## Git and Safety

- Do not revert user changes unless the user explicitly asks.
- Keep changes scoped to the assigned task and ownership boundary.
- Do not commit, rebase, push, or create branches unless the user asks.
- Follow `docs/git-conventions.md` for branch names, commit messages, PR descriptions, review expectations, and merge policy.
- Treat `local.properties`, generated build outputs, and machine-specific IDE files as local environment details.
- Do not edit generated files under `app/build/`.

## Final Response Contract

When work is complete, the coordinator reports:

- What changed.
- Which checks were run and their results.
- Any checks not run.
- Any follow-up work that is genuinely relevant.
