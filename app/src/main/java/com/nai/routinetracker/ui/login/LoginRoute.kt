package com.nai.routinetracker.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                LoginEffect.LoginSuccess -> onLoginSuccess()
            }
        }
    }

    LoginScreen(
        username = uiState.username,
        password = uiState.password,
        errorMessage = uiState.errorMessage,
        isLoading = uiState.isLoading,
        onUsernameChange = viewModel::onUsernameChange,
        onPasswordChange = viewModel::onPasswordChange,
        canSubmit = uiState.canSubmit,
        onLoginClick = viewModel::login
    )
}
