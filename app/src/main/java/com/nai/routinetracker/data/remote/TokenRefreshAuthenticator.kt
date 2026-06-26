package com.nai.routinetracker.data.remote

import com.nai.routinetracker.data.remote.dto.RefreshRequestDto
import com.nai.routinetracker.domain.session.AuthSession
import com.nai.routinetracker.domain.session.AuthSessionStore
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenRefreshAuthenticator @Inject constructor(
    private val authApi: AuthApi,
    private val authSessionStore: AuthSessionStore
) : Authenticator {
    private val refreshLock = Any()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != HTTP_UNAUTHORIZED_CODE || !response.request.isProtectedAuthRequest()) {
            return null
        }

        if (response.responseCount >= MAX_AUTH_ATTEMPTS) {
            runBlocking { authSessionStore.clearSession() }
            return null
        }

        val failedAuthorizationHeader = response.request.header(AUTHORIZATION_HEADER)

        return synchronized(refreshLock) {
            val currentSession = runBlocking {
                authSessionStore.observeSession().first()
            } ?: return@synchronized null

            if (failedAuthorizationHeader != currentSession.authorizationHeader) {
                return@synchronized response.request.withAuthorizationHeader(currentSession)
            }

            val refreshedSession = runBlocking {
                refreshSession(currentSession)
            }

            if (refreshedSession == null) {
                runBlocking { authSessionStore.clearSession() }
                null
            } else {
                response.request.withAuthorizationHeader(refreshedSession)
            }
        }
    }

    private suspend fun refreshSession(session: AuthSession): AuthSession? {
        return runCatching {
            val response = authApi.refresh(
                RefreshRequestDto(refreshToken = session.refreshToken)
            )

            if (response.success == false) {
                return@runCatching null
            }

            val tokenData = response.data ?: return@runCatching null
            val accessToken = tokenData.accessToken
                ?.takeIf { it.isNotBlank() }
                ?: return@runCatching null
            val refreshToken = tokenData.refreshToken
                ?.takeIf { it.isNotBlank() }
                ?: return@runCatching null

            AuthSession(
                accessToken = accessToken,
                refreshToken = refreshToken,
                tokenType = tokenData.tokenType ?: session.tokenType
            ).also { refreshedSession ->
                authSessionStore.saveSession(refreshedSession)
            }
        }.getOrNull()
    }

    private companion object {
        const val MAX_AUTH_ATTEMPTS = 2
    }
}
