package com.nai.routinetracker.data.remote

import com.nai.routinetracker.data.remote.dto.ApiResponseDto
import com.nai.routinetracker.data.remote.dto.LoginRequestDto
import com.nai.routinetracker.data.remote.dto.LoginTokenDto
import com.nai.routinetracker.data.remote.dto.RefreshRequestDto
import com.nai.routinetracker.data.remote.service.AuthService
import javax.inject.Inject

class AuthApi @Inject constructor(
    private val service: AuthService
) {
    suspend fun login(request: LoginRequestDto): ApiResponseDto<LoginTokenDto> {
        return service.login(request.toJson().toJsonRequestBody())
            .parseApiResponse(LoginTokenDto::fromJsonValue)
    }

    suspend fun refresh(request: RefreshRequestDto): ApiResponseDto<LoginTokenDto> {
        return service.refresh(request.toJson().toJsonRequestBody())
            .parseApiResponse(LoginTokenDto::fromJsonValue)
    }
}
