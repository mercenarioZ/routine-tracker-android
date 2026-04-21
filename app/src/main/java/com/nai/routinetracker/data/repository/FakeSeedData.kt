package com.nai.routinetracker.data.repository

import android.content.Context
import com.nai.routinetracker.R
import com.nai.routinetracker.model.RoutineCategories
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.TaskItem
import com.nai.routinetracker.model.TaskStatus
import com.nai.routinetracker.model.toTaskCategory

internal fun buildSampleRoutines(context: Context): List<RoutineItem> {
    return listOf(
        RoutineItem(
            id = "hydration",
            title = context.getString(R.string.routine_hydration_title),
            scheduleLabel = context.getString(R.string.routine_hydration_schedule),
            category = RoutineCategories.Health,
            streakDays = 12,
            description = context.getString(R.string.routine_hydration_description)
        ),
        RoutineItem(
            id = "planning",
            title = context.getString(R.string.routine_planning_title),
            scheduleLabel = context.getString(R.string.routine_planning_schedule),
            category = RoutineCategories.Planning,
            streakDays = 8,
            description = context.getString(R.string.routine_planning_description)
        ),
        RoutineItem(
            id = "focus",
            title = context.getString(R.string.routine_focus_title),
            scheduleLabel = context.getString(R.string.routine_focus_schedule),
            category = RoutineCategories.Focus,
            streakDays = 15,
            description = context.getString(R.string.routine_focus_description)
        ),
        RoutineItem(
            id = "learning",
            title = context.getString(R.string.routine_learning_title),
            scheduleLabel = context.getString(R.string.routine_learning_schedule),
            category = RoutineCategories.Learning,
            streakDays = 5,
            description = context.getString(R.string.routine_learning_description)
        )
    )
}

internal fun buildSampleTasks(context: Context, routines: List<RoutineItem>): List<TaskItem> {
    val routinesById = routines.associateBy { it.id }

    fun taskFromRoutine(
        routineId: String,
        taskId: String,
        dueLabelRes: Int,
        status: TaskStatus
    ): TaskItem {
        val routine = requireNotNull(routinesById[routineId]) {
            "Missing routine '$routineId' for fake task seed data."
        }
        return TaskItem(
            id = taskId,
            routineId = routine.id,
            title = routine.title,
            timeLabel = routine.scheduleLabel,
            dueLabel = context.getString(dueLabelRes),
            category = routine.category.toTaskCategory(),
            status = status,
            description = routine.description
        )
    }

    return listOf(
        taskFromRoutine(
            routineId = "hydration",
            taskId = "task-hydration-today",
            dueLabelRes = R.string.task_today_due_label,
            status = TaskStatus.InProgress
        ),
        taskFromRoutine(
            routineId = "planning",
            taskId = "task-planning-today",
            dueLabelRes = R.string.task_today_due_label,
            status = TaskStatus.Pending
        ),
        taskFromRoutine(
            routineId = "focus",
            taskId = "task-focus-today",
            dueLabelRes = R.string.task_today_due_label,
            status = TaskStatus.Done
        ),
        taskFromRoutine(
            routineId = "learning",
            taskId = "task-learning-tonight",
            dueLabelRes = R.string.task_tomorrow_due_label,
            status = TaskStatus.Pending
        )
    )
}
