package com.nai.routinetracker.data.remote

import java.net.URLEncoder
import java.net.URL

object ApiConfig {
    private const val BASE_URL = "http://10.0.2.2:8000"
    private const val API_PREFIX = "/v1/api"

    const val TIMEOUT_MILLIS = 15_000

    fun endpoint(
        path: String,
        queryParameters: Map<String, String?> = emptyMap()
    ): URL {
        val normalizedPath = if (path.startsWith("/")) path else "/$path"
        val query = queryParameters
            .filterValues { !it.isNullOrBlank() }
            .map { (name, value) ->
                "${name.urlEncoded()}=${value.orEmpty().urlEncoded()}"
            }
            .joinToString(separator = "&")
            .takeIf { it.isNotBlank() }
            ?.let { "?$it" }
            .orEmpty()

        return URL("$BASE_URL$API_PREFIX$normalizedPath$query")
    }

    private fun String.urlEncoded(): String {
        return URLEncoder.encode(this, "UTF-8")
    }
}

object ApiRoutes {
    object Auth {
        const val LOGIN = "/auth/login"
    }

    object Routines {
        const val LIST = "/routines"
    }
}
