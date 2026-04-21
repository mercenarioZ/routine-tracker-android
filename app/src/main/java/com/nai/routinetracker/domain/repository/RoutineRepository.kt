package com.nai.routinetracker.domain.repository

import com.nai.routinetracker.model.RoutineDashboardState
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    fun observeDashboard(): Flow<RoutineDashboardState>
}
