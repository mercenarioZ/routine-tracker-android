package com.nai.routinetracker.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nai.routinetracker.R
import com.nai.routinetracker.model.RoutineDashboardState

@Composable
fun HeaderSection(state: RoutineDashboardState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = state.dateLabel,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.home_welcome_back, state.userName),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = state.highlight,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
