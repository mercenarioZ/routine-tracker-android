package com.nai.routinetracker.ui.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val canSubmit: Boolean
        get() = username.isNotBlank() && password.isNotBlank() && !isLoading
}
