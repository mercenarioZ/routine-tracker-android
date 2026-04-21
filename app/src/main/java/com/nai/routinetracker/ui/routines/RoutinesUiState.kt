package com.nai.routinetracker.ui.routines

import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineItem

data class RoutinesUiState(
    val isLoading: Boolean = false,
    val routines: List<RoutineItem> = emptyList(),
    val selectedCategory: RoutineCategory? = null
) {
    val categories: List<RoutineCategory>
        get() = routines.map { it.category }.distinctBy { it.id }

    val visibleRoutines: List<RoutineItem>
        get() = routines.filter { routine ->
            selectedCategory == null || routine.category == selectedCategory
        }
}
