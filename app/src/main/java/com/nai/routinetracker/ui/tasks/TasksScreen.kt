package com.nai.routinetracker.ui.tasks

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.ViewAgenda
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nai.routinetracker.R
import com.nai.routinetracker.model.TaskCategory
import com.nai.routinetracker.model.isDone
import com.nai.routinetracker.ui.tasks.components.TaskCard
import com.nai.routinetracker.ui.theme.RoutineVisualDefaults

@Composable
fun TasksScreen(
    state: TasksUiState,
    onToggleTask: (String) -> Unit,
    onCategorySelected: (TaskCategory?) -> Unit,
    onCompletedFilterChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (state.isLoading && state.tasks.isEmpty()) {
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
                TasksHeader()
            }

            item {
                TaskFlowSummary(state = state)
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoutineVisualDefaults.CardShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FilterList,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = stringResource(R.string.tasks_filters_title),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        CategoryFilters(
                            categories = state.categories,
                            selectedCategory = state.selectedCategory,
                            onCategorySelected = onCategorySelected
                        )
                        FilterChip(
                            selected = state.showCompletedOnly,
                            onClick = { onCompletedFilterChanged(!state.showCompletedOnly) },
                            shape = RoutineVisualDefaults.PillShape,
                            colors = taskFilterChipColors(),
                            label = {
                                Text(text = stringResource(R.string.tasks_filter_done_only))
                            }
                        )
                    }
                }
            }

            if (state.visibleTasks.isEmpty()) {
                item {
                    EmptyState()
                }
            } else {
                items(state.visibleTasks, key = { it.id }) { task ->
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
private fun TasksHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = stringResource(R.string.tasks_screen_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.tasks_screen_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun TaskFlowSummary(state: TasksUiState) {
    val doneCount = state.tasks.count { it.isDone }
    val openCount = state.tasks.size - doneCount

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoutineVisualDefaults.CardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.tasks_summary_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TaskMetric(
                    label = stringResource(R.string.tasks_metric_open),
                    value = openCount.toString(),
                    icon = Icons.Outlined.RadioButtonUnchecked,
                    modifier = Modifier.weight(1f)
                )
                TaskMetric(
                    label = stringResource(R.string.tasks_metric_done),
                    value = doneCount.toString(),
                    icon = Icons.Outlined.CheckCircle,
                    modifier = Modifier.weight(1f)
                )
                TaskMetric(
                    label = stringResource(R.string.tasks_metric_visible),
                    value = state.visibleTasks.size.toString(),
                    icon = Icons.Outlined.ViewAgenda,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun TaskMetric(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.heightIn(min = 78.dp),
        shape = RoutineVisualDefaults.CompactShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.72f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CategoryFilters(
    categories: List<TaskCategory>,
    selectedCategory: TaskCategory?,
    onCategorySelected: (TaskCategory?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedCategory == null,
            onClick = { onCategorySelected(null) },
            shape = RoutineVisualDefaults.PillShape,
            colors = taskFilterChipColors(),
            label = { Text(text = stringResource(R.string.tasks_filter_all)) }
        )
        categories.forEach { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                shape = RoutineVisualDefaults.PillShape,
                colors = taskFilterChipColors(),
                label = { Text(text = category.label) }
            )
        }
    }
}

@Composable
private fun taskFilterChipColors() = FilterChipDefaults.filterChipColors(
    containerColor = MaterialTheme.colorScheme.surface,
    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
)

@Composable
private fun EmptyState() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoutineVisualDefaults.CardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                modifier = Modifier.size(42.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FilterList,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
            Text(
                text = stringResource(R.string.tasks_empty_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.tasks_empty_state),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
