package com.nai.routinetracker.data.repository

import com.nai.routinetracker.domain.repository.AuthRepository
import com.nai.routinetracker.domain.session.AuthSession
import com.nai.routinetracker.domain.session.AuthSessionStore
import javax.inject.Inject

class FakeAuthRepository @Inject constructor(
    private val authSessionStore: AuthSessionStore
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> {
        return runCatching {
            require(email.isNotBlank()) { "Email is required" }
            require(password.isNotBlank()) { "Password is required" }

            authSessionStore.saveSession(
                AuthSession(
                    accessToken = FAKE_ACCESS_TOKEN,
                    refreshToken = FAKE_REFRESH_TOKEN,
                    tokenType = "Bearer"
                )
            )
        }
    }

    private companion object {
        const val FAKE_ACCESS_TOKEN = "fake-access-token"
        const val FAKE_REFRESH_TOKEN = "fake-refresh-token"
    }
}
