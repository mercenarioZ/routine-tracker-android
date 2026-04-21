package com.nai.routinetracker.model

data class TaskCategory(
    val id: String,
    val label: String,
    val isSystem: Boolean = true
)

object TaskCategories {
    val Work = TaskCategory(
        id = "work",
        label = "Work"
    )

    val Personal = TaskCategory(
        id = "personal",
        label = "Personal"
    )

    val Admin = TaskCategory(
        id = "admin",
        label = "Admin"
    )

    val defaults = listOf(Work, Personal, Admin)
}

enum class TaskStatus {
    Pending,
    InProgress,
    Done
}

data class TaskItem(
    val id: String,
    val routineId: String?,
    val title: String,
    val timeLabel: String,
    val dueLabel: String,
    val category: TaskCategory,
    val status: TaskStatus,
    val description: String
)

val TaskItem.isDone: Boolean
    get() = status == TaskStatus.Done

fun TaskStatus.next(): TaskStatus {
    return when (this) {
        TaskStatus.Pending -> TaskStatus.InProgress
        TaskStatus.InProgress -> TaskStatus.Done
        TaskStatus.Done -> TaskStatus.Pending
    }
}

fun RoutineCategory.toTaskCategory(): TaskCategory {
    return TaskCategory(
        id = id,
        label = label,
        isSystem = isSystem
    )
}
