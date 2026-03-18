package com.nai.routinetracker.data.repository

import android.content.Context
import com.nai.routinetracker.R
import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineDashboardState
import com.nai.routinetracker.model.RoutineItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FakeRoutineRepository(
    context: Context
) : RoutineRepository {
    private val appContext = context.applicationContext

    private var dashboardState = buildDashboardState()

    override fun getDashboardState(): RoutineDashboardState = dashboardState

    override fun updateRoutines(routines: List<RoutineItem>): RoutineDashboardState {
        dashboardState = dashboardState.copy(routines = routines)
        return dashboardState
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
