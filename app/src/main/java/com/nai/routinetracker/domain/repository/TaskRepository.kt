package com.nai.routinetracker.domain.repository

import com.nai.routinetracker.model.TaskItem
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeTasks(): Flow<List<TaskItem>>

    suspend fun toggleTask(taskId: String)
}
