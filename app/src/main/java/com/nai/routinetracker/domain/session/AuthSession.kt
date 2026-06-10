package com.nai.routinetracker.domain.session

data class AuthSession(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String
) {
    val authorizationHeader: String
        get() = "$tokenType $accessToken"
}
