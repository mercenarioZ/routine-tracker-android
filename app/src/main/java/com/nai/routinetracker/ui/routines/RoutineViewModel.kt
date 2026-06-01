package com.nai.routinetracker.ui.routines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.model.RoutineCategories
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
    private val routineRepository: RoutineRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RoutinesUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _createUiState = MutableStateFlow(CreateRoutineUiState())
    val createUiState = _createUiState.asStateFlow()

    init {
        observeDashboard()
    }

    fun onCategorySelected(category: RoutineCategory?) {
        _uiState.update {
            it.copy(selectedCategory = category)
        }
    }

    fun onCreateTitleChanged(title: String) {
        _createUiState.update { it.copy(title = title) }
    }

    fun onCreateScheduleChanged(scheduleLabel: String) {
        _createUiState.update { it.copy(scheduleLabel = scheduleLabel) }
    }

    fun onCreateDescriptionChanged(description: String) {
        _createUiState.update { it.copy(description = description) }
    }

    fun onCreateCategorySelected(category: RoutineCategory) {
        _createUiState.update { it.copy(selectedCategory = category) }
    }

    fun createRoutine(onCreated: () -> Unit) {
        val form = _createUiState.value
        if (!form.canSubmit) return

        viewModelScope.launch {
            routineRepository.createRoutine(
                title = form.title.trim(),
                scheduleLabel = form.scheduleLabel.trim(),
                category = form.selectedCategory,
                description = form.description.trim()
            )
            _createUiState.value = CreateRoutineUiState()
            onCreated()
        }
    }

    private fun observeDashboard() {
        viewModelScope.launch {
            routineRepository.observeDashboard().collectLatest { dashboard ->
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
