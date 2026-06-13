package com.nai.routinetracker.data.repository

import com.nai.routinetracker.model.RoutineCategories
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.TaskItem
import com.nai.routinetracker.model.TaskStatus
import com.nai.routinetracker.model.toTaskCategory

internal fun buildSampleRoutines(strings: FakeSeedStrings): List<RoutineItem> {
    return listOf(
        RoutineItem(
            id = "hydration",
            title = strings.hydrationTitle,
            scheduleLabel = strings.hydrationSchedule,
            category = RoutineCategories.Health,
            streakDays = 12,
            description = strings.hydrationDescription
        ),
        RoutineItem(
            id = "planning",
            title = strings.planningTitle,
            scheduleLabel = strings.planningSchedule,
            category = RoutineCategories.Planning,
            streakDays = 8,
            description = strings.planningDescription
        ),
        RoutineItem(
            id = "focus",
            title = strings.focusTitle,
            scheduleLabel = strings.focusSchedule,
            category = RoutineCategories.Focus,
            streakDays = 15,
            description = strings.focusDescription
        ),
        RoutineItem(
            id = "learning",
            title = strings.learningTitle,
            scheduleLabel = strings.learningSchedule,
            category = RoutineCategories.Learning,
            streakDays = 5,
            description = strings.learningDescription
        )
    )
}

internal fun buildSampleTasks(strings: FakeSeedStrings, routines: List<RoutineItem>): List<TaskItem> {
    val routinesById = routines.associateBy { it.id }

    fun taskFromRoutine(
        routineId: String,
        taskId: String,
        dueLabel: FakeDueLabel,
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
            dueLabel = when (dueLabel) {
                FakeDueLabel.Today -> strings.taskTodayDueLabel
                FakeDueLabel.Tomorrow -> strings.taskTomorrowDueLabel
            },
            category = routine.category.toTaskCategory(),
            status = status,
            description = routine.description
        )
    }

    return listOf(
        taskFromRoutine(
            routineId = "hydration",
            taskId = "task-hydration-today",
            dueLabel = FakeDueLabel.Today,
            status = TaskStatus.Pending
        ),
        taskFromRoutine(
            routineId = "planning",
            taskId = "task-planning-today",
            dueLabel = FakeDueLabel.Today,
            status = TaskStatus.Pending
        ),
        taskFromRoutine(
            routineId = "focus",
            taskId = "task-focus-today",
            dueLabel = FakeDueLabel.Today,
            status = TaskStatus.Done
        ),
        taskFromRoutine(
            routineId = "learning",
            taskId = "task-learning-tonight",
            dueLabel = FakeDueLabel.Tomorrow,
            status = TaskStatus.Pending
        )
    )
}

private enum class FakeDueLabel {
    Today,
    Tomorrow
}
