package com.nai.routinetracker.data.repository

import com.nai.routinetracker.model.RoutineDashboardState
import com.nai.routinetracker.model.RoutineItem

interface RoutineRepository {
    fun getDashboardState(): RoutineDashboardState

    fun updateRoutines(routines: List<RoutineItem>): RoutineDashboardState
}
