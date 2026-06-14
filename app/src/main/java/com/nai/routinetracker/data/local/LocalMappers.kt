package com.nai.routinetracker.data.local

import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.RoutineRecurrence
import com.nai.routinetracker.model.RoutineWeekday
import com.nai.routinetracker.model.TaskCategory
import com.nai.routinetracker.model.TaskItem
import com.nai.routinetracker.model.TaskStatus

fun RoutineItem.toLocalEntity(sortOrder: Int): LocalRoutineEntity {
    return LocalRoutineEntity(
        id = id,
        title = title,
        scheduleLabel = scheduleLabel,
        categoryId = category.id,
        categoryLabel = category.label,
        categoryIsSystem = category.isSystem,
        recurrenceType = recurrence.toLocalType(),
        recurrenceDays = recurrence.toLocalDays(),
        streakDays = streakDays,
        isActive = isActive,
        description = description,
        sortOrder = sortOrder
    )
}

fun LocalRoutineEntity.toDomain(): RoutineItem {
    return RoutineItem(
        id = id,
        title = title,
        scheduleLabel = scheduleLabel,
        category = RoutineCategory(
            id = categoryId,
            label = categoryLabel,
            isSystem = categoryIsSystem
        ),
        recurrence = toRoutineRecurrence(
            type = recurrenceType,
            days = recurrenceDays
        ),
        streakDays = streakDays,
        isActive = isActive,
        description = description
    )
}

fun TaskItem.toLocalEntity(sortOrder: Int): LocalTaskEntity {
    return LocalTaskEntity(
        id = id,
        routineId = routineId,
        title = title,
        timeLabel = timeLabel,
        dueLabel = dueLabel,
        categoryId = category.id,
        categoryLabel = category.label,
        categoryIsSystem = category.isSystem,
        status = status.name,
        description = description,
        sortOrder = sortOrder
    )
}

fun LocalTaskEntity.toDomain(): TaskItem {
    return TaskItem(
        id = id,
        routineId = routineId,
        title = title,
        timeLabel = timeLabel,
        dueLabel = dueLabel,
        category = TaskCategory(
            id = categoryId,
            label = categoryLabel,
            isSystem = categoryIsSystem
        ),
        status = TaskStatus.valueOf(status),
        description = description
    )
}

private fun RoutineRecurrence.toLocalType(): String {
    return when (this) {
        RoutineRecurrence.Daily -> RecurrenceTypeDaily
        is RoutineRecurrence.Weekly -> RecurrenceTypeWeekly
        is RoutineRecurrence.Custom -> RecurrenceTypeCustom
    }
}

private fun RoutineRecurrence.toLocalDays(): String {
    val days = when (this) {
        RoutineRecurrence.Daily -> emptySet()
        is RoutineRecurrence.Weekly -> setOf(weekday)
        is RoutineRecurrence.Custom -> weekdays
    }
    return days
        .sortedBy { it.storageValue }
        .joinToString(separator = ",") { it.storageValue.toString() }
}

private fun toRoutineRecurrence(
    type: String,
    days: String
): RoutineRecurrence {
    val weekdays = days
        .split(',')
        .mapNotNull { value -> value.trim().toIntOrNull()?.let(RoutineWeekday::fromStorageValue) }
        .toSet()

    return when (type.lowercase()) {
        RecurrenceTypeWeekly -> RoutineRecurrence.Weekly(
            weekday = weekdays.firstOrNull() ?: RoutineWeekday.Monday
        )
        RecurrenceTypeCustom -> RoutineRecurrence.Custom(
            weekdays = weekdays.ifEmpty { setOf(RoutineWeekday.Monday) }
        )
        else -> RoutineRecurrence.Daily
    }
}

private const val RecurrenceTypeDaily = "daily"
private const val RecurrenceTypeWeekly = "weekly"
private const val RecurrenceTypeCustom = "custom"
