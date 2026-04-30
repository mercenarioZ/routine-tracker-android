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
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nai.routinetracker.R
import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.ui.theme.RoutineTrackerTheme

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

            OutlinedTextField(
                value = state.scheduleLabel,
                onValueChange = onScheduleChanged,
                label = { Text(text = stringResource(R.string.create_routine_time_label)) },
                placeholder = { Text(text = stringResource(R.string.create_routine_time_placeholder)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

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
}

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
