package com.nai.routinetracker.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nai.routinetracker.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<LoginEffect>()
    val effects = _effects.asSharedFlow()

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun login() {
        val state = _uiState.value
        if (!state.canSubmit) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            authRepository
                .login(
                    email = state.username.trim(),
                    password = state.password
                )
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    _effects.emit(LoginEffect.LoginSuccess)
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to login"
                        )
                    }
                }
        }
    }
}
