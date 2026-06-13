# Git Conventions

These conventions keep changes easy to review, easy to revert, and easy for agents to summarize.

## Branches

Create one branch per logical change.

Format:

```text
<type>/<short-kebab-description>
```

Use these branch types:

- `feature/` for user-visible features
- `fix/` for bug fixes
- `ui/` for visual-only UI changes
- `refactor/` for behavior-preserving code changes
- `test/` for test-only changes
- `docs/` for documentation-only changes
- `chore/` for tooling, dependency, or maintenance work

Examples:

```text
ui/improve-home-dashboard
fix/login-error-state
feature/create-routine-backend
docs/git-conventions
```

Codex-created branches should use the configured `codex/` prefix when creating a working branch:

```text
codex/ui-improve-home-dashboard
```

## Commits

Use Conventional Commits.

Format:

```text
<type>(<scope>): <imperative summary>
```

Rules:

- Use the imperative mood: `add`, `fix`, `update`, `remove`, not `added` or `fixed`.
- Keep the first line under 72 characters when practical.
- Commit one logical change at a time.
- Do not mix formatting, refactors, tests, and behavior changes unless they are inseparable.
- Explain the why in the body when the change is not obvious.

Types:

- `feat` for new behavior
- `fix` for bug fixes
- `ui` for visual or interaction polish
- `refactor` for behavior-preserving code changes
- `test` for tests
- `docs` for documentation
- `chore` for maintenance/tooling
- `build` for Gradle/build configuration
- `ci` for automation
- `revert` for reverting a previous commit

Preferred scopes for this project:

- `home`
- `routines`
- `tasks`
- `stats`
- `settings`
- `auth`
- `navigation`
- `data`
- `network`
- `di`
- `theme`
- `openspec`
- `docs`

Examples:

```text
ui(home): tighten routine metadata spacing
feat(routines): add create routine form validation
fix(auth): preserve session after app restart
docs(git): add branch and PR conventions
test(data): cover routine DTO mapping
```

Commit body template:

```text
<type>(<scope>): <summary>

Why:
- <reason for the change>

What:
- <notable implementation detail>
- <notable implementation detail>

Verification:
- <command/result>
```

Use `BREAKING CHANGE:` in the footer only when the change requires callers, users, or stored data to migrate.

## Pull Requests

Open one PR per logical change. A PR should be small enough to review in one sitting.

PR title format should match the main commit style:

```text
<type>(<scope>): <imperative summary>
```

Examples:

```text
ui(home): improve dashboard scanability
fix(network): handle empty routine responses
docs(git): add team conventions
```

## PR Description

Use this template:

```md
## Summary

- 
- 

## Why


## Changes

- 
- 

## Verification

- [ ] `openspec validate --all`
- [ ] `./gradlew testDebugUnitTest`
- [ ] `./gradlew lintDebug`
- [ ] `./gradlew assembleDebug`

## Screenshots or Recording

Before:

After:

## Risks / Notes

- 
```

Guidelines:

- Keep `Summary` user-facing and short.
- Use `Why` to explain the problem, not the implementation.
- Use `Changes` for technical details reviewers need.
- Include screenshots or a short recording for UI changes.
- If a verification command was not run, say why.

## OpenSpec and PRs

For OpenSpec-backed work:

- Create or update the OpenSpec change before implementation.
- Archive the change before opening the final PR when the implementation is accepted.
- Include the archived spec path in the PR when useful.
- Keep generated OpenSpec archive files in the same PR as the code change they document.

Example PR note:

```md
OpenSpec:
- `openspec/specs/home-dashboard-visuals/spec.md`
- `openspec/changes/archive/2026-06-13-improve-home-screen-visuals/`
```

## Review Checklist

Before requesting review:

- The branch contains one coherent change.
- The PR description is filled in.
- UI changes include screenshots or recording.
- OpenSpec changes are validated and archived when applicable.
- Relevant Gradle checks pass.
- No generated build outputs are included.
- No machine-specific files are included.

Reviewers should prioritize:

- Behavior correctness
- Architecture fit
- Lifecycle, coroutine, Flow, and Hilt issues
- Missing tests or weak verification
- Accessibility and UI scanability for visual changes

## Merge Policy

Prefer squash merge for feature branches so `main` stays readable.

Squash commit format:

```text
<type>(<scope>): <imperative summary>
```

The squash body should include:

```text
Summary:
- 

Verification:
- 
```

Do not merge with failing required checks. If checks are skipped, document the reason in the PR before merging.

## Reverts

Use an explicit revert commit instead of rewriting shared history.

Format:

```text
revert(<scope>): revert <original summary>
```

Body:

```text
Reverts: <commit-sha or PR number>

Reason:
- <why the revert is needed>
```

## Agent Rules

Agents working in this repository must:

- Inspect `git status --short` before staging, committing, or opening a PR.
- Never stage unrelated user changes.
- Mention all verification commands and results in the final response.
- Ask before committing, pushing, or opening a PR.
- Use the PR template above when asked to create a PR description.
