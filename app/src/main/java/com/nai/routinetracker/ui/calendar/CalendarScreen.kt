package com.nai.routinetracker.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.nai.routinetracker.ui.tasks.components.TaskCard
import com.nai.routinetracker.ui.theme.RoutineTrackerTheme
import com.nai.routinetracker.ui.theme.RoutineVisualDefaults

@Composable
fun CalendarScreen(
    state: CalendarUiState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDateSelected: (CalendarDate) -> Unit,
    onToggleTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (state.isLoading && state.monthDays.all { it.taskCount == 0 } && state.selectedRoutines.isEmpty()) {
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
                CalendarHeader()
            }
            item {
                MonthCard(
                    state = state,
                    onPreviousMonth = onPreviousMonth,
                    onNextMonth = onNextMonth,
                    onDateSelected = onDateSelected
                )
            }
            item {
                AgendaHeader(state = state)
            }
            item {
                AgendaRoutines(routines = state.selectedRoutines)
            }
            item {
                SectionHeader(
                    title = stringResource(R.string.calendar_tasks_title),
                    count = state.selectedTasks.size
                )
            }
            if (state.selectedTasks.isEmpty()) {
                item {
                    EmptyAgendaText(text = stringResource(R.string.calendar_empty_tasks))
                }
            } else {
                items(state.selectedTasks, key = { it.id }) { task ->
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
private fun CalendarHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = stringResource(R.string.calendar_screen_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.calendar_screen_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun MonthCard(
    state: CalendarUiState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDateSelected: (CalendarDate) -> Unit
) {
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPreviousMonth) {
                    Icon(
                        imageVector = Icons.Outlined.ChevronLeft,
                        contentDescription = stringResource(R.string.calendar_previous_month)
                    )
                }
                Text(
                    text = state.visibleMonthLabel,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(onClick = onNextMonth) {
                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = stringResource(R.string.calendar_next_month)
                    )
                }
            }

            WeekdayHeader()
            MonthGrid(
                days = state.monthDays,
                onDateSelected = onDateSelected
            )
        }
    }
}

@Composable
private fun WeekdayHeader() {
    val labels = listOf(
        R.string.calendar_weekday_monday,
        R.string.calendar_weekday_tuesday,
        R.string.calendar_weekday_wednesday,
        R.string.calendar_weekday_thursday,
        R.string.calendar_weekday_friday,
        R.string.calendar_weekday_saturday,
        R.string.calendar_weekday_sunday
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        labels.forEach { labelRes ->
            Text(
                text = stringResource(labelRes),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MonthGrid(
    days: List<CalendarDayUi>,
    onDateSelected: (CalendarDate) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        days.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                week.forEach { day ->
                    DayCell(
                        day = day,
                        onDateSelected = onDateSelected,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    day: CalendarDayUi,
    onDateSelected: (CalendarDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = when {
        day.isSelected -> MaterialTheme.colorScheme.primaryContainer
        day.isToday -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (day.isInVisibleMonth) 0.58f else 0.24f)
    }
    val contentColor = when {
        day.isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
        day.isToday -> MaterialTheme.colorScheme.onTertiaryContainer
        day.isInVisibleMonth -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.58f)
    }

    Surface(
        modifier = modifier.aspectRatio(1f),
        shape = RoutineVisualDefaults.CompactShape,
        color = containerColor,
        onClick = { onDateSelected(day.date) }
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = day.date.day.toString(),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (day.isSelected || day.isToday) FontWeight.Bold else FontWeight.Medium
                ),
                color = contentColor,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
            ActivityDots(
                taskCount = day.taskCount,
                routineCount = day.routineCount,
                isMuted = !day.isInVisibleMonth
            )
        }
    }
}

@Composable
private fun ActivityDots(
    taskCount: Int,
    routineCount: Int,
    isMuted: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (routineCount > 0) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = if (isMuted) 0.3f else 1f))
            )
        }
        if (taskCount > 0) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = if (isMuted) 0.3f else 1f))
            )
        }
    }
}

@Composable
private fun AgendaHeader(state: CalendarUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoutineVisualDefaults.CardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.EventAvailable,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.calendar_selected_day_title),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.74f)
                )
            }
            Text(
                text = if (state.selectedDate == state.today) {
                    "${state.selectedDateLabel} • ${stringResource(R.string.calendar_today_label)}"
                } else {
                    state.selectedDateLabel
                },
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${pluralStringResource(R.plurals.calendar_routine_count, state.selectedRoutines.size, state.selectedRoutines.size)} • ${
                    pluralStringResource(R.plurals.calendar_task_count, state.selectedTasks.size, state.selectedTasks.size)
                }",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f)
            )
        }
    }
}

@Composable
private fun AgendaRoutines(routines: List<RoutineItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionHeader(
            title = stringResource(R.string.calendar_routines_title),
            count = routines.size
        )
        if (routines.isEmpty()) {
            EmptyAgendaText(text = stringResource(R.string.calendar_empty_routines))
        } else {
            routines.forEach { routine ->
                RoutineAgendaRow(routine = routine)
            }
        }
    }
}

@Composable
private fun RoutineAgendaRow(routine: RoutineItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoutineVisualDefaults.CardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(38.dp),
                shape = CircleShape,
                color = RoutineVisualDefaults.categoryAccent(routine.category.id)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = RoutineVisualDefaults.onCategoryAccent(routine.category.id)
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = routine.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${routine.scheduleLabel} • ${routine.category.label}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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
        modifier = Modifier.fillMaxWidth(),
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
private fun EmptyAgendaText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CalendarScreenPreview() {
    val today = CalendarDate(year = 2026, month = 6, day = 14)
    RoutineTrackerTheme {
        CalendarScreen(
            state = CalendarStateFactory.build(
                today = today,
                visibleMonth = today.toMonth(),
                selectedDate = today,
                routines = listOf(
                    RoutineItem(
                        id = "hydration",
                        title = "Hydrate and stretch",
                        scheduleLabel = "06:30 AM",
                        category = RoutineCategories.Health,
                        streakDays = 12,
                        description = "Drink water and stretch."
                    )
                ),
                tasks = listOf(
                    TaskItem(
                        id = "task-hydration",
                        routineId = "hydration",
                        title = "Hydrate and stretch",
                        timeLabel = "06:30 AM",
                        dueLabel = "Today",
                        category = TaskCategories.Personal,
                        status = TaskStatus.Pending,
                        description = "Drink water and stretch."
                    )
                )
            ),
            onPreviousMonth = {},
            onNextMonth = {},
            onDateSelected = {},
            onToggleTask = {}
        )
    }
}
