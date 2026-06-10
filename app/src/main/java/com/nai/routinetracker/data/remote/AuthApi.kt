package com.nai.routinetracker.data.remote

import com.nai.routinetracker.data.remote.dto.ApiResponseDto
import com.nai.routinetracker.data.remote.dto.LoginRequestDto
import com.nai.routinetracker.data.remote.dto.LoginTokenDto
import javax.inject.Inject

class AuthApi @Inject constructor(
    private val apiClient: ApiClient
) {
    suspend fun login(request: LoginRequestDto): ApiResponseDto<LoginTokenDto> {
        return ApiResponseDto.fromJson(
            apiClient.postJson(
                path = ApiRoutes.Auth.LOGIN,
                body = request.toJson()
            ),
            parseData = LoginTokenDto::fromJsonValue
        )
    }
}
