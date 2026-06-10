package com.nai.routinetracker.data.repository

import com.nai.routinetracker.data.remote.AuthApi
import com.nai.routinetracker.data.remote.dto.LoginRequestDto
import com.nai.routinetracker.domain.repository.AuthRepository
import com.nai.routinetracker.domain.session.AuthSession
import com.nai.routinetracker.domain.session.AuthSessionStore
import javax.inject.Inject

class RemoteAuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val authSessionStore: AuthSessionStore
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> {
        return runCatching {
            val response = authApi.login(
                LoginRequestDto(
                    email = email,
                    password = password
                )
            )
            if (response.success == false) {
                error(response.message ?: "Login failed")
            }

            val tokenData = response.data ?: error(response.message ?: "Login response did not include token data")
            val accessToken = tokenData.accessToken
                ?.takeIf { it.isNotBlank() }
                ?: error(response.message ?: "Login response did not include a JWT")
            val refreshToken = tokenData.refreshToken
                ?.takeIf { it.isNotBlank() }
                ?: error(response.message ?: "Login response did not include a refresh token")

            authSessionStore.saveSession(
                AuthSession(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    tokenType = tokenData.tokenType ?: "Bearer"
                )
            )
        }
    }
}
