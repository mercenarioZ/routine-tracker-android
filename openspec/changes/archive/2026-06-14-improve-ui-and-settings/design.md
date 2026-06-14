## Context

The app is a local-first Compose routine tracker with bottom navigation for Home, Routines, Tasks, and Settings. Home, Routines, Tasks, and create-routine are functional; Settings is a placeholder. The UI already uses Material 3, Hilt, ViewModels, Room-backed repositories, and string resources.

## Goals / Non-Goals

**Goals:**
- Make the primary app screens feel more distinctive and polished for a niche routine-tracking product.
- Keep workflows dense, scannable, and ergonomic rather than turning the app into a marketing-style landing experience.
- Implement a functional Settings route with local reminder controls, app info, reset data, and sign out.
- Preserve existing navigation callbacks, ViewModel contracts, local Room persistence, and task/routine behavior.
- Split implementation into parallel worktree-safe ownership areas.

**Non-Goals:**
- No backend API integration for settings.
- No push notification scheduling or OS permission flow in this change.
- No full account/profile management beyond local display and sign out.
- No app-wide dynamic theme persistence unless it is required for the basic settings experience.

## Decisions

- Use a refined Material 3 visual layer rather than adding new UI dependencies.
  - Rationale: the app already uses Compose Material 3 and can become more polished through color, shape, typography, spacing, and component composition.
  - Alternative considered: add a component/design-system library. Rejected because it increases dependency and review surface for visual polish.
- Keep visual polish inside existing screen/component boundaries.
  - Rationale: Home, Routines, and Tasks already have ViewModels and rendering components. Styling them in place preserves behavior and avoids cross-feature rewrites.
  - Alternative considered: rebuild the screens around a new shared dashboard shell. Rejected because it would create unnecessary conflict and risk.
- Store Settings preferences locally in a small DataStore-backed repository.
  - Rationale: reminder toggles/time are app-local preferences and do not belong in Room routine/task tables.
  - Alternative considered: remember-only Compose state. Rejected because settings would reset on process death.
- Implement data reset as a data-layer operation that clears local Room tables and reseeds the local sample data.
  - Rationale: the reset action is meaningful only if it affects the persisted local routine/task state.
  - Alternative considered: navigate away or clear UI state only. Rejected because it would not match user expectations.
- Route Settings sign out through the existing auth session clearing path.
  - Rationale: `AuthViewModel.logout()` already owns session logout and navigation back to Login.
  - Alternative considered: duplicate session clearing in Settings. Rejected to keep auth behavior centralized.

## Risks / Trade-offs

- [Risk] UI polish and Settings both touch shared resources like strings/theme. -> Mitigation: assign disjoint primary ownership and let the coordinator integrate shared resource edits.
- [Risk] Data reset can surprise users. -> Mitigation: require a confirmation affordance before clearing local data.
- [Risk] Reminder preferences could imply real notifications. -> Mitigation: label them as reminder settings only and exclude OS notification scheduling from this change.
- [Risk] Parallel workers can conflict in navigation and strings. -> Mitigation: UI worker avoids Settings files; Settings worker avoids Home/Routines/Tasks visual files except minimal navigation callback wiring.
