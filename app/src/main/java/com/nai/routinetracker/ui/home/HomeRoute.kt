package com.nai.routinetracker.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nai.routinetracker.R
import com.nai.routinetracker.model.TaskStatus
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeRoute(
    onLogoutClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is HomeEffect.ShowTaskStatusChanged -> {
                    val message = context.getString(
                        when (effect.newStatus) {
                            TaskStatus.Pending -> R.string.home_task_marked_pending
                            TaskStatus.InProgress -> R.string.home_task_marked_in_progress
                            TaskStatus.Done -> R.string.home_task_marked_done
                        },
                        effect.taskTitle
                    )
                    snackBarHostState.showSnackbar(message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { innerPadding ->
        HomeScreen(
            state = uiState,
            onToggleTask = viewModel::onToggleTask,
            onLogoutClick = onLogoutClick,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
