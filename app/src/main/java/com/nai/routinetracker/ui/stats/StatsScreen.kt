package com.nai.routinetracker.ui.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nai.routinetracker.R
import com.nai.routinetracker.model.RoutineCategories
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.TaskCategories
import com.nai.routinetracker.model.TaskItem
import com.nai.routinetracker.model.TaskStatus
import com.nai.routinetracker.ui.theme.RoutineTrackerTheme
import com.nai.routinetracker.ui.theme.RoutineVisualDefaults

@Composable
fun StatsScreen(
    state: StatsUiState,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (state.isLoading && !state.hasData) {
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
                StatsHeader()
            }

            if (!state.hasData) {
                item {
                    EmptyStatsCard()
                }
            } else {
                item {
                    StatsMetricGrid(state = state)
                }
                item {
                    CompletionCard(state = state)
                }
                item {
                    val categorySegments = if (state.taskCategorySegments.isNotEmpty()) {
                        state.taskCategorySegments
                    } else {
                        state.routineCategorySegments
                    }
                    PieCard(
                        title = stringResource(R.string.stats_category_mix_title),
                        subtitle = stringResource(
                            if (state.taskCategorySegments.isNotEmpty()) {
                                R.string.stats_category_mix_tasks
                            } else {
                                R.string.stats_category_mix_routines
                            }
                        ),
                        segments = categorySegments
                    )
                }
                item {
                    ColumnChartCard(
                        title = stringResource(R.string.stats_streak_chart_title),
                        subtitle = stringResource(R.string.stats_streak_chart_subtitle),
                        segments = state.routineStreakBars
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = stringResource(R.string.stats_screen_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.stats_screen_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun StatsMetricGrid(state: StatsUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatMetricCard(
                label = stringResource(R.string.stats_metric_completion),
                value = stringResource(R.string.stats_percent_value, state.completionPercent),
                icon = Icons.Outlined.CheckCircle,
                modifier = Modifier.weight(1f)
            )
            StatMetricCard(
                label = stringResource(R.string.stats_metric_open_tasks),
                value = state.openTaskCount.toString(),
                icon = Icons.Outlined.RadioButtonUnchecked,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatMetricCard(
                label = stringResource(R.string.stats_metric_active_routines),
                value = state.activeRoutineCount.toString(),
                icon = Icons.Outlined.Autorenew,
                modifier = Modifier.weight(1f)
            )
            StatMetricCard(
                label = stringResource(R.string.stats_metric_best_streak),
                value = state.bestStreakDays.toString(),
                icon = Icons.Outlined.BarChart,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatMetricCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.heightIn(min = 92.dp),
        shape = RoutineVisualDefaults.CardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CompletionCard(state: StatsUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoutineVisualDefaults.CardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DonutChart(
                segments = state.completionSegments,
                centerValue = stringResource(R.string.stats_percent_value, state.completionPercent),
                centerLabel = stringResource(R.string.stats_completion_center_label),
                modifier = Modifier.size(150.dp),
                surfaceColor = MaterialTheme.colorScheme.primaryContainer
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.stats_completion_chart_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = stringResource(
                        R.string.stats_completion_summary,
                        state.completedTaskCount,
                        state.tasks.size
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.74f)
                )
                ChartLegend(segments = state.completionSegments)
            }
        }
    }
}

@Composable
private fun PieCard(
    title: String,
    subtitle: String,
    segments: List<StatsSegment>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoutineVisualDefaults.CardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ChartHeader(title = title, subtitle = subtitle)
            Row(
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DonutChart(
                    segments = segments,
                    centerValue = segments.sumOf { it.value }.toString(),
                    centerLabel = stringResource(R.string.stats_category_total_label),
                    modifier = Modifier.size(144.dp),
                    surfaceColor = MaterialTheme.colorScheme.surface
                )
                ChartLegend(
                    segments = segments,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ColumnChartCard(
    title: String,
    subtitle: String,
    segments: List<StatsSegment>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoutineVisualDefaults.CardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ChartHeader(title = title, subtitle = subtitle)
            if (segments.isEmpty()) {
                Text(
                    text = stringResource(R.string.stats_streak_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                StreakColumnChart(segments = segments)
            }
        }
    }
}

@Composable
private fun ChartHeader(
    title: String,
    subtitle: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DonutChart(
    segments: List<StatsSegment>,
    centerValue: String,
    centerLabel: String,
    surfaceColor: Color,
    modifier: Modifier = Modifier
) {
    val colors = segments.mapIndexed { index, segment ->
        segmentColor(segment.id, index)
    }
    val trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (segments.isEmpty()) {
                drawCircle(color = trackColor)
            } else {
                drawPieSegments(
                    segments = segments,
                    colors = colors,
                    surfaceColor = surfaceColor
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = centerValue,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            Text(
                text = centerLabel,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

private fun DrawScope.drawPieSegments(
    segments: List<StatsSegment>,
    colors: List<Color>,
    surfaceColor: Color
) {
    val total = segments.sumOf { it.value }.coerceAtLeast(1)
    val diameter = size.minDimension
    val topLeft = Offset(
        x = (size.width - diameter) / 2f,
        y = (size.height - diameter) / 2f
    )
    var startAngle = -90f

    segments.forEachIndexed { index, segment ->
        val sweepAngle = 360f * segment.value / total
        drawArc(
            color = colors[index],
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = true,
            topLeft = topLeft,
            size = Size(diameter, diameter)
        )
        startAngle += sweepAngle
    }
    drawCircle(
        color = surfaceColor,
        radius = diameter * 0.28f
    )
}

@Composable
private fun StreakColumnChart(segments: List<StatsSegment>) {
    val maxValue = segments.maxOfOrNull { it.value }?.coerceAtLeast(1) ?: 1

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(178.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        segments.forEachIndexed { index, segment ->
            val ratio = (segment.value.toFloat() / maxValue).coerceIn(0.05f, 1f)

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = segment.value.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Surface(
                        modifier = Modifier
                            .width(30.dp)
                            .fillMaxHeight(ratio),
                        shape = RoutineVisualDefaults.CompactShape,
                        color = segmentColor(segment.id, index)
                    ) {}
                }
                Text(
                    text = segment.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ChartLegend(
    segments: List<StatsSegment>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        segments.forEachIndexed { index, segment ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            color = segmentColor(segment.id, index),
                            shape = CircleShape
                        )
                )
                Text(
                    text = stringResource(
                        R.string.stats_legend_item,
                        segment.label,
                        segment.value
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun EmptyStatsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoutineVisualDefaults.CardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.stats_empty_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.stats_empty_body),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun segmentColor(id: String, index: Int): Color {
    return when (id) {
        "done" -> MaterialTheme.colorScheme.primary
        "open" -> MaterialTheme.colorScheme.secondary
        else -> if (index % 2 == 0) {
            RoutineVisualDefaults.categoryAccent(id)
        } else {
            RoutineVisualDefaults.onCategoryAccent(id)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatsScreenPreview() {
    RoutineTrackerTheme {
        StatsScreen(
            state = StatsUiState(
                routines = listOf(
                    RoutineItem(
                        id = "1",
                        title = "Hydrate and stretch",
                        scheduleLabel = "06:30 AM",
                        category = RoutineCategories.Health,
                        streakDays = 8,
                        description = "Wake the body up."
                    ),
                    RoutineItem(
                        id = "2",
                        title = "Deep work",
                        scheduleLabel = "09:00 AM",
                        category = RoutineCategories.Focus,
                        streakDays = 5,
                        description = "Start a focus block."
                    )
                ),
                tasks = listOf(
                    TaskItem(
                        id = "1",
                        routineId = "1",
                        title = "Stretch",
                        timeLabel = "06:30 AM",
                        dueLabel = "Today",
                        category = TaskCategories.Personal,
                        status = TaskStatus.Done,
                        description = "Stretch."
                    ),
                    TaskItem(
                        id = "2",
                        routineId = null,
                        title = "Inbox review",
                        timeLabel = "10:00 AM",
                        dueLabel = "Today",
                        category = TaskCategories.Work,
                        status = TaskStatus.Pending,
                        description = "Review inbox."
                    )
                )
            )
        )
    }
}
