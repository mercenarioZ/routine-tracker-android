package com.nai.routinetracker.ui.routines

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nai.routinetracker.R
import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.ui.routines.components.CategoryFilters
import com.nai.routinetracker.ui.routines.components.CategoryInsight
import com.nai.routinetracker.ui.routines.components.EmptyState
import com.nai.routinetracker.ui.routines.components.ErrorBanner
import com.nai.routinetracker.ui.routines.components.ListHeader
import com.nai.routinetracker.ui.routines.components.RoutineHeader
import com.nai.routinetracker.ui.routines.components.RoutineManagementCard
import com.nai.routinetracker.ui.routines.components.RoutineSummary

@Composable
fun RoutinesScreen(
    state: RoutinesUiState,
    onCategorySelected: (RoutineCategory?) -> Unit,
    onCreateRoutineClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateRoutineClick) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(R.string.create_routine_title)
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (state.isLoading && state.routines.isEmpty()) {
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
                    .statusBarsPadding()
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 20.dp,
                    end = 20.dp,
                    bottom = 92.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    RoutineHeader()
                }

                item {
                    RoutineSummary(state = state)
                }

                state.errorMessage?.let { errorMessage ->
                    item {
                        ErrorBanner(message = errorMessage)
                    }
                }

                item {
                    CategoryInsight(state = state)
                }

                item {
                    CategoryFilters(
                        categories = state.categories,
                        selectedCategory = state.selectedCategory,
                        onCategorySelected = onCategorySelected
                    )
                }

                item {
                    ListHeader(state = state)
                }

                if (state.visibleRoutines.isEmpty()) {
                    item {
                        EmptyState(
                            hasFilter = state.selectedCategory != null,
                            onCreateRoutineClick = onCreateRoutineClick
                        )
                    }
                } else {
                    items(state.visibleRoutines, key = { it.id }) { routine ->
                        RoutineManagementCard(
                            routine = routine,
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }
}
