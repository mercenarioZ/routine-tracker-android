package com.nai.routinetracker.ui.tasks

import com.nai.routinetracker.model.TaskCategory
import com.nai.routinetracker.model.TaskItem
import com.nai.routinetracker.model.isDone

data class TasksUiState(
    val isLoading: Boolean = false,
    val tasks: List<TaskItem> = emptyList(),
    val selectedCategory: TaskCategory? = null,
    val showCompletedOnly: Boolean = false
) {
    val categories: List<TaskCategory>
        get() = tasks.map { it.category }.distinctBy { it.id }

    val visibleTasks: List<TaskItem>
        get() = tasks.filter { task ->
            val matchesCategory = selectedCategory == null || task.category == selectedCategory
            val matchesCompletion = !showCompletedOnly || task.isDone
            matchesCategory && matchesCompletion
        }.sortedBy { if (it.isDone) 1 else 0 }
}
