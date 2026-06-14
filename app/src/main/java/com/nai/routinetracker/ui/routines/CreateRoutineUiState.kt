package com.nai.routinetracker.ui.routines

import com.nai.routinetracker.model.RoutineCategories
import com.nai.routinetracker.model.RoutineCategory

data class CreateRoutineUiState(
    val title: String = "",
    val scheduleLabel: String = DefaultRoutineScheduleLabel,
    val description: String = "",
    val selectedCategory: RoutineCategory = RoutineCategories.Health,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
) {
    val categories: List<RoutineCategory>
        get() = RoutineCategories.defaults

    val canSubmit: Boolean
        get() = title.isNotBlank() &&
            scheduleLabel.isNotBlank() &&
            description.isNotBlank() &&
            !isSaving
}

const val DefaultRoutineScheduleLabel = "06:30 AM"
