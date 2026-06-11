package com.nai.routinetracker.data.remote.dto

import com.nai.routinetracker.model.RoutineCategories
import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineItem
import java.util.Locale
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

@Serializable
data class RoutineDto(
    val id: String?,
    val title: String?,
    val scheduleLabel: String?,
    val category: RoutineCategoryDto?,
    val streakDays: Int?,
    val isActive: Boolean?,
    val description: String?
) {
    fun toDomain(): RoutineItem {
        val safeTitle = title?.takeIf { it.isNotBlank() } ?: "Untitled routine"
        return RoutineItem(
            id = id?.takeIf { it.isNotBlank() } ?: safeTitle.toStableId(),
            title = safeTitle,
            scheduleLabel = scheduleLabel?.takeIf { it.isNotBlank() } ?: "Anytime",
            category = category?.toDomain() ?: RoutineCategory(
                id = "routine",
                label = "Routine",
                isSystem = false
            ),
            streakDays = streakDays ?: 0,
            isActive = isActive ?: true,
            description = description.orEmpty()
        )
    }

    companion object {
        fun listFromJsonValue(value: JsonElement?): List<RoutineDto> {
            return when (value) {
                is JsonArray -> value.toRoutineDtos()
                is JsonObject -> value.extractRoutineArray().toRoutineDtos()
                else -> emptyList()
            }
        }

        fun fromJson(json: JsonObject): RoutineDto {
            return RoutineDto(
                id = json.firstStringOrNull("id", "_id", "routineId"),
                title = json.firstStringOrNull("title", "name"),
                scheduleLabel = json.firstStringOrNull(
                    "scheduleLabel",
                    "timeLabel",
                    "schedule",
                    "time",
                    "startTime",
                    "reminderTime"
                ) ?: buildScheduleLabel(
                    frequency = json.firstStringOrNull("frequency"),
                    startDate = json.firstStringOrNull("startDate")
                ),
                category = RoutineCategoryDto.fromJsonValue(
                    json.elementOrNull("category")
                        ?: json.firstStringOrNull("categoryName", "categoryLabel", "categoryId")
                            ?.let(::JsonPrimitive)
                ),
                streakDays = json.firstIntOrNull(
                    "streakDays",
                    "streak",
                    "currentStreak",
                    "streakCount"
                ),
                isActive = json.firstBooleanOrNull("isActive", "active"),
                description = json.firstStringOrNull("description", "note")
            )
        }
    }
}

@Serializable
data class RoutineCategoryDto(
    val id: String?,
    val label: String?
) {
    fun toDomain(): RoutineCategory {
        val safeLabel = label?.takeIf { it.isNotBlank() }
            ?: id?.takeIf { it.isNotBlank() }
            ?: "Routine"
        val safeId = id?.takeIf { it.isNotBlank() } ?: safeLabel.toStableId()
        return RoutineCategories.defaults.firstOrNull { it.id == safeId }
            ?: RoutineCategory(
                id = safeId,
                label = safeLabel,
                isSystem = false
            )
    }

    companion object {
        fun fromJsonValue(value: JsonElement?): RoutineCategoryDto? {
            return when (value) {
                null, is JsonNull -> null
                is JsonObject -> RoutineCategoryDto(
                    id = value.firstStringOrNull("id", "categoryId", "code"),
                    label = value.firstStringOrNull("label", "name", "title")
                )
                is JsonPrimitive -> RoutineCategoryDto(
                    id = value.contentOrNull?.toStableId(),
                    label = value.contentOrNull
                )
                else -> RoutineCategoryDto(
                    id = value.toString().toStableId(),
                    label = value.toString()
                )
            }
        }
    }
}

private fun JsonObject.extractRoutineArray(): JsonArray {
    return firstArrayOrNull("content", "items", "routines", "results", "data") ?: JsonArray(emptyList())
}

private fun JsonArray.toRoutineDtos(): List<RoutineDto> {
    return buildList {
        this@toRoutineDtos.forEach { item ->
            item.jsonObjectOrNull()?.let { add(RoutineDto.fromJson(it)) }
        }
    }
}

private fun JsonObject.firstArrayOrNull(vararg names: String): JsonArray? {
    return names.firstNotNullOfOrNull { name ->
        elementOrNull(name).jsonArrayOrNull()
    }
}

private fun buildScheduleLabel(
    frequency: String?,
    startDate: String?
): String? {
    return listOfNotNull(
        frequency?.replace('_', ' '),
        startDate?.let { "from $it" }
    ).joinToString(separator = " • ")
        .ifBlank { null }
}

private fun String.toStableId(): String {
    return lowercase(Locale.US)
        .replace(Regex("[^a-z0-9]+"), "-")
        .trim('-')
        .ifBlank { "routine" }
}
