package com.nai.routinetracker.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nai.routinetracker.domain.session.AuthSessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authSessionStore: AuthSessionStore
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeSession()
    }

    fun logout() {
        viewModelScope.launch {
            authSessionStore.clearSession()
        }
    }

    private fun observeSession() {
        viewModelScope.launch {
            authSessionStore.observeSession().collectLatest { session ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoggedIn = session != null
                    )
                }
            }
        }
    }
}
