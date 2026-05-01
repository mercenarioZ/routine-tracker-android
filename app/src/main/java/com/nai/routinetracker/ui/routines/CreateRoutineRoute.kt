package com.nai.routinetracker.ui.routines

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CreateRoutineRoute(
    onRoutineCreated: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: RoutineViewModel = hiltViewModel()
) {
    val uiState = viewModel.createUiState.collectAsStateWithLifecycle().value

    CreateRoutineScreen(
        state = uiState,
        onTitleChanged = viewModel::onCreateTitleChanged,
        onScheduleChanged = viewModel::onCreateScheduleChanged,
        onDescriptionChanged = viewModel::onCreateDescriptionChanged,
        onCategorySelected = viewModel::onCreateCategorySelected,
        onSaveClick = { viewModel.createRoutine(onRoutineCreated) },
        onBackClick = onBackClick
    )
}
