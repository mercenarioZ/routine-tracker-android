package com.nai.routinetracker.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier

@Composable
fun SettingsRoute(
    onSignOutClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    Scaffold { innerPadding ->
        SettingsScreen(
            state = uiState,
            onReminderEnabledChanged = viewModel::onReminderEnabledChanged,
            onReminderTimeChanged = viewModel::onReminderTimeChanged,
            onThemeModeChanged = viewModel::onThemeModeChanged,
            onSignOutClick = onSignOutClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
