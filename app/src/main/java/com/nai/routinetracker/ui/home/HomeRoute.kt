package com.nai.routinetracker.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nai.routinetracker.ui.home.preview.HomePreviewData
import com.nai.routinetracker.ui.theme.RoutineTrackerTheme

@Composable
fun HomeRoute(viewModel: HomeViewModel) {
    HomeScreen(
        state = viewModel.uiState,
        onToggleRoutine = viewModel::onRoutineToggle
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeRoutePreview() {
    RoutineTrackerTheme {
        HomeScreen(
            state = HomePreviewData.dashboardState,
            onToggleRoutine = {}
        )
    }
}
