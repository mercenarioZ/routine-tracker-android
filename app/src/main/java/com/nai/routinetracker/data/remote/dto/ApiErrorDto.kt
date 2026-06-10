package com.nai.routinetracker.data.remote.dto

import org.json.JSONArray
import org.json.JSONObject

data class ApiErrorDto(
    val message: String?,
    val messageKey: String?,
    val path: String?,
    val status: Int?,
    val success: Boolean?,
    val timestamp: Long?,
    val errors: Map<String, List<String>>
) {
    val displayMessage: String?
        get() {
            val validationMessages = errors
                .flatMap { (_, messages) -> messages }
                .filter { it.isNotBlank() }

            return when {
                validationMessages.isNotEmpty() -> validationMessages.joinToString(separator = "\n")
                !message.isNullOrBlank() -> message
                else -> null
            }
        }

    companion object {
        fun fromJson(jsonString: String): ApiErrorDto {
            if (jsonString.isBlank()) return empty()

            return runCatching {
                val json = JSONObject(jsonString)
                ApiErrorDto(
                    message = json.optString("message")
                        .ifBlank { json.optString("error") }
                        .ifBlank { null },
                    messageKey = json.optStringOrNull("messageKey"),
                    path = json.optStringOrNull("path"),
                    status = json.optIntOrNull("status"),
                    success = json.optBooleanOrNull("success"),
                    timestamp = json.optLongOrNull("timestamp"),
                    errors = json.optJSONObject("errors").toValidationErrors()
                )
            }.getOrDefault(empty())
        }

        private fun empty(): ApiErrorDto {
            return ApiErrorDto(
                message = null,
                messageKey = null,
                path = null,
                status = null,
                success = null,
                timestamp = null,
                errors = emptyMap()
            )
        }
    }
}

private fun JSONObject?.toValidationErrors(): Map<String, List<String>> {
    if (this == null) return emptyMap()

    return buildMap {
        keys().forEach { key ->
            val value = opt(key)
            val messages = when (value) {
                is JSONArray -> value.toStringList()
                is String -> listOf(value)
                JSONObject.NULL, null -> emptyList()
                else -> listOf(value.toString())
            }
            put(key, messages)
        }
    }
}

private fun JSONArray.toStringList(): List<String> {
    return buildList {
        for (index in 0 until length()) {
            val value = opt(index)
            if (value != null && value != JSONObject.NULL) {
                add(value.toString())
            }
        }
    }
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
