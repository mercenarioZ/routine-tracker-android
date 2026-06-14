package com.nai.routinetracker.data.repository

import com.nai.routinetracker.data.local.LocalDatabaseSeeder
import com.nai.routinetracker.data.local.TaskDao
import com.nai.routinetracker.data.local.toDomain
import com.nai.routinetracker.data.local.toLocalEntity
import com.nai.routinetracker.domain.repository.TaskRepository
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.TaskItem
import com.nai.routinetracker.model.TaskStatus
import com.nai.routinetracker.model.toTaskCategory
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocalTaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val seeder: LocalDatabaseSeeder,
    seedStringProvider: FakeSeedStringProvider
) : TaskRepository {
    private val seedStrings = seedStringProvider.strings()

    override fun observeTasks(): Flow<List<TaskItem>> {
        return flow {
            seeder.seedIfNeeded()
            emitAll(
                taskDao.observeTasks().map { tasks ->
                    tasks.map { it.toDomain() }
                }
            )
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun createTaskFromRoutine(routine: RoutineItem) =
        withContext(Dispatchers.IO) {
            seeder.seedIfNeeded()
            taskDao.insert(
                TaskItem(
                    id = "task-${routine.id}-today",
                    routineId = routine.id,
                    title = routine.title,
                    timeLabel = routine.scheduleLabel,
                    dueLabel = seedStrings.taskTodayDueLabel,
                    category = routine.category.toTaskCategory(),
                    status = TaskStatus.Pending,
                    description = routine.description
                ).toLocalEntity(sortOrder = taskDao.count())
            )
        }

    override suspend fun toggleTask(taskId: String) =
        withContext(Dispatchers.IO) {
            seeder.seedIfNeeded()
            taskDao.toggleStatus(taskId)
        }
}
