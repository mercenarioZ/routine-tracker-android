package com.nai.routinetracker.data.repository

import android.content.Context
import com.nai.routinetracker.R
import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.model.RoutineCategories
import com.nai.routinetracker.model.RoutineDashboardState
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.RoutineStatus
import com.nai.routinetracker.model.isDone
import com.nai.routinetracker.model.next
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class FakeRoutineRepository @Inject constructor(
    @ApplicationContext context: Context
) : RoutineRepository {
    private val appContext = context.applicationContext
    private val dashboardState = MutableStateFlow(buildDashboardState())

    override fun observeDashboard(): Flow<RoutineDashboardState> = dashboardState.asStateFlow()

    override suspend fun toggleRoutine(routineId: String) =
        withContext(Dispatchers.IO) {
            dashboardState.update { currentDashboard ->
                currentDashboard.copy(
                    routines = currentDashboard.routines.map { routine ->
                        if (routine.id == routineId) {
                            routine.updatedCompletion()
                        } else {
                            routine
                        }
                    }
                )
            }
        }

    private fun buildDashboardState(): RoutineDashboardState {
        return RoutineDashboardState(
            userName = appContext.getString(R.string.sample_user_name),
            dateLabel = currentDateLabel(),
            highlight = appContext.getString(R.string.home_highlight),
            routines = listOf(
                RoutineItem(
                    id = "hydration",
                    title = appContext.getString(R.string.routine_hydration_title),
                    scheduleLabel = appContext.getString(R.string.routine_hydration_schedule),
                    category = RoutineCategories.Health,
                    streakDays = 12,
                    status = RoutineStatus.Done,
                    description = appContext.getString(R.string.routine_hydration_description)
                ),
                RoutineItem(
                    id = "planning",
                    title = appContext.getString(R.string.routine_planning_title),
                    scheduleLabel = appContext.getString(R.string.routine_planning_schedule),
                    category = RoutineCategories.Planning,
                    streakDays = 8,
                    status = RoutineStatus.InProgress,
                    description = appContext.getString(R.string.routine_planning_description)
                ),
                RoutineItem(
                    id = "focus",
                    title = appContext.getString(R.string.routine_focus_title),
                    scheduleLabel = appContext.getString(R.string.routine_focus_schedule),
                    category = RoutineCategories.Focus,
                    streakDays = 15,
                    status = RoutineStatus.Pending,
                    description = appContext.getString(R.string.routine_focus_description)
                ),
                RoutineItem(
                    id = "learning",
                    title = appContext.getString(R.string.routine_learning_title),
                    scheduleLabel = appContext.getString(R.string.routine_learning_schedule),
                    category = RoutineCategories.Learning,
                    streakDays = 5,
                    status = RoutineStatus.Done,
                    description = appContext.getString(R.string.routine_learning_description)
                )
            )
        )
    }

    private fun currentDateLabel(): String {
        val formatter = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        return formatter.format(Date())
    }
}

private fun RoutineItem.updatedCompletion(): RoutineItem {
    val nextStatus = status.next()

    return copy(
        status = nextStatus,
        streakDays = when {
            !isDone && nextStatus == RoutineStatus.Done -> streakDays + 1
            isDone && nextStatus != RoutineStatus.Done -> (streakDays - 1).coerceAtLeast(0)
            else -> streakDays
        }
    )
}
