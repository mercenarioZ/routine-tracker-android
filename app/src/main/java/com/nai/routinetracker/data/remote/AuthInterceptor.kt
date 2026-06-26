package com.nai.routinetracker.data.remote

import com.nai.routinetracker.domain.session.AuthSessionStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authSessionStore: AuthSessionStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val session = runBlocking {
            authSessionStore.observeSession().first()
        }

        val request = if (
            session != null && originalRequest.header(AUTHORIZATION_HEADER) == null &&
            originalRequest.isProtectedAuthRequest()
        ) {
            originalRequest.withAuthorizationHeader(session)
        } else {
            originalRequest
        }

        return chain.proceed(request)
    }
}
