package com.nai.routinetracker.model

data class RoutineCategory(
    val id: String,
    val label: String,
    val isSystem: Boolean = true
)

object RoutineCategories {
    val Health = RoutineCategory(
        id = "health",
        label = "Health"
    )

    val Focus = RoutineCategory(
        id = "focus",
        label = "Focus"
    )

    val Learning = RoutineCategory(
        id = "learning",
        label = "Learning"
    )

    val Planning = RoutineCategory(
        id = "planning",
        label = "Planning"
    )

    val defaults = listOf(Health, Focus, Learning, Planning)
}

enum class RoutineStatus {
    Pending,
    InProgress,
    Done
}

data class RoutineItem(
    val id: String,
    val title: String,
    val scheduleLabel: String,
    val category: RoutineCategory,
    val streakDays: Int,
    val status: RoutineStatus,
    val description: String
)

data class RoutineDashboardState(
    val userName: String,
    val dateLabel: String,
    val highlight: String,
    val routines: List<RoutineItem>
)

val RoutineItem.isDone: Boolean
    get() = status == RoutineStatus.Done

fun RoutineStatus.next(): RoutineStatus {
    return when (this) {
        RoutineStatus.Pending -> RoutineStatus.InProgress
        RoutineStatus.InProgress -> RoutineStatus.Done
        RoutineStatus.Done -> RoutineStatus.Pending
    }
}
