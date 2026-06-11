package com.nai.routinetracker.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ApiResponseDto<T>(
    val data: T?,
    val message: String?,
    val messageKey: String?,
    val path: String?,
    val status: Int?,
    val success: Boolean?,
    val timestamp: Long?
) {
    companion object {
        fun <T> fromJson(
            jsonString: String,
            parseData: (JsonElement?) -> T?
        ): ApiResponseDto<T> {
            if (jsonString.isBlank()) {
                return empty()
            }

            val trimmedJson = jsonString.trim()
            val jsonElement = trimmedJson.parseJsonElementOrPrimitive()
            val json = jsonElement.jsonObjectOrNull()
            if (json == null) {
                return empty(data = parseData(jsonElement))
            }

            return ApiResponseDto(
                data = parseData(json.elementOrNull("data")),
                message = json.stringOrNull("message"),
                messageKey = json.stringOrNull("messageKey"),
                path = json.stringOrNull("path"),
                status = json.intOrNull("status"),
                success = json.booleanOrNull("success"),
                timestamp = json.longOrNull("timestamp")
            )
        }

        private fun <T> empty(data: T? = null): ApiResponseDto<T> {
            return ApiResponseDto(
                data = data,
                message = null,
                messageKey = null,
                path = null,
                status = null,
                success = null,
                timestamp = null
            )
        }
    }
}
