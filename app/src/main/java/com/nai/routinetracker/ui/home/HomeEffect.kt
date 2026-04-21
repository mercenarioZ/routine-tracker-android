package com.nai.routinetracker.ui.home

import com.nai.routinetracker.model.RoutineStatus

sealed interface HomeEffect {
    data class ShowRoutineStatusChanged(
        val routineTitle: String,
        val newStatus: RoutineStatus
    ) : HomeEffect
}
