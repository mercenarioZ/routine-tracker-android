package com.nai.routinetracker.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.domain.repository.TaskRepository
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.TaskItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val today = CalendarDate.today()
    private val _uiState = MutableStateFlow(CalendarUiState.initial(today))
    val uiState = _uiState.asStateFlow()

    private var routines: List<RoutineItem> = emptyList()
    private var tasks: List<TaskItem> = emptyList()

    init {
        observeCalendarData()
    }

    fun onPreviousMonth() {
        val state = _uiState.value
        updateCalendar(visibleMonth = state.visibleMonth.previous())
    }

    fun onNextMonth() {
        val state = _uiState.value
        updateCalendar(visibleMonth = state.visibleMonth.next())
    }

    fun onDateSelected(date: CalendarDate) {
        updateCalendar(
            visibleMonth = date.toMonth(),
            selectedDate = date
        )
    }

    fun onToggleTask(taskId: String) {
        viewModelScope.launch {
            runCatching {
                taskRepository.toggleTask(taskId)
            }
        }
    }

    private fun observeCalendarData() {
        viewModelScope.launch {
            combine(
                routineRepository.observeDashboard(),
                taskRepository.observeTasks()
            ) { dashboard, observedTasks ->
                routines = dashboard.routines
                tasks = observedTasks
            }.catch {
                _uiState.update { currentState ->
                    currentState.copy(isLoading = false)
                }
            }.collect {
                val state = _uiState.value
                updateCalendar(
                    visibleMonth = state.visibleMonth,
                    selectedDate = state.selectedDate,
                    isLoading = false
                )
            }
        }
    }

    private fun updateCalendar(
        visibleMonth: CalendarMonth,
        selectedDate: CalendarDate = visibleMonth.dateForDayClamped(_uiState.value.selectedDate.day),
        isLoading: Boolean = _uiState.value.isLoading
    ) {
        _uiState.value = CalendarStateFactory.build(
            today = today,
            visibleMonth = visibleMonth,
            selectedDate = selectedDate,
            routines = routines,
            tasks = tasks,
            isLoading = isLoading
        )
    }
}
