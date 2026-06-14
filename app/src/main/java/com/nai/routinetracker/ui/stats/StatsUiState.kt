package com.nai.routinetracker.ui.stats

import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.TaskItem
import com.nai.routinetracker.model.isDone

data class StatsUiState(
    val isLoading: Boolean = false,
    val routines: List<RoutineItem> = emptyList(),
    val tasks: List<TaskItem> = emptyList()
) {
    val hasData: Boolean
        get() = routines.isNotEmpty() || tasks.isNotEmpty()

    val completedTaskCount: Int
        get() = tasks.count { it.isDone }

    val openTaskCount: Int
        get() = tasks.size - completedTaskCount

    val completionRatio: Float
        get() = if (tasks.isEmpty()) 0f else completedTaskCount.toFloat() / tasks.size

    val completionPercent: Int
        get() = (completionRatio * 100).toInt()

    val activeRoutineCount: Int
        get() = routines.count { it.isActive }

    val totalStreakDays: Int
        get() = routines.sumOf { it.streakDays }

    val bestStreakDays: Int
        get() = routines.maxOfOrNull { it.streakDays } ?: 0

    val completionSegments: List<StatsSegment>
        get() = listOf(
            StatsSegment(
                id = "done",
                label = "Done",
                value = completedTaskCount
            ),
            StatsSegment(
                id = "open",
                label = "Open",
                value = openTaskCount
            )
        ).filter { it.value > 0 }

    val taskCategorySegments: List<StatsSegment>
        get() = tasks
            .groupBy { it.category }
            .map { (category, categoryTasks) ->
                StatsSegment(
                    id = category.id,
                    label = category.label,
                    value = categoryTasks.size
                )
            }
            .sortedByDescending { it.value }

    val routineCategorySegments: List<StatsSegment>
        get() = routines
            .groupBy { it.category }
            .map { (category, categoryRoutines) ->
                StatsSegment(
                    id = category.id,
                    label = category.label,
                    value = categoryRoutines.size
                )
            }
            .sortedByDescending { it.value }

    val routineStreakBars: List<StatsSegment>
        get() = routines
            .sortedWith(
                compareByDescending<RoutineItem> { it.streakDays }
                    .thenBy { it.title }
            )
            .take(5)
            .map { routine ->
                StatsSegment(
                    id = routine.category.id,
                    label = routine.title,
                    value = routine.streakDays
                )
            }
}

data class StatsSegment(
    val id: String,
    val label: String,
    val value: Int
)
