package com.nai.routinetracker.ui.routines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.model.RoutineCategories
import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineItem
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
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
            repository.createRoutine(
                RoutineItem(
                    id = UUID.randomUUID().toString(),
                    title = form.title.trim(),
                    scheduleLabel = form.scheduleLabel.trim(),
                    category = form.selectedCategory,
                    streakDays = 0,
                    description = form.description.trim()
                )
            )
            _createUiState.value = CreateRoutineUiState()
            onCreated()
        }
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
