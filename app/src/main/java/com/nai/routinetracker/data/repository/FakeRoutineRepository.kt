package com.nai.routinetracker.data.repository

import android.content.Context
import com.nai.routinetracker.R
import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.model.RoutineDashboardState
import com.nai.routinetracker.model.RoutineItem
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeRoutineRepository @Inject constructor(
    @ApplicationContext context: Context
) : RoutineRepository {
    private val appContext = context.applicationContext
    private val dashboardState = MutableStateFlow(buildDashboardState())

    override fun observeDashboard(): Flow<RoutineDashboardState> = dashboardState.asStateFlow()

    override suspend fun createRoutine(routine: RoutineItem) {
        dashboardState.value = dashboardState.value.copy(
            routines = dashboardState.value.routines + routine
        )
    }

    private fun buildDashboardState(): RoutineDashboardState {
        return RoutineDashboardState(
            userName = appContext.getString(R.string.sample_user_name),
            dateLabel = currentDateLabel(),
            highlight = appContext.getString(R.string.home_highlight),
            routines = buildSampleRoutines(appContext)
        )
    }

    private fun currentDateLabel(): String {
        val formatter = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        return formatter.format(Date())
    }
}
