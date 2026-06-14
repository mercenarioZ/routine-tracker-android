package com.nai.routinetracker.ui.calendar

import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.domain.repository.TaskRepository
import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineDashboardState
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.RoutineRecurrence
import com.nai.routinetracker.model.TaskItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class CalendarViewModelTest {
    @Test
    fun onToggleTask_delegatesToTaskRepository() {
        val taskRepository = RecordingTaskRepository()
        val viewModel = CalendarViewModel(
            routineRepository = EmptyRoutineRepository(),
            taskRepository = taskRepository
        )

        viewModel.onToggleTask("task-1")
        shadowOf(android.os.Looper.getMainLooper()).idle()

        assertEquals(listOf("task-1"), taskRepository.toggledTaskIds)
    }
}

private class EmptyRoutineRepository : RoutineRepository {
    private val dashboard = MutableStateFlow(
        RoutineDashboardState(
            userName = "Builder",
            dateLabel = "Today",
            highlight = "",
            routines = emptyList()
        )
    )

    override fun observeDashboard(): Flow<RoutineDashboardState> {
        return dashboard
    }

    override suspend fun createRoutine(
        title: String,
        scheduleLabel: String,
        category: RoutineCategory,
        recurrence: RoutineRecurrence,
        description: String
    ) = Unit
}

private class RecordingTaskRepository : TaskRepository {
    private val tasks = MutableStateFlow(emptyList<TaskItem>())
    val toggledTaskIds = mutableListOf<String>()

    override fun observeTasks(): Flow<List<TaskItem>> {
        return tasks
    }

    override suspend fun createTaskFromRoutine(routine: RoutineItem) = Unit

    override suspend fun toggleTask(taskId: String) {
        toggledTaskIds += taskId
    }
}
