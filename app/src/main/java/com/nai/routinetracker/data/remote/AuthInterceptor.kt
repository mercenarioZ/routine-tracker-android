package com.nai.routinetracker.data.remote

import com.nai.routinetracker.domain.session.AuthSessionStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

private const val AUTHORIZATION_HEADER = "Authorization"
private const val HTTP_UNAUTHORIZED_CODE = 401

class AuthInterceptor @Inject constructor(
    private val authSessionStore: AuthSessionStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val session = runBlocking {
            authSessionStore.observeSession().first()
        }

        val req = if (session != null && originalRequest.header(AUTHORIZATION_HEADER) == null) {
            originalRequest.newBuilder()
                .header(AUTHORIZATION_HEADER, session.authorizationHeader)
                .build()
        } else {
            originalRequest
        }

        val res = chain.proceed(req)

        if (res.code == HTTP_UNAUTHORIZED_CODE && originalRequest.isProtectedRequest()) {
            runBlocking {
                authSessionStore.clearSession()
            }
        }

        return res
    }

    private fun okhttp3.Request.isProtectedRequest(): Boolean {
        return !url.encodedPath.endsWith("/${ApiRoutes.Auth.LOGIN}")
    }
}