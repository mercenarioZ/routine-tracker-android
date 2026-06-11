package com.nai.routinetracker.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

@Serializable
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

            val json = jsonString.parseJsonElementOrPrimitive().jsonObjectOrNull() ?: return empty()
            return ApiErrorDto(
                message = json.firstStringOrNull("message", "error"),
                messageKey = json.stringOrNull("messageKey"),
                path = json.stringOrNull("path"),
                status = json.intOrNull("status"),
                success = json.booleanOrNull("success"),
                timestamp = json.longOrNull("timestamp"),
                errors = json.elementOrNull("errors").toValidationErrors()
            )
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

private fun JsonElement?.toValidationErrors(): Map<String, List<String>> {
    val errors = jsonObjectOrNull() ?: return emptyMap()

    return buildMap {
        errors.forEach { (key, value) ->
            val messages = when (value) {
                is JsonNull -> emptyList()
                is JsonArray -> value.toStringList()
                is JsonPrimitive -> listOfNotNull(value.contentOrNull)
                else -> listOf(value.toString())
            }
            put(key, messages)
        }
    }
}

private fun JsonArray.toStringList(): List<String> {
    return buildList {
        this@toStringList.forEach { value ->
            when (value) {
                is JsonNull -> Unit
                is JsonPrimitive -> value.contentOrNull?.let(::add)
                else -> add(value.toString())
            }
        }
    }
}
