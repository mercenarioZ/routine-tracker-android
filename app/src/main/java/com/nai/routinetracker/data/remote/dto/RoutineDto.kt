package com.nai.routinetracker.data.remote.dto

import com.nai.routinetracker.model.RoutineCategories
import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineItem
import java.util.Locale
import org.json.JSONArray
import org.json.JSONObject

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
        fun listFromJsonValue(value: Any?): List<RoutineDto> {
            return when (value) {
                is JSONArray -> value.toRoutineDtos()
                is JSONObject -> value.extractRoutineArray().toRoutineDtos()
                else -> emptyList()
            }
        }

        fun fromJson(json: JSONObject): RoutineDto {
            return RoutineDto(
                id = json.optFirstString("id", "_id", "routineId"),
                title = json.optFirstString("title", "name"),
                scheduleLabel = json.optFirstString(
                    "scheduleLabel",
                    "timeLabel",
                    "schedule",
                    "time",
                    "startTime",
                    "reminderTime"
                ) ?: buildScheduleLabel(
                    frequency = json.optFirstString("frequency"),
                    startDate = json.optFirstString("startDate")
                ),
                category = RoutineCategoryDto.fromJsonValue(
                    json.optNullable("category")
                        ?: json.optFirstString("categoryName", "categoryLabel", "categoryId")
                ),
                streakDays = json.optFirstInt(
                    "streakDays",
                    "streak",
                    "currentStreak",
                    "streakCount"
                ),
                isActive = json.optFirstBoolean("isActive", "active"),
                description = json.optFirstString("description", "note")
            )
        }
    }
}

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
        fun fromJsonValue(value: Any?): RoutineCategoryDto? {
            return when (value) {
                null, JSONObject.NULL -> null
                is JSONObject -> RoutineCategoryDto(
                    id = value.optFirstString("id", "categoryId", "code"),
                    label = value.optFirstString("label", "name", "title")
                )
                is String -> RoutineCategoryDto(
                    id = value.toStableId(),
                    label = value
                )
                else -> RoutineCategoryDto(
                    id = value.toString().toStableId(),
                    label = value.toString()
                )
            }
        }
    }
}

private fun JSONObject.extractRoutineArray(): JSONArray {
    return optFirstArray("content", "items", "routines", "results", "data") ?: JSONArray()
}

private fun JSONArray.toRoutineDtos(): List<RoutineDto> {
    return buildList {
        for (index in 0 until length()) {
            val item = opt(index)
            if (item is JSONObject) {
                add(RoutineDto.fromJson(item))
            }
        }
    }
}

private fun JSONObject.optNullable(name: String): Any? {
    return if (has(name) && !isNull(name)) opt(name) else null
}

private fun JSONObject.optFirstArray(vararg names: String): JSONArray? {
    return names.firstNotNullOfOrNull { name ->
        optNullable(name) as? JSONArray
    }
}

private fun JSONObject.optFirstString(vararg names: String): String? {
    return names.firstNotNullOfOrNull { name ->
        if (has(name) && !isNull(name)) optString(name).ifBlank { null } else null
    }
}

private fun JSONObject.optFirstInt(vararg names: String): Int? {
    return names.firstNotNullOfOrNull { name ->
        if (has(name) && !isNull(name)) optInt(name) else null
    }
}

private fun JSONObject.optFirstBoolean(vararg names: String): Boolean? {
    return names.firstNotNullOfOrNull { name ->
        if (has(name) && !isNull(name)) optBoolean(name) else null
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
