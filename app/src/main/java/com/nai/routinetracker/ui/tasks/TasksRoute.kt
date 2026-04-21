package com.nai.routinetracker.ui.tasks

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TasksRoute(
    viewModel: TaskViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    TasksScreen(
        state = uiState,
        onToggleTask = viewModel::onToggleTask,
        onCategorySelected = viewModel::onCategorySelected,
        onCompletedFilterChanged = viewModel::onCompletedFilterChanged
    )
}
