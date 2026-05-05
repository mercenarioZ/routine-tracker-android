package com.nai.routinetracker.ui.routines

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nai.routinetracker.R
import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.ui.theme.RoutineTrackerTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoutineScreen(
    state: CreateRoutineUiState,
    onTitleChanged: (String) -> Unit,
    onScheduleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCategorySelected: (RoutineCategory) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isTimePickerOpen by remember { mutableStateOf(false) }
    val initialTime = parseTimeLabel(state.scheduleLabel)
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = false
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.create_routine_back)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = stringResource(R.string.create_routine_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.create_routine_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedTextField(
                value = state.title,
                onValueChange = onTitleChanged,
                label = { Text(text = stringResource(R.string.create_routine_name_label)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier.fillMaxWidth()
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.create_routine_time_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedButton(
                    onClick = {
                        val selectedTime = parseTimeLabel(state.scheduleLabel)
                        timePickerState.hour = selectedTime.hour
                        timePickerState.minute = selectedTime.minute
                        isTimePickerOpen = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Text(
                        text = state.scheduleLabel.ifBlank {
                            stringResource(R.string.create_routine_time_placeholder)
                        }
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = stringResource(R.string.create_routine_category_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.categories.forEach { category ->
                        FilterChip(
                            selected = state.selectedCategory == category,
                            onClick = { onCategorySelected(category) },
                            label = { Text(text = category.label) }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = state.description,
                onValueChange = onDescriptionChanged,
                label = { Text(text = stringResource(R.string.create_routine_description_label)) },
                minLines = 4,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                enabled = state.canSubmit,
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.create_routine_save))
            }
        }
    }

    if (isTimePickerOpen) {
        AlertDialog(
            onDismissRequest = { isTimePickerOpen = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onScheduleChanged(
                            formatTimeLabel(
                                hour = timePickerState.hour,
                                minute = timePickerState.minute
                            )
                        )
                        isTimePickerOpen = false
                    }
                ) {
                    Text(text = stringResource(R.string.create_routine_time_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { isTimePickerOpen = false }) {
                    Text(text = stringResource(R.string.create_routine_time_cancel))
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}

private data class TimeSelection(
    val hour: Int,
    val minute: Int
)

private fun parseTimeLabel(scheduleLabel: String): TimeSelection {
    val match = TIME_LABEL_REGEX.matchEntire(scheduleLabel.trim())
        ?: return TimeSelection(hour = 6, minute = 30)
    val displayHour = match.groupValues[1].toInt()
    val minute = match.groupValues[2].toInt()
    if (displayHour !in 1..12 || minute !in 0..59) {
        return TimeSelection(hour = 6, minute = 30)
    }
    val period = match.groupValues[3].uppercase(Locale.US)
    val hour = when {
        period == "AM" && displayHour == 12 -> 0
        period == "PM" && displayHour != 12 -> displayHour + 12
        else -> displayHour
    }
    return TimeSelection(hour = hour, minute = minute)
}

private fun formatTimeLabel(hour: Int, minute: Int): String {
    val displayHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    val period = if (hour < 12) "AM" else "PM"
    return String.format(Locale.US, "%02d:%02d %s", displayHour, minute, period)
}

private val TIME_LABEL_REGEX = Regex("""(\d{1,2}):(\d{2})\s*([AP]M)""", RegexOption.IGNORE_CASE)

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CreateRoutineScreenPreview() {
    RoutineTrackerTheme {
        CreateRoutineScreen(
            state = CreateRoutineUiState(
                title = "Evening reset",
                scheduleLabel = "08:30 PM",
                description = "Tidy the desk, review tomorrow, and close the day calmly."
            ),
            onTitleChanged = {},
            onScheduleChanged = {},
            onDescriptionChanged = {},
            onCategorySelected = {},
            onSaveClick = {},
            onBackClick = {}
        )
    }
}
