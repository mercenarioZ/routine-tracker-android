package com.nai.routinetracker.data.repository

import com.nai.routinetracker.domain.repository.TaskRepository
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.TaskItem
import com.nai.routinetracker.model.TaskStatus
import com.nai.routinetracker.model.next
import com.nai.routinetracker.model.toTaskCategory
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class FakeTaskRepository @Inject constructor(
    seedStringProvider: FakeSeedStringProvider
) : TaskRepository {
    private val seedStrings = seedStringProvider.strings()
    private val tasksState = MutableStateFlow(
        buildSampleTasks(
            strings = seedStrings,
            routines = buildSampleRoutines(seedStrings)
        )
    )

    override fun observeTasks(): Flow<List<TaskItem>> = tasksState.asStateFlow()

    override suspend fun createTaskFromRoutine(routine: RoutineItem) =
        withContext(Dispatchers.IO) {
            tasksState.update { tasks ->
                tasks + TaskItem(
                    id = "task-${routine.id}-today",
                    routineId = routine.id,
                    title = routine.title,
                    timeLabel = routine.scheduleLabel,
                    dueLabel = seedStrings.taskTodayDueLabel,
                    category = routine.category.toTaskCategory(),
                    status = TaskStatus.Pending,
                    description = routine.description
                )
            }
        }

    override suspend fun toggleTask(taskId: String) =
        withContext(Dispatchers.IO) {
            tasksState.update { tasks ->
                tasks.map { task ->
                    if (task.id == taskId) {
                        task.copy(status = task.status.next())
                    } else {
                        task
                    }
                }
            }
        }

}
