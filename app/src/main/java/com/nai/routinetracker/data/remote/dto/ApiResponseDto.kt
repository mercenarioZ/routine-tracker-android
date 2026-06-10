package com.nai.routinetracker.data.remote.dto

import org.json.JSONArray
import org.json.JSONObject

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
            parseData: (Any?) -> T?
        ): ApiResponseDto<T> {
            if (jsonString.isBlank()) {
                return empty()
            }

            val trimmedJson = jsonString.trim()
            if (!trimmedJson.startsWith("{")) {
                return empty(data = parseData(trimmedJson.toJsonValue()))
            }

            val json = JSONObject(trimmedJson)
            return ApiResponseDto(
                data = parseData(json.optNullable("data")),
                message = json.optStringOrNull("message"),
                messageKey = json.optStringOrNull("messageKey"),
                path = json.optStringOrNull("path"),
                status = json.optIntOrNull("status"),
                success = json.optBooleanOrNull("success"),
                timestamp = json.optLongOrNull("timestamp")
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

private fun String.toJsonValue(): Any? {
    return when {
        startsWith("[") -> JSONArray(this)
        startsWith("{") -> JSONObject(this)
        else -> this
    }
}

private fun JSONObject.optNullable(name: String): Any? {
    return if (has(name) && !isNull(name)) opt(name) else null
}

private fun JSONObject.optStringOrNull(name: String): String? {
    return if (has(name) && !isNull(name)) optString(name).ifBlank { null } else null
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
