package com.nai.routinetracker.data.repository

import com.nai.routinetracker.data.remote.ApiException
import com.nai.routinetracker.data.remote.TaskApi
import com.nai.routinetracker.data.remote.dto.TaskCreateRequestDto
import com.nai.routinetracker.data.remote.dto.TaskUpdateRequestDto
import com.nai.routinetracker.domain.repository.TaskRepository
import com.nai.routinetracker.domain.session.AuthSession
import com.nai.routinetracker.domain.session.AuthSessionStore
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.TaskItem
import com.nai.routinetracker.model.next
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class RemoteTaskRepository @Inject constructor(
    private val taskApi: TaskApi,
    private val authSessionStore: AuthSessionStore
) : TaskRepository {
    private val tasksState = MutableStateFlow(emptyList<TaskItem>())

    override fun observeTasks(): Flow<List<TaskItem>> {
        return flow {
            refreshTasks()
            emitAll(tasksState)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun createTaskFromRoutine(routine: RoutineItem) {
        withContext(Dispatchers.IO) {
            val response = taskApi.createTask(
                request = TaskCreateRequestDto.fromRoutine(routine),
                authorizationHeader = activeSession().authorizationHeader
            )

            if (response.success == false) {
                error(response.message ?: "Unable to create task")
            }

            refreshTasks()
        }
    }

    override suspend fun toggleTask(taskId: String) {
        withContext(Dispatchers.IO) {
            val currentTask = tasksState.value.firstOrNull { it.id == taskId }
                ?: refreshTasks().firstOrNull { it.id == taskId }
                ?: error("Task not found")

            val response = taskApi.updateTaskCompletion(
                taskId = taskId,
                request = TaskUpdateRequestDto.fromStatus(currentTask.status.next()),
                authorizationHeader = activeSession().authorizationHeader
            )

            if (response.success == false) {
                error(response.message ?: "Unable to update task")
            }

            refreshTasks()
        }
    }

    private suspend fun refreshTasks(): List<TaskItem> {
        val response = taskApi.getTasks(
            authorizationHeader = activeSession().authorizationHeader
        )

        if (response.success == false) {
            error(response.message ?: "Unable to load tasks")
        }

        val tasks = response.data.orEmpty().map { it.toDomain() }
        tasksState.value = tasks
        return tasks
    }

    private suspend fun activeSession(): AuthSession {
        return authSessionStore.observeSession().first()
            ?: throw ApiException(
                statusCode = 401,
                message = "Please log in again"
            )
    }
}
