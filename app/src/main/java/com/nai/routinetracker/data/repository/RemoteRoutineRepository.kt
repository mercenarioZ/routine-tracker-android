package com.nai.routinetracker.data.repository

import android.content.Context
import com.nai.routinetracker.R
import com.nai.routinetracker.data.remote.RoutineApi
import com.nai.routinetracker.data.remote.dto.RoutineCreateRequestDto
import com.nai.routinetracker.data.remote.dto.RoutineQueryDto
import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineDashboardState
import com.nai.routinetracker.model.RoutineRecurrence
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class RemoteRoutineRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val routineApi: RoutineApi,
) : RoutineRepository {
    private val appContext = context.applicationContext
    private val dashboardState = MutableStateFlow(emptyDashboardState())

    override fun observeDashboard(): Flow<RoutineDashboardState> {
        return flow {
            refreshDashboard()
            emitAll(dashboardState)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun createRoutine(
        title: String,
        scheduleLabel: String,
        category: RoutineCategory,
        recurrence: RoutineRecurrence,
        description: String
    ) {
        withContext(Dispatchers.IO) {
            val response = routineApi.createRoutine(
                request = RoutineCreateRequestDto.fromDomain(
                    title = title,
                    scheduleLabel = scheduleLabel,
                    category = category,
                    recurrence = recurrence,
                    description = description
                ),
            )

            if (response.success == false) {
                error(response.message ?: "Unable to create routine")
            }

            refreshDashboard()
        }
    }

    private suspend fun refreshDashboard() {
        val response = routineApi.getRoutines(
            query = RoutineQueryDto.activeRoutines(),
        )

        if (response.success == false) {
            error(response.message ?: "Unable to load routines")
        }

        dashboardState.value = RoutineDashboardState(
            userName = appContext.getString(R.string.sample_user_name),
            dateLabel = currentDateLabel(),
            highlight = appContext.getString(R.string.home_highlight),
            routines = response.data.orEmpty().map { it.toDomain() }
        )
    }

    private fun emptyDashboardState(): RoutineDashboardState {
        return RoutineDashboardState(
            userName = appContext.getString(R.string.sample_user_name),
            dateLabel = currentDateLabel(),
            highlight = appContext.getString(R.string.home_highlight),
            routines = emptyList()
        )
    }

    private fun currentDateLabel(): String {
        val formatter = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        return formatter.format(Date())
    }
}
