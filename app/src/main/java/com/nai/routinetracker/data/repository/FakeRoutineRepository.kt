package com.nai.routinetracker.data.repository

import android.content.Context
import com.nai.routinetracker.R
import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineDashboardState
import com.nai.routinetracker.model.RoutineItem
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
                    category = RoutineCategory.Health,
                    streakDays = 12,
                    completed = true,
                    description = appContext.getString(R.string.routine_hydration_description)
                ),
                RoutineItem(
                    id = "planning",
                    title = appContext.getString(R.string.routine_planning_title),
                    scheduleLabel = appContext.getString(R.string.routine_planning_schedule),
                    category = RoutineCategory.Planning,
                    streakDays = 8,
                    completed = false,
                    description = appContext.getString(R.string.routine_planning_description)
                ),
                RoutineItem(
                    id = "focus",
                    title = appContext.getString(R.string.routine_focus_title),
                    scheduleLabel = appContext.getString(R.string.routine_focus_schedule),
                    category = RoutineCategory.Focus,
                    streakDays = 15,
                    completed = true,
                    description = appContext.getString(R.string.routine_focus_description)
                ),
                RoutineItem(
                    id = "learning",
                    title = appContext.getString(R.string.routine_learning_title),
                    scheduleLabel = appContext.getString(R.string.routine_learning_schedule),
                    category = RoutineCategory.Learning,
                    streakDays = 5,
                    completed = false,
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
    return copy(
        completed = !completed,
        streakDays = if (completed) {
            (streakDays - 1).coerceAtLeast(0)
        } else {
            streakDays + 1
        }
    )
}
