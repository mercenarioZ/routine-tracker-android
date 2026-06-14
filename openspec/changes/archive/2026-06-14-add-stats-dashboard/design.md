## Context

Stats is already registered as a navigation destination, but its screen is placeholder content and it is not part of the primary bottom navigation. Routine and task data are already available through `RoutineRepository.observeDashboard()` and `TaskRepository.observeTasks()`, so the dashboard can be built without new persistence, backend, or schema work.

## Goals / Non-Goals

**Goals:**
- Render a useful Stats dashboard with pie-style and column-style visual summaries.
- Reuse existing routine/task repositories and category color conventions.
- Make Stats reachable from the bottom navigation.
- Preserve loading and empty states when data is not available.

**Non-Goals:**
- Add historical analytics or long-term trend storage.
- Add a charting library or new external dependency.
- Change routine/task repository contracts or database schema.

## Decisions

- Add a `StatsViewModel` and `StatsUiState` under `ui/stats`.
  - Rationale: Stats needs derived UI state from both routine and task flows. Keeping that derivation in a ViewModel matches the existing Home/Tasks/Routines structure.
  - Alternative considered: Reuse `HomeViewModel`; rejected because Stats has distinct derived metrics and visual grouping.

- Draw charts with Compose Canvas and layout primitives.
  - Rationale: Current needs are simple pie and column views; custom drawing avoids a dependency and keeps visual styling aligned with the app theme.
  - Alternative considered: Add a chart library; rejected as unnecessary for static summary charts.

- Derive category and status summaries from current in-memory lists.
  - Rationale: The app currently tracks today's routine/task data, not persisted analytics history.
  - Alternative considered: Add historical aggregation; rejected because it requires new data model and product decisions outside this request.

## Risks / Trade-offs

- Current stats represent the loaded routine/task snapshot, not historical trends -> Label copy and visuals will focus on current progress and routine mix.
- Small data sets can make charts look sparse -> Include metric cards and empty states alongside charts.
- Bottom navigation gains another item -> Use the existing Stats icon/label and keep destinations concise.
