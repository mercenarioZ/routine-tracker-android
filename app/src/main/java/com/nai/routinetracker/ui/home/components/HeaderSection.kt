package com.nai.routinetracker.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nai.routinetracker.R
import com.nai.routinetracker.ui.home.HomeUiState
import com.nai.routinetracker.ui.theme.RoutineTrackerTheme
import com.nai.routinetracker.ui.theme.RoutineVisualDefaults

@Composable
fun HeaderSection(
    state: HomeUiState,
    onLogoutClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.82f),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            DatePill(dateLabel = state.dateLabel)
            Text(
                text = stringResource(R.string.home_welcome_short, state.userName),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            state.highlight?.takeIf { it.isNotBlank() }?.let { highlight ->
                Text(
                    text = highlight,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(y = (-4).dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            border = RoutineVisualDefaults.cardBorder()
        ) {
            IconButton(onClick = onLogoutClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = stringResource(R.string.home_logout),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun DatePill(dateLabel: String) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.tertiaryContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.CalendarToday,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Text(
                text = dateLabel,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HeaderSectionPreview() {
    RoutineTrackerTheme {
        HeaderSection(
            state = HomeUiState(
                userName = "Nai",
                dateLabel = "Friday, April 17",
                highlight = "Keep your streak alive today."
            ),
            onLogoutClick = {}
        )
    }
}
