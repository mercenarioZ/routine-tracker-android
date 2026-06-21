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
    suspend fun getTasks(
        authorizationHeader: String
    ): ApiResponseDto<List<TaskDto>> {
        return service.getTasks(
            authorizationHeader = authorizationHeader
        ).parseApiResponse(TaskDto::listFromJsonValue)
    }

    suspend fun createTask(
        request: TaskCreateRequestDto,
        authorizationHeader: String
    ): ApiResponseDto<TaskDto> {
        return service.createTask(
            body = request.toJson().toJsonRequestBody(),
            authorizationHeader = authorizationHeader
        ).parseApiResponse { value ->
            value.jsonObjectOrNull()?.let(TaskDto::fromJson)
        }
    }

    suspend fun updateTaskCompletion(
        taskId: String,
        request: TaskUpdateRequestDto,
        authorizationHeader: String
    ): ApiResponseDto<TaskDto> {
        return service.updateTaskCompletion(
            taskId = taskId,
            body = request.toJson().toJsonRequestBody(),
            authorizationHeader = authorizationHeader
        ).parseApiResponse { value ->
            value.jsonObjectOrNull()?.let(TaskDto::fromJson)
        }
    }
}
