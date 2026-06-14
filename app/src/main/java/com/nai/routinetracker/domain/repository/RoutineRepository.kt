package com.nai.routinetracker.domain.repository

import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineDashboardState
import com.nai.routinetracker.model.RoutineRecurrence
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    fun observeDashboard(): Flow<RoutineDashboardState>

    suspend fun createRoutine(
        title: String,
        scheduleLabel: String,
        category: RoutineCategory,
        recurrence: RoutineRecurrence,
        description: String
    )
}
