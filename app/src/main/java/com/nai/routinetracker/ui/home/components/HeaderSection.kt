package com.nai.routinetracker.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nai.routinetracker.R
import com.nai.routinetracker.ui.home.HomeUiState
import com.nai.routinetracker.ui.theme.RoutineTrackerTheme

@Composable
fun HeaderSection(
    state: HomeUiState,
    onLogoutClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
//            Text(
//                text = state.highlight,
//                style = MaterialTheme.typography.bodyLarge,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            ) // quote
        }

        IconButton(
            onClick = onLogoutClick,
            modifier = Modifier.align(Alignment.TopEnd).offset(y = (-8).dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Logout,
                contentDescription = stringResource(R.string.home_logout)
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
