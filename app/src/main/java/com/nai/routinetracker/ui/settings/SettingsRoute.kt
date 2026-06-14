package com.nai.routinetracker.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.Modifier
import com.nai.routinetracker.R

@Composable
fun SettingsRoute(
    onSignOutClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.resetMessage) {
        when (uiState.resetMessage) {
            SettingsResetMessage.Success -> {
                snackbarHostState.showSnackbar(
                    context.getString(R.string.settings_reset_success)
                )
                viewModel.onResetMessageConsumed()
            }
            SettingsResetMessage.Error -> {
                snackbarHostState.showSnackbar(
                    context.getString(R.string.settings_reset_error)
                )
                viewModel.onResetMessageConsumed()
            }
            null -> Unit
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        SettingsScreen(
            state = uiState,
            onReminderEnabledChanged = viewModel::onReminderEnabledChanged,
            onReminderTimeChanged = viewModel::onReminderTimeChanged,
            onResetLocalDataConfirmed = viewModel::onResetLocalDataConfirmed,
            onSignOutClick = onSignOutClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
