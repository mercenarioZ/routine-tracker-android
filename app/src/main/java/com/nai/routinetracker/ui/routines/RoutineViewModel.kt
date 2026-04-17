package com.nai.routinetracker.ui.routines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.model.RoutineCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val repository: RoutineRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RoutinesUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        observeDashboard()
    }

    fun onToggleRoutine(routineId: String) {
        viewModelScope.launch {
            repository.toggleRoutine(routineId)
        }
    }

    fun onCategorySelected(category: RoutineCategory?) {
        _uiState.update {
            it.copy(selectedCategory = category)
        }
    }

    fun onCompletedFilterChanged(showCompletedOnly: Boolean) {
        _uiState.update { it.copy(showCompletedOnly = showCompletedOnly) }
    }

    private fun observeDashboard() {
        viewModelScope.launch {
            repository.observeDashboard().collectLatest { dashboard ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        routines = dashboard.routines
                    )
                }
            }
        }
    }
}
