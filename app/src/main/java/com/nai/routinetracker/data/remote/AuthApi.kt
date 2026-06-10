package com.nai.routinetracker.data.remote

import com.nai.routinetracker.data.remote.dto.ApiErrorDto
import com.nai.routinetracker.data.remote.dto.LoginRequestDto
import com.nai.routinetracker.data.remote.dto.LoginResponseDto
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthApi @Inject constructor() {
    suspend fun login(request: LoginRequestDto): LoginResponseDto =
        withContext(Dispatchers.IO) {
            val connection =
                (ApiConfig.endpoint(ApiRoutes.Auth.LOGIN).openConnection() as HttpURLConnection)
                    .apply {
                        requestMethod = "POST"
                        connectTimeout = ApiConfig.TIMEOUT_MILLIS
                        readTimeout = ApiConfig.TIMEOUT_MILLIS
                        doOutput = true
                        setRequestProperty("Accept", "application/json")
                        setRequestProperty("Content-Type", "application/json")
                    }

            try {
                connection.outputStream.use { outputStream ->
                    outputStream.write(request.toJson().toByteArray(Charsets.UTF_8))
                }

                val responseCode = connection.responseCode
                val responseBody = readResponseBody(connection)
                if (responseCode in 200..299) {
                    LoginResponseDto.fromJson(responseBody)
                } else {
                    val errorMessage = ApiErrorDto.fromJson(responseBody).message
                        ?: "Login failed with HTTP $responseCode"
                    throw ApiException(statusCode = responseCode, message = errorMessage)
                }
            } finally {
                connection.disconnect()
            }
        }

    private fun readResponseBody(connection: HttpURLConnection): String {
        val responseStream = if (connection.responseCode in 200..299) {
            connection.inputStream
        } else {
            connection.errorStream ?: throw IOException("Login failed with HTTP ${connection.responseCode}")
        }

        return responseStream.bufferedReader().use { it.readText() }
    }
}
