package com.nai.routinetracker.data.repository

import com.nai.routinetracker.model.RoutineDashboardState
import com.nai.routinetracker.model.RoutineItem

interface RoutineRepository {
    suspend fun getDashboardState(): RoutineDashboardState

    suspend fun updateRoutines(routines: List<RoutineItem>): RoutineDashboardState
}
