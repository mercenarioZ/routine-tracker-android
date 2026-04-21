package com.nai.routinetracker.ui.routines

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RoutinesRoute(
    viewModel: RoutineViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    RoutinesScreen(
        state = uiState,
        onCategorySelected = viewModel::onCategorySelected
    )
}
