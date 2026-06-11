package com.nai.routinetracker.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class LoginTokenDto(
    val accessToken: String?,
    val refreshToken: String?,
    val tokenType: String?
) {
    companion object {
        fun fromJsonValue(value: JsonElement?): LoginTokenDto? {
            val json = value.jsonObjectOrNull() ?: return null
            val token = RemoteJson.decodeFromJsonElement<LoginTokenDto>(json)
            return token.copy(
                accessToken = token.accessToken?.takeIf { it.isNotBlank() },
                refreshToken = token.refreshToken?.takeIf { it.isNotBlank() },
                tokenType = token.tokenType?.takeIf { it.isNotBlank() }
            )
        }
    }
}
