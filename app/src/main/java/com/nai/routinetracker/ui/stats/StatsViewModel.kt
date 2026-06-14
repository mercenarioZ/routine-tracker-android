package com.nai.routinetracker.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class StatsViewModel @Inject constructor(
    routineRepository: RoutineRepository,
    taskRepository: TaskRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatsUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                routineRepository.observeDashboard(),
                taskRepository.observeTasks()
            ) { dashboardState, tasks ->
                StatsUiState(
                    isLoading = false,
                    routines = dashboardState.routines,
                    tasks = tasks
                )
            }
                .catch {
                    _uiState.update { currentState ->
                        currentState.copy(isLoading = false)
                    }
                }
                .collectLatest { statsState ->
                    _uiState.value = statsState
                }
        }
    }
}
