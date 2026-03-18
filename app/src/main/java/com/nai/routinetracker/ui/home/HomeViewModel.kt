package com.nai.routinetracker.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nai.routinetracker.data.repository.RoutineRepository
import com.nai.routinetracker.model.RoutineItem

class HomeViewModel(
    private val repository: RoutineRepository
) : ViewModel() {
    var uiState by mutableStateOf(repository.getDashboardState())
        private set

    fun onRoutineToggle(routineId: String) {
        val updatedRoutines = uiState.routines.map { routine ->
            if (routine.id != routineId) {
                routine
            } else {
                routine.updatedCompletion()
            }
        }

        uiState = repository.updateRoutines(updatedRoutines)
    }

    class Factory(
        private val repository: RoutineRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(repository) as T
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
