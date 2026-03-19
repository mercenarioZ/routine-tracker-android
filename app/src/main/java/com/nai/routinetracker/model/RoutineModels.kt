package com.nai.routinetracker.model

enum class RoutineCategory {
    Health,
    Focus,
    Learning,
    Planning
}

data class RoutineItem(
    val id: String,
    val title: String,
    val scheduleLabel: String,
    val category: RoutineCategory,
    val streakDays: Int,
    val completed: Boolean,
    val description: String
)

data class RoutineDashboardState(
    val userName: String,
    val dateLabel: String,
    val highlight: String,
    val routines: List<RoutineItem>
)
