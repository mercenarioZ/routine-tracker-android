package com.nai.routinetracker.ui.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())

    private val mockUsername = "test@example.com"
    private val mockPassword = "password"
    val uiState = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }
    fun login(): Boolean {
        val state = _uiState.value
        val isValid = state.username == mockUsername && state.password == mockPassword

        _uiState.update {
            it.copy(
                errorMessage = if (isValid) null else "Invalid username or password"
            )
        }

        return isValid
    }
}
