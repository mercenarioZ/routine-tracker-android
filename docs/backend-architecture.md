# Routine Tracker Backend Architecture

This document is a source-of-truth handoff for agents or clients that need to integrate with the Routine Tracker backend. It is written with a Kotlin Android client in mind.

## Runtime Summary

- Language/runtime: Java 21
- Framework: Spring Boot 4
- API style: REST JSON
- Auth: stateless JWT access tokens plus persisted refresh tokens
- Persistence: PostgreSQL through Spring Data JPA
- Migrations: Flyway SQL migrations in `src/main/resources/db/migration`
- Default server port: `8000` from `application-postgres.properties`
- Base API path: `/v1/api`
- OpenAPI/Swagger: `/swagger-ui.html`, `/api-docs`

## Layered Architecture

The backend follows a hexagonal/layered structure.

```text
presentation
  controllers, request DTOs, ApiResponse wrapper, exception mapping

application
  service interfaces, use-case implementations, response DTOs, message keys

domain
  pure domain models, repository ports, business errors/exceptions

infrastructure
  Spring security, persistence adapters, JPA entities, mappers, Spring Data repositories
```

Dependency direction is inward:

```text
presentation -> application -> domain
infrastructure -> domain/application ports
```

Controllers should stay thin. Business rules live in `application/service/impl` and domain objects. JPA entities stay in `infrastructure/persistence/entity` and must not leak into API responses.

## Request Flow

Typical protected request:

```text
Android client
  -> Authorization: Bearer <accessToken>
  -> JwtAuthenticationFilter
  -> JwtService validates signature, expiration, and token type = access
  -> CustomUserDetailsService loads the user by email
  -> AuthPrincipal is placed in SecurityContext
  -> Controller reads principal.getUserId()
  -> Application service enforces ownership by userId
  -> Repository adapter maps domain <-> JPA entity
  -> Controller returns ApiResponse<T>
```

All `/v1/api/**` endpoints require authentication except:

- `/v1/api/auth/**`
- `/v1/api/health`
- Swagger/OpenAPI endpoints

`GET /v1/api/users` additionally requires `ROLE_ADMIN`.

## API Response Envelope

Every normal success and mapped error response uses:

```json
{
  "success": true,
  "status": 200,
  "messageKey": "success.task.listed",
  "message": "Get tasks successfully",
  "data": {},
  "errors": null,
  "timestamp": 1710000000000,
  "path": "/v1/api/tasks"
}
```

Fields with `null` are omitted by Jackson.

Android should model this as a generic wrapper:

```kotlin
data class ApiResponse<T>(
    val success: Boolean,
    val status: Int,
    val messageKey: String?,
    val message: String?,
    val data: T?,
    val errors: Map<String, List<String>>?,
    val timestamp: Long,
    val path: String?
)
```

Validation errors return `errors` keyed by request field:

```json
{
  "success": false,
  "status": 400,
  "messageKey": "error.validation.failed",
  "message": "Validation failed",
  "errors": {
    "email": ["Email is required"]
  },
  "timestamp": 1710000000000,
  "path": "/v1/api/auth/register"
}
```

## Authentication

### Register

```http
POST /v1/api/auth/register
Content-Type: application/json
```

Request:

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

Validation:

- `email`: required, valid email
- `password`: required, min 8 characters

Response data:

```json
{
  "accessToken": "...",
  "tokenType": "Bearer",
  "refreshToken": "..."
}
```

### Login

```http
POST /v1/api/auth/login
Content-Type: application/json
```

Request:

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

Response data is the same as register.

### Refresh

```http
POST /v1/api/auth/refresh
Content-Type: application/json
```

Request:

```json
{
  "refreshToken": "..."
}
```

Behavior:

- Refresh token must be a JWT with claim `type = refresh`.
- Refresh token must exist in the `refresh_tokens` table.
- Refresh token must not be revoked.
- On register, login, or refresh, all existing active refresh tokens for that user are revoked and a new access/refresh token pair is issued.

Android implication: store only the latest refresh token. If concurrent refresh calls happen, one may revoke another token. Serialize refresh requests in the client.

## Routines API

All routines are scoped to the authenticated user. The client does not send `userId`; the backend gets it from the JWT principal.

### Create Routine

```http
POST /v1/api/routines
Authorization: Bearer <accessToken>
Content-Type: application/json
```

Request:

```json
{
  "title": "Morning workout",
  "description": "Pushups and stretching",
  "frequency": "DAILY",
  "startDate": "2026-06-15"
}
```

Validation:

- `title`: required, max 255 characters
- `frequency`: required, one of `DAILY`, `WEEKLY`, `CUSTOM`
- `description`: optional
- `startDate`: optional ISO date, format `yyyy-MM-dd`

Behavior:

- Creates an active routine.
- Also creates one initial task for the routine.
- The initial task date is `startDate`, or the server's current date if `startDate` is absent.

Response data:

