package com.nai.routinetracker.ui.routines.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nai.routinetracker.R
import com.nai.routinetracker.ui.routines.RoutinesUiState
import com.nai.routinetracker.ui.theme.RoutineVisualDefaults

@Composable
fun ErrorBanner(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoutineVisualDefaults.CardShape,
        color = MaterialTheme.colorScheme.errorContainer
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.routines_error_title),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
fun CategoryInsight(state: RoutinesUiState) {
    val selectedCategory = state.selectedCategory
    val categoryContext = if (selectedCategory == null) {
        stringResource(R.string.routines_viewing_all)
    } else {
        stringResource(R.string.routines_viewing_category, selectedCategory.label)
    }
    val strongestCategory = state.routines
        .groupBy { it.category }
        .maxByOrNull { (_, routines) -> routines.size }
        ?.key
        ?.label

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoutineVisualDefaults.CardShape,
        border = RoutineVisualDefaults.cardBorder(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.routines_category_insight_title),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = categoryContext,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Surface(
                shape = RoutineVisualDefaults.PillShape,
                color = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Text(
                    text = stringResource(
                        R.string.routines_visible_count,
                        state.visibleRoutines.size,
                        state.routines.size
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (strongestCategory != null) {
                Text(
                    text = stringResource(
                        R.string.routines_category_insight_body,
                        strongestCategory
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
