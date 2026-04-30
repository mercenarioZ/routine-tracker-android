package com.nai.routinetracker.domain.repository

import com.nai.routinetracker.model.RoutineDashboardState
import com.nai.routinetracker.model.RoutineItem
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    fun observeDashboard(): Flow<RoutineDashboardState>

    suspend fun createRoutine(routine: RoutineItem)
}
