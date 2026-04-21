package com.nai.routinetracker.ui.home

import com.nai.routinetracker.model.RoutineDashboardState
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.isDone

data class HomeUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val dateLabel: String = "",
    val highlight: String = "",
    val routines: List<RoutineItem> = emptyList()
) {
    val completedCount: Int
        get() = routines.count { it.isDone }

    val completionRatio: Float
        get() = if (routines.isEmpty()) 0f else completedCount.toFloat() / routines.size

    val totalStreakDays: Int
        get() = routines.sumOf { it.streakDays }

    val orderedRoutines: List<RoutineItem>
        get() = routines.sortedBy { if (it.isDone) 1 else 0 }

    val nextRoutine: RoutineItem?
        get() = routines.firstOrNull { !it.isDone }
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
