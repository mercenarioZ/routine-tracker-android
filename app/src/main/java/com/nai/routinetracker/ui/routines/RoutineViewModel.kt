package com.nai.routinetracker.ui.routines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineWeekday
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
        _createUiState.update { it.copy(title = title, errorMessage = null) }
    }

    fun onCreateScheduleChanged(scheduleLabel: String) {
        _createUiState.update { it.copy(scheduleLabel = scheduleLabel, errorMessage = null) }
    }

    fun onCreateDescriptionChanged(description: String) {
        _createUiState.update { it.copy(description = description, errorMessage = null) }
    }

    fun onCreateCategorySelected(category: RoutineCategory) {
        _createUiState.update { it.copy(selectedCategory = category, errorMessage = null) }
    }

    fun onCreateRecurrenceModeSelected(mode: CreateRoutineRecurrenceMode) {
        _createUiState.update { state ->
            state.copy(
                recurrenceMode = mode,
                selectedWeekdays = when {
                    mode == CreateRoutineRecurrenceMode.Daily -> state.selectedWeekdays
                    state.selectedWeekdays.isNotEmpty() -> state.selectedWeekdays
                    else -> setOf(RoutineWeekday.Monday)
                },
                errorMessage = null
            )
        }
    }

    fun onCreateWeekdayToggled(weekday: RoutineWeekday) {
        _createUiState.update { state ->
            val updatedWeekdays = when (state.recurrenceMode) {
                CreateRoutineRecurrenceMode.Daily -> state.selectedWeekdays
                CreateRoutineRecurrenceMode.Weekly -> setOf(weekday)
                CreateRoutineRecurrenceMode.Custom -> {
                    if (weekday in state.selectedWeekdays) {
                        state.selectedWeekdays - weekday
                    } else {
                        state.selectedWeekdays + weekday
                    }
                }
            }
            state.copy(
                selectedWeekdays = updatedWeekdays,
                errorMessage = null
            )
        }
    }

    fun createRoutine(onCreated: () -> Unit) {
        val form = _createUiState.value
        if (!form.canSubmit) return

        viewModelScope.launch {
            _createUiState.update { it.copy(isSaving = true, errorMessage = null) }

            runCatching {
                routineRepository.createRoutine(
                    title = form.title.trim(),
                    scheduleLabel = form.scheduleLabel.trim(),
                    category = form.selectedCategory,
                    recurrence = form.recurrence,
                    description = form.description.trim()
                )
            }.onSuccess {
                _createUiState.value = CreateRoutineUiState()
                onCreated()
            }.onFailure { throwable ->
                _createUiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = throwable.message ?: "Unable to create routine"
                    )
                }
            }
        }
    }

    private fun observeDashboard() {
        viewModelScope.launch {
            routineRepository.observeDashboard()
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to load routines"
                        )
                    }
                }
                .collectLatest { dashboard ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            routines = dashboard.routines,
                            errorMessage = null
                        )
                    }
                }
        }
    }
}
