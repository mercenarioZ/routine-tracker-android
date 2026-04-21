package com.nai.routinetracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.model.next
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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
        observeDashboard()
    }

    fun onToggleRoutine(routineId: String) {
        val routine = _uiState.value.routines.firstOrNull { it.id == routineId } ?: return
        val newStatus = routine.status.next()

        viewModelScope.launch {
            repository.toggleRoutine(routineId)
            _effects.emit(
                HomeEffect.ShowRoutineStatusChanged(
                    routineTitle = routine.title,
                    newStatus = newStatus
                )
            )
        }
    }

    private fun observeDashboard() {
        viewModelScope.launch {
            repository.observeDashboard().collectLatest { dashboardState ->
                _uiState.value = dashboardState.toUiState()
            }
        }
    }
}
