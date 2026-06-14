package com.nai.routinetracker.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nai.routinetracker.R
import com.nai.routinetracker.ui.home.HomeUiState
import com.nai.routinetracker.ui.theme.RoutineVisualDefaults
import kotlin.math.roundToInt

@Composable
fun MetricsRow(state: HomeUiState) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MetricCard(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.home_metric_completion),
            value = "${(state.completionRatio * 100).roundToInt()}%",
            icon = Icons.Outlined.CheckCircle,
            emphasized = true
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.home_metric_active_routines),
                value = state.activeRoutines.size.toString(),
                icon = Icons.Outlined.RadioButtonChecked
            )
            MetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.home_metric_streak_days),
                value = state.totalStreakDays.toString(),
                icon = Icons.Outlined.LocalFireDepartment
            )
        }
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    emphasized: Boolean = false
) {
    Card(
        modifier = modifier.heightIn(min = if (emphasized) 78.dp else 74.dp),
        shape = RoutineVisualDefaults.CardShape,
        border = RoutineVisualDefaults.cardBorder(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (emphasized) {
                MaterialTheme.colorScheme.tertiaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        if (emphasized) {
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.68f)
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer
                        }
                    )
                    .padding(7.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (emphasized) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    }
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (emphasized) {
                        MaterialTheme.colorScheme.onTertiaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (emphasized) {
                        MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.74f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}
