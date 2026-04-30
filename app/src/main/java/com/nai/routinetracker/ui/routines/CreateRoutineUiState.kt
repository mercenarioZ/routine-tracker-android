package com.nai.routinetracker.ui.routines

import com.nai.routinetracker.model.RoutineCategories
import com.nai.routinetracker.model.RoutineCategory

data class CreateRoutineUiState(
    val title: String = "",
    val scheduleLabel: String = "",
    val description: String = "",
    val selectedCategory: RoutineCategory = RoutineCategories.Health
) {
    val categories: List<RoutineCategory>
        get() = RoutineCategories.defaults

    val canSubmit: Boolean
        get() = title.isNotBlank() && scheduleLabel.isNotBlank() && description.isNotBlank()
}