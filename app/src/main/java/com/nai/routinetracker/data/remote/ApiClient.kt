package com.nai.routinetracker.data.remote

import com.nai.routinetracker.data.remote.dto.ApiErrorDto
import java.net.HttpURLConnection
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiClient @Inject constructor() {
    suspend fun get(
        path: String,
        queryParameters: Map<String, String?> = emptyMap(),
        authorizationHeader: String? = null
    ): String {
        return execute(
            path = path,
            method = "GET",
            queryParameters = queryParameters,
            authorizationHeader = authorizationHeader
        )
    }

    suspend fun postJson(
        path: String,
        body: String,
        authorizationHeader: String? = null
    ): String {
        return execute(
            path = path,
            method = "POST",
            body = body,
            authorizationHeader = authorizationHeader
        )
    }

    private suspend fun execute(
        path: String,
        method: String,
        queryParameters: Map<String, String?> = emptyMap(),
        body: String? = null,
        authorizationHeader: String? = null
    ): String = withContext(Dispatchers.IO) {
        val connection =
            (ApiConfig.endpoint(path, queryParameters).openConnection() as HttpURLConnection)
                .apply {
                    requestMethod = method
                    connectTimeout = ApiConfig.TIMEOUT_MILLIS
                    readTimeout = ApiConfig.TIMEOUT_MILLIS
                    doInput = true
                    setRequestProperty("Accept", "*/*")
                    authorizationHeader?.let {
                        setRequestProperty("Authorization", it)
                    }
                    if (body != null) {
                        doOutput = true
                        setRequestProperty("Content-Type", "application/json")
                    }
                }

        try {
            if (body != null) {
                connection.outputStream.use { outputStream ->
                    outputStream.write(body.toByteArray(Charsets.UTF_8))
                }
            }

            val responseCode = connection.responseCode
            val responseBody = readResponseBody(connection, responseCode)
            if (responseCode in 200..299) {
                responseBody
            } else {
                val errorMessage = ApiErrorDto.fromJson(responseBody).displayMessage
                    ?: "Request failed with HTTP $responseCode"
                throw ApiException(statusCode = responseCode, message = errorMessage)
            }
        } finally {
            connection.disconnect()
        }
    }

    private fun readResponseBody(
        connection: HttpURLConnection,
        responseCode: Int
    ): String {
        val stream = if (responseCode in 200..299) {
            connection.inputStream
        } else {
            connection.errorStream
        } ?: return ""

        return stream.bufferedReader().use { it.readText() }
    }
}
