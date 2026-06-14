package com.nai.routinetracker.data.local

import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineItem
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
