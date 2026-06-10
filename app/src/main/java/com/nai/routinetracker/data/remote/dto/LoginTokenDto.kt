package com.nai.routinetracker.data.remote.dto

import org.json.JSONObject

data class LoginTokenDto(
    val accessToken: String?,
    val refreshToken: String?,
    val tokenType: String?
) {
    companion object {
        fun fromJsonValue(value: Any?): LoginTokenDto? {
            return when (value) {
                is JSONObject -> LoginTokenDto(
                    accessToken = value.optString("accessToken").ifBlank { null },
                    refreshToken = value.optString("refreshToken").ifBlank { null },
                    tokenType = value.optString("tokenType").ifBlank { null }
                )
                else -> null
            }
        }
    }
}
