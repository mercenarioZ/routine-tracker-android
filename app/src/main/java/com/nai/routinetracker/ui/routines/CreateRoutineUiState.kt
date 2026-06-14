package com.nai.routinetracker.ui.routines

import com.nai.routinetracker.model.RoutineCategories
import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineRecurrence
import com.nai.routinetracker.model.RoutineWeekday

data class CreateRoutineUiState(
    val title: String = "",
    val scheduleLabel: String = DefaultRoutineScheduleLabel,
    val description: String = "",
    val selectedCategory: RoutineCategory = RoutineCategories.Health,
    val recurrenceMode: CreateRoutineRecurrenceMode = CreateRoutineRecurrenceMode.Daily,
    val selectedWeekdays: Set<RoutineWeekday> = setOf(RoutineWeekday.Monday),
    val isSaving: Boolean = false,
    val errorMessage: String? = null
) {
    val categories: List<RoutineCategory>
        get() = RoutineCategories.defaults

    val canSubmit: Boolean
        get() = title.isNotBlank() &&
            scheduleLabel.isNotBlank() &&
            description.isNotBlank() &&
            recurrenceIsValid &&
            !isSaving

    val recurrence: RoutineRecurrence
        get() = when (recurrenceMode) {
            CreateRoutineRecurrenceMode.Daily -> RoutineRecurrence.Daily
            CreateRoutineRecurrenceMode.Weekly -> RoutineRecurrence.Weekly(
                weekday = selectedWeekdays.firstOrNull() ?: RoutineWeekday.Monday
            )
            CreateRoutineRecurrenceMode.Custom -> RoutineRecurrence.Custom(
                weekdays = selectedWeekdays
            )
        }

    private val recurrenceIsValid: Boolean
        get() = recurrenceMode != CreateRoutineRecurrenceMode.Custom || selectedWeekdays.isNotEmpty()
}

const val DefaultRoutineScheduleLabel = "06:30 AM"

enum class CreateRoutineRecurrenceMode {
    Daily,
    Weekly,
    Custom
}
