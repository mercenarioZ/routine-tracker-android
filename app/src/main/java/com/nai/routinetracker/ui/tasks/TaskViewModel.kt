package com.nai.routinetracker.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nai.routinetracker.domain.repository.TaskRepository
import com.nai.routinetracker.model.TaskCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TasksUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        observeTasks()
    }

    fun onToggleTask(taskId: String) {
        viewModelScope.launch {
            repository.toggleTask(taskId)
        }
    }

    fun onCategorySelected(category: TaskCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onCompletedFilterChanged(showCompletedOnly: Boolean) {
        _uiState.update { it.copy(showCompletedOnly = showCompletedOnly) }
    }

    private fun observeTasks() {
        viewModelScope.launch {
            repository.observeTasks().collectLatest { tasks ->
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
