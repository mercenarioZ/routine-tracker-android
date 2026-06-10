package com.nai.routinetracker.data.remote.dto

import org.json.JSONObject

data class LoginResponseDto(
    val data: LoginTokenDto?,
    val message: String?,
    val messageKey: String?,
    val path: String?,
    val status: Int?,
    val success: Boolean?,
    val timestamp: Long?
) {
    companion object {
        fun fromJson(jsonString: String): LoginResponseDto {
            if (jsonString.isBlank()) {
                return LoginResponseDto(
                    data = null,
                    message = null,
                    messageKey = null,
                    path = null,
                    status = null,
                    success = null,
                    timestamp = null
                )
            }

            val json = JSONObject(jsonString)
            return LoginResponseDto(
                data = json.optJSONObject("data")?.let(LoginTokenDto::fromJson),
                message = json.optString("message").ifBlank { null },
                messageKey = json.optString("messageKey").ifBlank { null },
                path = json.optString("path").ifBlank { null },
                status = json.optIntOrNull("status"),
                success = json.optBooleanOrNull("success"),
                timestamp = json.optLongOrNull("timestamp")
            )
        }
    }
}

data class LoginTokenDto(
    val accessToken: String?,
    val refreshToken: String?,
    val tokenType: String?
) {
    companion object {
        fun fromJson(json: JSONObject): LoginTokenDto {
            return LoginTokenDto(
                accessToken = json.optString("accessToken").ifBlank { null },
                refreshToken = json.optString("refreshToken").ifBlank { null },
                tokenType = json.optString("tokenType").ifBlank { null }
            )
        }
    }
}

private fun JSONObject.optIntOrNull(name: String): Int? {
    return if (has(name) && !isNull(name)) optInt(name) else null
}

private fun JSONObject.optLongOrNull(name: String): Long? {
    return if (has(name) && !isNull(name)) optLong(name) else null
}

private fun JSONObject.optBooleanOrNull(name: String): Boolean? {
    return if (has(name) && !isNull(name)) optBoolean(name) else null
}
