package com.nai.routinetracker.data.remote.dto

import org.json.JSONObject

data class ApiErrorDto(
    val message: String?
) {
    companion object {
        fun fromJson(jsonString: String): ApiErrorDto {
            if (jsonString.isBlank()) return ApiErrorDto(message = null)

            return runCatching {
                val json = JSONObject(jsonString)
                ApiErrorDto(
                    message = json.optString("message")
                        .ifBlank { json.optString("error") }
                        .ifBlank { null }
                )
            }.getOrDefault(ApiErrorDto(message = null))
        }
    }
}
