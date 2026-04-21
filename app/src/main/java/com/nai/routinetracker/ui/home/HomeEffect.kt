package com.nai.routinetracker.ui.home

import com.nai.routinetracker.model.TaskStatus

sealed interface HomeEffect {
    data class ShowTaskStatusChanged(
        val taskTitle: String,
        val newStatus: TaskStatus
    ) : HomeEffect
}
