package com.nai.routinetracker.data.repository

import android.content.Context
import com.nai.routinetracker.domain.repository.TaskRepository
import com.nai.routinetracker.model.TaskItem
import com.nai.routinetracker.model.next
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class FakeTaskRepository @Inject constructor(
    @ApplicationContext context: Context
) : TaskRepository {
    private val appContext = context.applicationContext
    private val tasksState = MutableStateFlow(
        buildSampleTasks(
            context = appContext,
            routines = buildSampleRoutines(appContext)
        )
    )

    override fun observeTasks(): Flow<List<TaskItem>> = tasksState.asStateFlow()

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