```json
{
  "id": "uuid",
  "userId": "uuid",
  "title": "Morning workout",
  "description": "Pushups and stretching",
  "frequency": "DAILY",
  "startDate": "2026-06-15",
  "active": true,
  "streakCount": 0,
  "createdAt": "2026-06-15T04:30:00Z"
}
```

### List Routines

```http
GET /v1/api/routines?search=workout&fromDate=2026-06-01&toDate=2026-06-30&isActive=true&sortBy=created_at&sortDirection=desc
Authorization: Bearer <accessToken>
```

Query parameters:

- `search`: optional; matches title or description, case-insensitive
- `fromDate`: optional ISO date; filters by `startDate >= fromDate`
- `toDate`: optional ISO date; filters by `startDate <= toDate`
- `isActive`: optional boolean
- `sortBy`: optional; accepted values are `created_at`, `title`, `start_date`; default `created_at`
- `sortDirection`: optional; `asc` or `desc`; default `desc`

Response data: array of routine responses.

### Update Routine

```http
PATCH /v1/api/routines/{routineId}
Authorization: Bearer <accessToken>
Content-Type: application/json
```

Request fields are optional:

```json
{
  "title": "Updated title",
  "description": "Updated description",
  "frequency": "WEEKLY",
  "startDate": "2026-06-20"
}
```

Rules:

- At least one field must be present.
- If `title` is present, it cannot be blank and must be at most 255 characters.
- The routine must belong to the authenticated user.

Response data: routine response.

### Delete Routine

```http
DELETE /v1/api/routines/{routineId}
Authorization: Bearer <accessToken>
```

Behavior:

- Soft-deletes the routine by setting `active = false`.
- Does not delete existing tasks.

Response data is `null` or omitted.

## Tasks API

Tasks are scoped to the authenticated user.

### Create Task

```http
POST /v1/api/tasks
Authorization: Bearer <accessToken>
Content-Type: application/json
```

Request:

```json
{
  "title": "Buy groceries",
  "taskType": "MANUAL",
  "scheduledDate": "2026-06-15",
  "routineId": null
}
```

Validation:

- `title`: required, max 255 characters
- `taskType`: required, one of `ROUTINE`, `MANUAL`
- `scheduledDate`: optional ISO date, defaults to server's current date
- `routineId`: required when `taskType = ROUTINE`; optional otherwise

If `routineId` is present, the routine must belong to the authenticated user.

Response data:

```json
{
  "id": "uuid",
  "userId": "uuid",
  "routineId": null,
  "title": "Buy groceries",
  "taskType": "MANUAL",
  "scheduledDate": "2026-06-15",
  "completed": false,
  "completedAt": null,
  "createdAt": "2026-06-15T04:30:00Z",
  "updatedAt": "2026-06-15T04:30:00Z"
}
```

### List Tasks

```http
GET /v1/api/tasks?from=2026-06-01&to=2026-06-30
Authorization: Bearer <accessToken>
```

Query parameters:

- `from`: optional ISO date; filters `scheduledDate >= from`
- `to`: optional ISO date; filters `scheduledDate <= to`

Rules:

- If both dates are absent, returns all tasks for the user.
- If both dates are present, `from` must be before or equal to `to`.

Response data: array of task responses.

### Update Task Completion

```http
PATCH /v1/api/tasks/{taskId}/completion
Authorization: Bearer <accessToken>
Content-Type: application/json
```

Request:

```json
{
  "completed": true
}
```

Rules:

- `completed` is required.
- Setting `completed = true` sets `completedAt` to current server time.
- Setting `completed = false` clears `completedAt`.
- The task must belong to the authenticated user.

Response data: task response.

## Users API

```http
GET /v1/api/users
Authorization: Bearer <adminAccessToken>
```

Requires `ROLE_ADMIN`.

Response data:

```json
[
  {
    "id": "uuid",
    "email": "admin@example.com",
    "role": "ADMIN"
  }
]
```

Roles:

- `USER`
- `ADMIN`

## Domain Rules

- Users register with role `USER`.
- Passwords are hashed with BCrypt.
- Access JWTs contain claim `type = access`.
- Refresh JWTs contain claim `type = refresh`.
- Only access tokens authenticate protected endpoints.
- Routine ownership is checked by `(routineId, userId)`.
- Task ownership is checked by `(taskId, userId)`.
- Creating a routine creates an initial `ROUTINE` task.
- Routine streak counts are calculated from routine tasks up to server `LocalDate.now()`.
- Daily streak expects consecutive previous calendar days.
- Weekly streak expects consecutive previous weeks.
- `CUSTOM` routines do not advance to a previous expected date in the current implementation, so streak behavior is limited.

## Data Model

### users

```text
id uuid primary key
email varchar(255) unique not null
password varchar(255) not null
role varchar(20) not null
```

### refresh_tokens

```text
id uuid primary key
user_id uuid not null references users(id) on delete cascade
token varchar(512) unique not null
expires_at timestamp not null
created_at timestamp not null default now()
revoked boolean not null default false
```

### routines

