package com.nai.routinetracker.data.repository

import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.domain.repository.TaskRepository
import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineDashboardState
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.RoutineRecurrence
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeRoutineRepository @Inject constructor(
    seedStringProvider: FakeSeedStringProvider,
    private val taskRepository: TaskRepository
) : RoutineRepository {
    private val seedStrings = seedStringProvider.strings()
    private val dashboardState = MutableStateFlow(buildDashboardState(seedStrings))

    override fun observeDashboard(): Flow<RoutineDashboardState> = dashboardState.asStateFlow()

    override suspend fun createRoutine(
        title: String,
        scheduleLabel: String,
        category: RoutineCategory,
        recurrence: RoutineRecurrence,
        description: String
    ) {
        val routine = RoutineItem(
            id = UUID.randomUUID().toString(),
            title = title,
            scheduleLabel = scheduleLabel,
            category = category,
            recurrence = recurrence,
            streakDays = 0,
            description = description
        )
        dashboardState.update { state ->
            state.copy(routines = state.routines + routine)
        }
        taskRepository.createTaskFromRoutine(routine)
    }

    private fun buildDashboardState(strings: FakeSeedStrings): RoutineDashboardState {
        return RoutineDashboardState(
            userName = strings.sampleUserName,
            dateLabel = currentDateLabel(),
            highlight = strings.homeHighlight,
            routines = buildSampleRoutines(strings)
        )
    }

    private fun currentDateLabel(): String {
        val formatter = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        return formatter.format(Date())
    }
}
