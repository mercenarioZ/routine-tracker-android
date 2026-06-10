package com.nai.routinetracker.data.remote

import java.net.URL

object ApiConfig {
    private const val BASE_URL = "http://10.0.2.2:8000"
    private const val API_PREFIX = "/v1/api"

    const val TIMEOUT_MILLIS = 15_000

    fun endpoint(path: String): URL {
        val normalizedPath = if (path.startsWith("/")) path else "/$path"
        return URL("$BASE_URL$API_PREFIX$normalizedPath")
    }
}

object ApiRoutes {
    object Auth {
        const val LOGIN = "/auth/login"
    }
}
