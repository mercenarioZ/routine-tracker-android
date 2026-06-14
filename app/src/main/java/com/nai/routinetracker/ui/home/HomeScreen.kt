package com.nai.routinetracker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
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
import com.nai.routinetracker.ui.theme.RoutineTrackerTheme
import com.nai.routinetracker.ui.theme.RoutineVisualDefaults

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
                start = RoutineVisualDefaults.ScreenHorizontalPadding,
                top = 20.dp,
                end = RoutineVisualDefaults.ScreenHorizontalPadding,
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
                SectionHeader(
                    title = stringResource(R.string.home_active_routines_title),
                    count = state.activeRoutines.size
                )
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
                SectionHeader(
                    title = stringResource(R.string.home_routines_title),
                    count = state.orderedTasks.size
                )
            }

            if (state.orderedTasks.isEmpty()) {
                item {
                    EmptySectionText(text = stringResource(R.string.home_tasks_empty_state))
                }
            } else {
                items(state.orderedTasks, key = { it.id }) { task ->
                    HomeTaskRow(
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
private fun SectionHeader(
    title: String,
    count: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Surface(
            shape = RoutineVisualDefaults.PillShape,
            color = MaterialTheme.colorScheme.tertiaryContainer
        ) {
            Text(
                text = count.toString(),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

@Composable
private fun EmptySectionText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun HomeTaskRow(
    task: TaskItem,
    onToggleTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (task.status == TaskStatus.Done) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoutineVisualDefaults.CardShape,
        border = RoutineVisualDefaults.cardBorder(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.status == TaskStatus.Done) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.74f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .clip(RoutineVisualDefaults.PillShape)
                    .background(RoutineVisualDefaults.categoryAccent(task.category.id))
            )

            IconButton(
                onClick = { onToggleTask(task.id) },
                modifier = Modifier.size(42.dp)
            ) {
                Icon(
                    imageVector = if (task.status == TaskStatus.Done) {
                        Icons.Outlined.CheckCircle
                    } else {
                        Icons.Outlined.RadioButtonUnchecked
                    },
                    contentDescription = stringResource(
                        if (task.status == TaskStatus.Done) {
                            R.string.home_task_mark_pending
                        } else {
                            R.string.home_task_mark_done
                        }
                    ),
                    tint = if (task.status == TaskStatus.Done) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (task.status == TaskStatus.Done) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )
                Row(
                    modifier = Modifier.heightIn(min = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.EventNote,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${task.dueLabel} • ${task.timeLabel}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Surface(
                shape = CircleShape,
                color = RoutineVisualDefaults.categoryAccent(task.category.id)
            ) {
                Text(
                    text = task.category.label,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = RoutineVisualDefaults.onCategoryAccent(task.category.id),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
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
