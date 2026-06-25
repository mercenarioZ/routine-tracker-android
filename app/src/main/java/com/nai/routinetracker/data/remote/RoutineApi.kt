package com.nai.routinetracker.data.remote

import com.nai.routinetracker.data.remote.dto.ApiResponseDto
import com.nai.routinetracker.data.remote.dto.RoutineCreateRequestDto
import com.nai.routinetracker.data.remote.dto.RoutineDto
import com.nai.routinetracker.data.remote.dto.RoutineQueryDto
import com.nai.routinetracker.data.remote.dto.jsonObjectOrNull
import com.nai.routinetracker.data.remote.service.RoutineService
import javax.inject.Inject

class RoutineApi @Inject constructor(
    private val service: RoutineService
) {
    suspend fun getRoutines(
        query: RoutineQueryDto,
    ): ApiResponseDto<List<RoutineDto>> {
        return service.getRoutines(
            queryParameters = query.toQueryParameters(),
        ).parseApiResponse(RoutineDto::listFromJsonValue)
    }

    suspend fun createRoutine(
        request: RoutineCreateRequestDto,
    ): ApiResponseDto<RoutineDto> {
        return service.createRoutine(
            body = request.toJson().toJsonRequestBody(),
        ).parseApiResponse { value ->
            value.jsonObjectOrNull()?.let(RoutineDto::fromJson)
        }
    }
}
