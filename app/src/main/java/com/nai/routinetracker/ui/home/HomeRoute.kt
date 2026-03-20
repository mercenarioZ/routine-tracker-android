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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nai.routinetracker.R
import com.nai.routinetracker.ui.theme.RoutineTrackerTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(viewModel.effects) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is HomeEffect.ShowRoutineStatusChanged -> {
                    val message = context.getString(
                        if (effect.isCompleted) {
                            R.string.home_routine_marked_done
                        } else {
                            R.string.home_routine_marked_pending
                        },
                        effect.routineTitle
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
            onToggleRoutine = viewModel::onToggleRoutine,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

