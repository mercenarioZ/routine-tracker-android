package com.nai.routinetracker.data.remote

import com.nai.routinetracker.domain.session.AuthSession
import okhttp3.Request
import okhttp3.Response

internal const val AUTHORIZATION_HEADER = "Authorization"
internal const val HTTP_UNAUTHORIZED_CODE = 401

internal fun Request.isProtectedAuthRequest(): Boolean {
    val path = url.encodedPath

    return !path.endsWith("/${ApiRoutes.Auth.LOGIN}") &&
        !path.endsWith("/${ApiRoutes.Auth.REFRESH}")
}

internal fun Request.withAuthorizationHeader(session: AuthSession): Request {
    return newBuilder()
        .header(AUTHORIZATION_HEADER, session.authorizationHeader)
        .build()
}

internal val Response.responseCount: Int
    get() {
        var count = 1
        var prior = priorResponse
        while (prior != null) {
            count += 1
            prior = prior.priorResponse
        }

        return count
    }
