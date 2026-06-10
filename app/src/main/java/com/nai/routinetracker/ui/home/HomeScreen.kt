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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nai.routinetracker.R
import com.nai.routinetracker.model.RoutineCategories
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.TaskCategories
import com.nai.routinetracker.model.TaskItem
import com.nai.routinetracker.model.TaskStatus
import com.nai.routinetracker.ui.home.components.HeaderSection
import com.nai.routinetracker.ui.home.components.MetricsRow
import com.nai.routinetracker.ui.home.components.OverviewCard
import com.nai.routinetracker.ui.home.components.RoutineCard
import com.nai.routinetracker.ui.tasks.components.TaskCard
import com.nai.routinetracker.ui.theme.RoutineTrackerTheme

@Composable
fun HomeScreen(
    state: HomeUiState,
    onToggleTask: (String) -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (state.isLoading && state.routines.isEmpty() && state.tasks.isEmpty()) {
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
            contentPadding = PaddingValues(
                start = 20.dp,
                top = 20.dp,
                end = 20.dp,
                bottom = 28.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HeaderSection(
                    state = state,
                    onLogoutClick = onLogoutClick
                )
            }

            item {
                OverviewCard(state = state)
            }

            item {
                MetricsRow(state = state)
            }

            item {
                SectionTitle(text = stringResource(R.string.home_active_routines_title))
            }

            if (state.activeRoutines.isEmpty()) {
                item {
                    EmptySectionText(text = stringResource(R.string.home_overview_empty_body))
                }
            } else {
                items(state.activeRoutines, key = { it.id }) { routine ->
                    RoutineCard(
                        routine = routine,
                        modifier = Modifier.animateItem()
                    )
                }
            }

            item {
                SectionTitle(text = stringResource(R.string.home_routines_title))
            }

            if (state.orderedTasks.isEmpty()) {
                item {
                    EmptySectionText(text = stringResource(R.string.home_tasks_empty_state))
                }
            } else {
                items(state.orderedTasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onToggleTask = onToggleTask,
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun EmptySectionText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun HomeScreenPreview() {
    RoutineTrackerTheme {
        HomeScreen(
            state = HomeUiState(
                userName = "Nai",
                dateLabel = "Wednesday, April 29",
                routines = listOf(
                    RoutineItem(
                        id = "hydration",
                        title = "Hydrate and stretch",
                        scheduleLabel = "DAILY",
                        category = RoutineCategories.Health,
                        streakDays = 12,
                        description = "Drink water, stretch for five minutes, and wake the body up."
                    ),
                    RoutineItem(
                        id = "planning",
                        title = "Plan top 3 tasks",
                        scheduleLabel = "DAILY",
                        category = RoutineCategories.Planning,
                        streakDays = 8,
                        description = "Write the three outcomes that must happen today."
                    )
                ),
                tasks = listOf(
                    TaskItem(
                        id = "task-hydration",
                        routineId = "hydration",
                        title = "Hydration",
                        timeLabel = "09:00",
                        dueLabel = "Today",
                        category = TaskCategories.Personal,
                        status = TaskStatus.Done,
                        description = "Drink a glass of water before deep work."
                    )
                )
            ),
            onToggleTask = {},
            onLogoutClick = {}
        )
    }
}
