package com.nai.routinetracker.ui.routines

import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.isDone

data class RoutinesUiState(
    val isLoading: Boolean = false,
    val routines: List<RoutineItem> = emptyList(),
    val selectedCategory: RoutineCategory? = null,
    val showCompletedOnly: Boolean = false
) {
    val visibleRoutines: List<RoutineItem>
        get() = routines.filter { routine ->
            val matchesCategory = selectedCategory == null || routine.category == selectedCategory
            val matchesCompletion = !showCompletedOnly || routine.isDone
            matchesCategory && matchesCompletion
        }.sortedBy { if (it.isDone) 1 else 0 }
}
