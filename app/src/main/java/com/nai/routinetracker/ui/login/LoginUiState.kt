package com.nai.routinetracker.ui.login

data class LoginUiState(
    val username: String = "",
    val password: String = ""
) {
    val canSubmit: Boolean
        get() = username == "test@example.com" && password == "password"
}
