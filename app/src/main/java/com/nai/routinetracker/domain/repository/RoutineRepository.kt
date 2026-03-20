package com.nai.routinetracker.domain.repository

import com.nai.routinetracker.model.RoutineDashboardState
import com.nai.routinetracker.model.RoutineItem

interface RoutineRepository {
    suspend fun getDashboard(): RoutineDashboardState

    suspend fun updateRoutines(routines: List<RoutineItem>): RoutineDashboardState
}