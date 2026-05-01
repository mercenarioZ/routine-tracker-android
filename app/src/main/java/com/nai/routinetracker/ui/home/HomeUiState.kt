package com.nai.routinetracker.ui.home

import com.nai.routinetracker.model.RoutineDashboardState
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.TaskItem
import com.nai.routinetracker.model.isDone

data class HomeUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val dateLabel: String = "",
    val highlight: String? = "",
    val routines: List<RoutineItem> = emptyList(),
    val tasks: List<TaskItem> = emptyList()
) {
    val completedCount: Int
        get() = tasks.count { it.isDone }

    val completionRatio: Float
        get() = if (tasks.isEmpty()) 0f else completedCount.toFloat() / tasks.size

    val totalStreakDays: Int
        get() = 10

    val orderedTasks: List<TaskItem>
        get() = tasks.sortedBy { if (it.isDone) 1 else 0 }

    val nextTask: TaskItem?
        get() = tasks.firstOrNull { !it.isDone }
}

fun RoutineDashboardState.toUiState(): HomeUiState {
    return HomeUiState(
        isLoading = false,
        userName = userName,
        dateLabel = dateLabel,
        highlight = highlight,
        routines = routines
    )
}
