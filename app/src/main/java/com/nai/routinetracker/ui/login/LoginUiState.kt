package com.nai.routinetracker.ui.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val errorMessage: String? = null
) {
    val canSubmit: Boolean
        get() = username.isNotBlank() && password.isNotBlank()
}
