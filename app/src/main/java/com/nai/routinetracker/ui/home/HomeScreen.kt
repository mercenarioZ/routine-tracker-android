package com.nai.routinetracker.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nai.routinetracker.R
import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.ui.home.components.HeaderSection
import com.nai.routinetracker.ui.home.components.MetricsRow
import com.nai.routinetracker.ui.home.components.OverviewCard
import com.nai.routinetracker.ui.home.components.RoutineCard
import com.nai.routinetracker.ui.theme.RoutineTrackerTheme

@Composable
fun HomeScreen(
    state: HomeUiState,
    onToggleRoutine: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (state.isLoading && state.routines.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Surface
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HeaderSection(state = state)
            }

            item {
                OverviewCard(state = state)
            }

            item {
                MetricsRow(state = state)
            }

            item {
                Text(
                    text = stringResource(R.string.home_routines_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(state.routines, key = { it.id }) { routine ->
                RoutineCard(
                    routine = routine,
                    onToggleRoutine = onToggleRoutine
                )
            }
        }
    }
}

@Preview(name = "Home", device = Devices.PIXEL_7, showBackground = true, showSystemUi = false)
@Composable
private fun HomeScreenPreview() {
    RoutineTrackerTheme {
        HomeScreen(
            state = previewHomeUiState(),
            onToggleRoutine = {}
        )
    }
}

private fun previewHomeUiState(): HomeUiState {
    return HomeUiState(
        isLoading = false,
        userName = "Builder",
        dateLabel = "Friday, March 20",
        highlight = "Small routines create stable days.",
        routines = listOf(
            RoutineItem(
                id = "hydration",
                title = "Hydrate and stretch",
                scheduleLabel = "06:30 AM",
                category = RoutineCategory.Health,
                streakDays = 12,
                completed = true,
                description = "Drink water and stretch for five minutes."
            ),
            RoutineItem(
                id = "planning",
                title = "Plan top 3 tasks",
                scheduleLabel = "07:00 AM",
                category = RoutineCategory.Planning,
                streakDays = 8,
                completed = false,
                description = "Write the three outcomes that must happen today."
            )
        )
    )
}
