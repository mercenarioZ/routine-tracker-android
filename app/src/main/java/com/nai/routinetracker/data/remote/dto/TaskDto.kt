package com.nai.routinetracker.data.remote.dto

import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.TaskCategories
import com.nai.routinetracker.model.TaskCategory
import com.nai.routinetracker.model.TaskItem
import com.nai.routinetracker.model.TaskStatus
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

@Serializable
data class TaskDto(
    val id: String?,
    val routineId: String?,
    val title: String?,
    val timeLabel: String?,
    val dueLabel: String?,
    val category: TaskCategoryDto?,
    val status: String?,
    val completed: Boolean?,
    val description: String?
) {
    fun toDomain(): TaskItem {
        val safeTitle = title?.takeIf { it.isNotBlank() } ?: "Untitled task"
        return TaskItem(
            id = id?.takeIf { it.isNotBlank() } ?: safeTitle.toStableId(),
            routineId = routineId?.takeIf { it.isNotBlank() },
            title = safeTitle,
            timeLabel = timeLabel?.takeIf { it.isNotBlank() } ?: "Anytime",
            dueLabel = dueLabel?.takeIf { it.isNotBlank() }?.toDueLabel() ?: "Today",
            category = category?.toDomain() ?: TaskCategory(
                id = "task",
                label = "Task",
                isSystem = false
            ),
            status = completed?.toTaskStatus() ?: status.toTaskStatus(),
            description = description.orEmpty()
        )
    }

    companion object {
        fun listFromJsonValue(value: JsonElement?): List<TaskDto> {
            return when (value) {
                is JsonArray -> value.toTaskDtos()
                is JsonObject -> value.extractTaskArray().toTaskDtos()
                else -> emptyList()
            }
        }

        fun fromJson(json: JsonObject): TaskDto {
            return TaskDto(
                id = json.firstStringOrNull("id", "_id", "taskId"),
                routineId = json.firstStringOrNull("routineId", "routine_id"),
                title = json.firstStringOrNull("title", "name"),
                timeLabel = json.firstStringOrNull(
                    "timeLabel",
                    "scheduleLabel",
                    "time",
                    "dueTime",
                    "startTime"
                ),
                dueLabel = json.firstStringOrNull("dueLabel", "due", "dueDate", "scheduledDate", "date"),
                category = TaskCategoryDto.fromJsonValue(
                    json.elementOrNull("category")
                        ?: json.firstStringOrNull("categoryName", "categoryLabel", "categoryId")
                            ?.let(::JsonPrimitive)
                ),
                status = json.firstStringOrNull("status", "state")
                    ?: json.firstBooleanOrNull("isDone", "done", "completed")?.toStatusString(),
                completed = json.firstBooleanOrNull("completed", "done", "isDone"),
                description = json.firstStringOrNull("description", "note")
            )
        }
    }
}

@Serializable
data class TaskCategoryDto(
    val id: String?,
    val label: String?
) {
    fun toDomain(): TaskCategory {
        val safeLabel = label?.takeIf { it.isNotBlank() }
            ?: id?.takeIf { it.isNotBlank() }
            ?: "Task"
        val safeId = id?.takeIf { it.isNotBlank() } ?: safeLabel.toStableId()
        return TaskCategories.defaults.firstOrNull { it.id == safeId }
            ?: TaskCategory(
                id = safeId,
                label = safeLabel,
                isSystem = false
            )
    }

    companion object {
        fun fromJsonValue(value: JsonElement?): TaskCategoryDto? {
            return when (value) {
                null, is JsonNull -> null
                is JsonObject -> TaskCategoryDto(
                    id = value.firstStringOrNull("id", "categoryId", "code"),
                    label = value.firstStringOrNull("label", "name", "title")
                )
                is JsonPrimitive -> TaskCategoryDto(
                    id = value.contentOrNull?.toStableId(),
                    label = value.contentOrNull
                )
                else -> TaskCategoryDto(
                    id = value.toString().toStableId(),
                    label = value.toString()
                )
            }
        }
    }
}

@Serializable
data class TaskCreateRequestDto(
    val title: String,
    val taskType: String,
    val scheduledDate: String? = null,
    val routineId: String?
) {
    fun toJson(): String {
        return RemoteJson.encodeToString(this)
    }

    companion object {
        fun fromRoutine(routine: RoutineItem): TaskCreateRequestDto {
            return TaskCreateRequestDto(
                title = routine.title,
                taskType = "ROUTINE",
                routineId = routine.id
            )
        }
    }
}

@Serializable
data class TaskUpdateRequestDto(
    val completed: Boolean
) {
    fun toJson(): String {
        return RemoteJson.encodeToString(this)
    }

    companion object {
        fun fromStatus(status: TaskStatus): TaskUpdateRequestDto {
            return TaskUpdateRequestDto(completed = status == TaskStatus.Done)
        }
    }
}

private fun JsonObject.extractTaskArray(): JsonArray {
    return firstArrayOrNull("content", "items", "tasks", "results", "data") ?: JsonArray(emptyList())
}

private fun JsonArray.toTaskDtos(): List<TaskDto> {
    return buildList {
        this@toTaskDtos.forEach { item ->
            item.jsonObjectOrNull()?.let { add(TaskDto.fromJson(it)) }
        }
    }
}

private fun JsonObject.firstArrayOrNull(vararg names: String): JsonArray? {
    return names.firstNotNullOfOrNull { name ->
        elementOrNull(name).jsonArrayOrNull()
    }
}

private fun String?.toTaskStatus(): TaskStatus {
    return when (this?.trim()?.lowercase(Locale.US)) {
        "done", "complete", "completed", "true" -> TaskStatus.Done
        else -> TaskStatus.Pending
    }
}

private fun Boolean.toTaskStatus(): TaskStatus {
    return if (this) {
        TaskStatus.Done
    } else {
        TaskStatus.Pending
    }
}

private fun Boolean.toStatusString(): String {
    return if (this) {
        "done"
    } else {
        "pending"
    }
}

private fun String.toDueLabel(): String {
    val today = isoDateForOffset(days = 0)
    val tomorrow = isoDateForOffset(days = 1)
    return when (this) {
        today -> "Today"
        tomorrow -> "Tomorrow"
        else -> this
    }
}

private fun isoDateForOffset(days: Int): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_MONTH, days)
    return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time)
}

private fun String.toStableId(): String {
    return lowercase(Locale.US)
        .replace(Regex("[^a-z0-9]+"), "-")
        .trim('-')
        .ifBlank { "task" }
}
