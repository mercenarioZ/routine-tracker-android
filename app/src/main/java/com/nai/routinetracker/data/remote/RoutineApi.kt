package com.nai.routinetracker.data.remote

import com.nai.routinetracker.data.remote.dto.ApiResponseDto
import com.nai.routinetracker.data.remote.dto.RoutineDto
import com.nai.routinetracker.data.remote.dto.RoutineQueryDto
import javax.inject.Inject

class RoutineApi @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun getRoutines(
        query: RoutineQueryDto,
        authorizationHeader: String
    ): ApiResponseDto<List<RoutineDto>> {
        return ApiResponseDto.fromJson(
            apiClient.get(
                path = ApiRoutes.Routines.LIST,
                queryParameters = query.toQueryParameters(),
                authorizationHeader = authorizationHeader
            ),
            parseData = RoutineDto::listFromJsonValue
        )
    }
}
