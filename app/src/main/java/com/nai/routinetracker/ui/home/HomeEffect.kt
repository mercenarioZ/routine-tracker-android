package com.nai.routinetracker.ui.home

sealed interface HomeEffect {
    data class ShowRoutineStatusChanged(
        val routineTitle: String,
        val isCompleted: Boolean
    ) : HomeEffect
}
