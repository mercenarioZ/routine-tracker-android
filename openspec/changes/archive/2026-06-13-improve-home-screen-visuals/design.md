## Context

The existing Home screen has the correct data but uses similar text-heavy cards for the header, overview, metrics, routines, and tasks. This makes the screen feel busy and slows down scanning.

## Goals / Non-Goals

**Goals:**
- Make the Home screen visually clearer with icon-led hierarchy and compact sections.
- Keep the dashboard useful at a glance: greeting, next routine, active routines, streaks, and today's tasks.
- Preserve current state inputs, navigation behavior, and task toggle behavior.

**Non-Goals:**
- No backend, repository, Hilt, or navigation route changes.
- No new third-party dependencies.
- No changes to task or routine domain models.

## Decisions

- Use existing Material icon dependencies instead of adding image assets or new libraries.
- Keep the Home redesign inside existing Home components so the change remains easy to review.
- Use concise labels and max-line truncation to reduce text density while preserving important information.
- Keep card radii within the project's instruction guidance by using 8.dp cards and icon containers for emphasis.

## Risks / Trade-offs

- Visual polish is subjective -> keep behavior unchanged and make the layout easy to iterate.
- Text truncation could hide some routine detail -> show the most important fields first and keep full detail available in other screens.