```text
id uuid primary key
user_id uuid not null references users(id) on delete cascade
title varchar(255) not null
description text
frequency varchar(50) not null check in DAILY, WEEKLY, CUSTOM
start_date date
is_active boolean not null default true
created_at timestamp not null default now()
updated_at timestamp not null default now()
custom_rule jsonb
```

Note: `custom_rule` exists in the migration but is not currently exposed in domain models or APIs.

### tasks

```text
id uuid primary key
user_id uuid not null references users(id) on delete cascade
routine_id uuid references routines(id) on delete set null
title varchar(255) not null
task_type varchar(20) not null check in ROUTINE, MANUAL
scheduled_date date not null
completed boolean not null default false
completed_at timestamptz
created_at timestamptz not null default now()
updated_at timestamptz not null default now()
```

Indexes:

- `idx_tasks_user_date(user_id, scheduled_date)`
- `idx_tasks_type_date(task_type, scheduled_date)`
- `idx_tasks_routine_date(routine_id, scheduled_date)`

## Error Handling

Mapped errors use the same response envelope with `success = false`.

Important message keys:

- `error.bad.credentials`
- `error.unauthorized`
- `error.forbidden`
- `error.validation.failed`
- `error.user.already.exists`
- `error.refresh.token.invalid`
- `error.routine.not.found`
- `error.routine.title.blank`
- `error.routine.update.empty`
- `error.task.routine.id.required`
- `error.task.not.found`
- `error.task.range.invalid`
- `error.task.completed.required`

HTTP status behavior:

- Validation, malformed body, most business exceptions: `400`
- Bad credentials: `401`
- Access denied: `403`
- Unknown route/resource: `404`
- Unexpected server error: `500`

## Android Client Notes

- Use base URL `http://10.0.2.2:8000/` from the Android emulator when backend runs on the host machine.
- Use base URL `http://<host-lan-ip>:8000/` from a physical device on the same network.
- Add `Authorization: Bearer <accessToken>` for all non-auth API calls.
- Parse dates as:
  - `LocalDate` strings: `yyyy-MM-dd`
  - `Instant` strings: ISO-8601 timestamps such as `2026-06-15T04:30:00Z`
- Model UUIDs as `String` in API DTOs unless the app already has a UUID type strategy.
- Keep API DTOs separate from UI/domain models in Android.
- Treat `messageKey` as stable for app logic and localization; treat `message` as display text from the backend.
- On `401`, attempt a single serialized refresh request. If refresh fails, clear tokens and send the user to login.
- Because refresh rotates tokens, protect refresh with a mutex/single-flight mechanism.
- The backend currently has no pagination for routines or tasks; list screens should handle full arrays.

## Suggested Kotlin DTO Sketch

```kotlin
data class AuthRequestDto(
    val email: String,
    val password: String
)

data class RegisterRequestDto(
    val email: String,
    val password: String
)

data class RefreshRequestDto(
    val refreshToken: String
)

data class AuthResponseDto(
    val accessToken: String,
    val tokenType: String,
    val refreshToken: String
)

enum class RoutineFrequencyDto {
    DAILY,
    WEEKLY,
    CUSTOM
}

data class CreateRoutineRequestDto(
    val title: String,
    val description: String?,
    val frequency: RoutineFrequencyDto,
    val startDate: String?
)

data class UpdateRoutineRequestDto(
    val title: String? = null,
    val description: String? = null,
    val frequency: RoutineFrequencyDto? = null,
    val startDate: String? = null
)

data class RoutineResponseDto(
    val id: String,
    val userId: String,
    val title: String,
    val description: String?,
    val frequency: RoutineFrequencyDto,
    val startDate: String?,
    val active: Boolean,
    val streakCount: Int,
    val createdAt: String
)

enum class TaskTypeDto {
    ROUTINE,
    MANUAL
}

data class CreateTaskRequestDto(
    val title: String,
    val taskType: TaskTypeDto,
    val scheduledDate: String?,
    val routineId: String?
)

data class UpdateTaskCompletionRequestDto(
    val completed: Boolean
)

data class TaskResponseDto(
    val id: String,
    val userId: String,
    val routineId: String?,
    val title: String,
    val taskType: TaskTypeDto,
    val scheduledDate: String,
    val completed: Boolean,
    val completedAt: String?,
    val createdAt: String,
    val updatedAt: String
)
```

## Suggested Client Module Boundaries

For a Kotlin Android app, a clean matching shape would be:

```text
data/remote
  RoutineTrackerApi
  AuthInterceptor
  TokenAuthenticator
  dto/*

data/repository
  AuthRepositoryImpl
  RoutineRepositoryImpl
  TaskRepositoryImpl

domain/model
  UserSession
  Routine
  Task

domain/repository
  AuthRepository
  RoutineRepository
  TaskRepository

domain/usecase
  LoginUseCase
  RefreshSessionUseCase
  GetRoutinesUseCase
  CreateRoutineUseCase
  GetTasksUseCase
  UpdateTaskCompletionUseCase

presentation
  screens/viewmodels/state
```

Keep token storage in an encrypted/local secure storage abstraction. Keep network DTO parsing and token refresh behavior out of ViewModels.
