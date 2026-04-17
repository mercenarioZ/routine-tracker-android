package com.nai.routinetracker.ui.routines

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nai.routinetracker.model.RoutineCategory

@Composable
fun RoutinesScreen(state: RoutinesUiState,
                   onToggleRoutine: (String) -> Unit,
                   onCategorySelected: (RoutineCategory?) -> Unit,
                   onCompletedFilterChanged: (Boolean) -> Unit,
                   modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Routines management page",
            style = MaterialTheme.typography.titleLarge
        )
    }
}