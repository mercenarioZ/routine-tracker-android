package com.nai.routinetracker.data.remote.dto

import org.json.JSONObject

data class LoginRequestDto(
    val email: String,
    val password: String
) {
    fun toJson(): String {
        return JSONObject()
            .put("email", email)
            .put("password", password)
            .toString()
    }
}
