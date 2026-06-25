package com.nai.routinetracker.data.remote

import com.nai.routinetracker.data.remote.dto.ApiResponseDto
import com.nai.routinetracker.data.remote.dto.TaskCreateRequestDto
import com.nai.routinetracker.data.remote.dto.TaskDto
import com.nai.routinetracker.data.remote.dto.TaskUpdateRequestDto
import com.nai.routinetracker.data.remote.dto.jsonObjectOrNull
import com.nai.routinetracker.data.remote.service.TaskService
import javax.inject.Inject

class TaskApi @Inject constructor(
    private val service: TaskService
) {
    suspend fun getTasks(): ApiResponseDto<List<TaskDto>> {
        return service.getTasks().parseApiResponse(TaskDto::listFromJsonValue)
    }

    suspend fun createTask(
        request: TaskCreateRequestDto,
    ): ApiResponseDto<TaskDto> {
        return service.createTask(
            body = request.toJson().toJsonRequestBody()).parseApiResponse { value ->
            value.jsonObjectOrNull()?.let(TaskDto::fromJson)
        }
    }

    suspend fun updateTaskCompletion(
        taskId: String,
        request: TaskUpdateRequestDto,
    ): ApiResponseDto<TaskDto> {
        return service.updateTaskCompletion(
            taskId = taskId,
            body = request.toJson().toJsonRequestBody()).parseApiResponse { value ->
            value.jsonObjectOrNull()?.let(TaskDto::fromJson)
        }
    }
}
