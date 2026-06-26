package com.nai.routinetracker.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

@Serializable
data class RefreshRequestDto(
    val refreshToken: String
) {
    fun toJson(): String {
        return RemoteJson.encodeToString(this)
    }
}
