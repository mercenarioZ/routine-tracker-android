package com.nai.routinetracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.model.RoutineItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RoutineRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<HomeEffect>()
    val effects = _effects.asSharedFlow()

    init {
        loadDashboard()
    }

    fun onToggleRoutine(routineId: String) {
        val currentState = _uiState.value
        val updatedRoutines = currentState.routines.map { routine ->
            if (routine.id == routineId) {
                routine.updatedCompletion()
            } else {
                routine
            }
        }
        val updatedRoutine = updatedRoutines.firstOrNull { it.id == routineId } ?: return

        _uiState.update { it.copy(routines = updatedRoutines) }

        viewModelScope.launch {
            val dashboardState = repository.updateRoutines(updatedRoutines)
            _uiState.value = dashboardState.toUiState()
            _effects.emit(
                HomeEffect.ShowRoutineStatusChanged(
                    routineTitle = updatedRoutine.title,
                    isCompleted = updatedRoutine.completed
                )
            )
        }
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            val dashboardState = repository.getDashboard()
            _uiState.value = dashboardState.toUiState()
        }
    }
}

private fun RoutineItem.updatedCompletion(): RoutineItem {
    return copy(
        completed = !completed,
        streakDays = if (completed) {
            (streakDays - 1).coerceAtLeast(0)
        } else {
            streakDays + 1
        }
    )
}
