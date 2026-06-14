package com.nai.routinetracker.data.repository

import android.content.Context
import com.nai.routinetracker.R
import com.nai.routinetracker.data.remote.ApiException
import com.nai.routinetracker.data.remote.RoutineApi
import com.nai.routinetracker.data.remote.dto.RoutineQueryDto
import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.domain.session.AuthSessionStore
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteRoutineRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val routineApi: RoutineApi,
    private val authSessionStore: AuthSessionStore
) : RoutineRepository {
    private val appContext = context.applicationContext

    override fun observeDashboard(): Flow<RoutineDashboardState> {
        return flow {
            val session = authSessionStore.observeSession().first()
                ?: throw ApiException(
                    statusCode = 401,
                    message = "Please log in again"
                )

            val response = routineApi.getRoutines(
                query = RoutineQueryDto.activeForToday(),
                authorizationHeader = session.authorizationHeader
            )

            if (response.success == false) {
                error(response.message ?: "Unable to load routines")
            }

            emit(
                RoutineDashboardState(
                    userName = appContext.getString(R.string.sample_user_name),
                    dateLabel = currentDateLabel(),
                    highlight = appContext.getString(R.string.home_highlight),
                    routines = response.data.orEmpty().map { it.toDomain() }
                )
            )
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun createRoutine(
        title: String,
        scheduleLabel: String,
        category: RoutineCategory,
        recurrence: RoutineRecurrence,
        description: String
    ) {
        error("Creating routines is not wired to the backend yet")
    }

    private fun currentDateLabel(): String {
        val formatter = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        return formatter.format(Date())
    }
}
