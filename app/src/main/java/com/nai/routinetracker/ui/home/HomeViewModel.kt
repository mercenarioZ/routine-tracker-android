package com.nai.routinetracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.domain.repository.TaskRepository
import com.nai.routinetracker.model.next
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<HomeEffect>()
    val effects = _effects.asSharedFlow()

    init {
        observeRoutineDashboard()
        observeTasks()
    }

    fun onToggleTask(taskId: String) {
        val task = _uiState.value.tasks.firstOrNull { it.id == taskId } ?: return
        val newStatus = task.status.next()

        viewModelScope.launch {
            taskRepository.toggleTask(taskId)
            _effects.emit(
                HomeEffect.ShowTaskStatusChanged(
                    taskTitle = task.title,
                    newStatus = newStatus
                )
            )
        }
    }

    private fun observeRoutineDashboard() {
        viewModelScope.launch {
            routineRepository.observeDashboard().collectLatest { dashboardState ->
                _uiState.update { currentState ->
                    dashboardState.toUiState().copy(tasks = currentState.tasks)
                }
            }
        }
    }

    private fun observeTasks() {
        viewModelScope.launch {
            taskRepository.observeTasks().collectLatest { tasks ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        tasks = tasks
                    )
                }
            }
        }
    }
}
