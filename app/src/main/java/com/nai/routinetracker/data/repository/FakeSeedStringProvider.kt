package com.nai.routinetracker.data.repository

import android.content.Context
import com.nai.routinetracker.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

data class FakeSeedStrings(
    val sampleUserName: String,
    val homeHighlight: String,
    val hydrationTitle: String,
    val hydrationSchedule: String,
    val hydrationDescription: String,
    val planningTitle: String,
    val planningSchedule: String,
    val planningDescription: String,
    val focusTitle: String,
    val focusSchedule: String,
    val focusDescription: String,
    val learningTitle: String,
    val learningSchedule: String,
    val learningDescription: String,
    val taskTodayDueLabel: String,
    val taskTomorrowDueLabel: String
)

interface FakeSeedStringProvider {
    fun strings(): FakeSeedStrings
}

class AndroidFakeSeedStringProvider @Inject constructor(
    @ApplicationContext context: Context
) : FakeSeedStringProvider {
    private val appContext = context.applicationContext

    override fun strings(): FakeSeedStrings {
        return FakeSeedStrings(
            sampleUserName = appContext.getString(R.string.sample_user_name),
            homeHighlight = appContext.getString(R.string.home_highlight),
            hydrationTitle = appContext.getString(R.string.routine_hydration_title),
            hydrationSchedule = appContext.getString(R.string.routine_hydration_schedule),
            hydrationDescription = appContext.getString(R.string.routine_hydration_description),
            planningTitle = appContext.getString(R.string.routine_planning_title),
            planningSchedule = appContext.getString(R.string.routine_planning_schedule),
            planningDescription = appContext.getString(R.string.routine_planning_description),
            focusTitle = appContext.getString(R.string.routine_focus_title),
            focusSchedule = appContext.getString(R.string.routine_focus_schedule),
            focusDescription = appContext.getString(R.string.routine_focus_description),
            learningTitle = appContext.getString(R.string.routine_learning_title),
            learningSchedule = appContext.getString(R.string.routine_learning_schedule),
            learningDescription = appContext.getString(R.string.routine_learning_description),
            taskTodayDueLabel = appContext.getString(R.string.task_today_due_label),
            taskTomorrowDueLabel = appContext.getString(R.string.task_tomorrow_due_label)
        )
    }
}
