## Context

The app is a single-module Jetpack Compose Android app with Navigation Compose and Material 3. Primary navigation is defined in `AppDestination.kt` and rendered by `AppNavHost.kt`; Routine state is already supplied through `RoutineViewModel` as immutable UI state and rendered by `RoutinesScreen`.

The current bottom bar contains five destinations: Home, Routines, Tasks, Stats, and Settings. Stats is a placeholder route, while Home, Routines, Tasks, and Settings map to the app's current useful workflows. The Routine route already supports loading, category filtering, and creation navigation, but the screen reads as a simple list instead of a focused routine management surface.

## Goals / Non-Goals

**Goals:**
- Make the bottom bar a four-destination primary navigation surface: Home, Routines, Tasks, Settings.
- Improve the Routine route with stronger hierarchy, at-a-glance metrics, category insight, filter context, and polished routine list presentation.
- Keep Routine state collection in the route and rendering logic in composables.
- Preserve existing create-routine navigation and category filtering behavior.

**Non-Goals:**
- Remove the Stats route or its placeholder screen from the navigation graph.
- Add routine completion, editing, deleting, or backend changes.
- Introduce new dependencies or architectural layers.

## Decisions

- Keep Stats out of `bottomBarDestinations` but leave its `AppDestination` and NavHost entry intact.
  - Rationale: this simplifies the app's primary navigation without deleting future expansion points.
  - Alternative considered: keep five tabs. Rejected because the fifth tab is not yet a meaningful workflow and competes with the more important Routine and Task destinations.

- Redesign `RoutinesScreen` using local composable sections rather than adding new state models.
  - Rationale: all required values can be derived from `RoutinesUiState` and `RoutineItem`, keeping the change scoped to UI.
  - Alternative considered: add a dedicated dashboard model. Rejected because no domain behavior changes are needed.

- Use Material 3 surfaces, cards, filter chips, and iconography already available in the project.
  - Rationale: this stays consistent with Home and Tasks screens while improving scanability.
  - Alternative considered: introduce a custom component system. Rejected as unnecessary for this narrow UI upgrade.

- Show summary metrics derived from visible and total routines.
  - Rationale: users need immediate answers to "how many active routines do I have?", "what is my strongest streak?", and "which filter am I viewing?" without leaving the route.

## Risks / Trade-offs

- Derived metrics can overstate progress if backend data becomes stale -> keep labels factual and based only on loaded routine fields.
- Removing Stats from the bottom bar could hide a future analytics path -> preserve the route so it can be reintroduced when implemented.
- A richer Routine screen can become visually busy -> use compact sections, restrained cards, and the existing theme spacing.
