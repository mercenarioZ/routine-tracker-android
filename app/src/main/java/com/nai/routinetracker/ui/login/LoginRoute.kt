package com.nai.routinetracker.ui.login

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LoginScreen(
        username = uiState.username,
        password = uiState.password,
        onUsernameChange = viewModel::onUsernameChange,
        onPasswordChange = viewModel::onPasswordChange,
        canSubmit = uiState.canSubmit,
        onLoginClick = {
            if (uiState.canSubmit) {
                onLoginSuccess()
            }
        }
    )
}
