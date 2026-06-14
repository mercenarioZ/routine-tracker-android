package com.nai.routinetracker.data.repository

import com.nai.routinetracker.model.RoutineCategories
import com.nai.routinetracker.model.RoutineRecurrence
import com.nai.routinetracker.model.RoutineWeekday
import com.nai.routinetracker.model.TaskStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FakeRoutineRepositoryTest {
    private val seedStringProvider = StaticFakeSeedStringProvider()

    @Test
    fun createRoutine_addsRoutineAndGeneratesPendingTask() = runBlocking {
        val taskRepository = FakeTaskRepository(seedStringProvider)
        val routineRepository = FakeRoutineRepository(
            seedStringProvider = seedStringProvider,
            taskRepository = taskRepository
        )
        val initialRoutineCount = routineRepository.observeDashboard().first().routines.size
        val initialTaskCount = taskRepository.observeTasks().first().size

        routineRepository.createRoutine(
            title = "Evening reset",
            scheduleLabel = "08:30 PM",
            category = RoutineCategories.Planning,
            recurrence = RoutineRecurrence.Weekly(RoutineWeekday.Friday),
            description = "Tidy the desk and plan tomorrow."
        )

        val createdRoutine = routineRepository
            .observeDashboard()
            .first()
            .routines
            .single { it.title == "Evening reset" }
        val generatedTask = taskRepository
            .observeTasks()
            .first()
            .single { it.routineId == createdRoutine.id }

        assertEquals(initialRoutineCount + 1, routineRepository.observeDashboard().first().routines.size)
        assertEquals("08:30 PM", createdRoutine.scheduleLabel)
        assertEquals(RoutineCategories.Planning, createdRoutine.category)
        assertEquals(RoutineRecurrence.Weekly(RoutineWeekday.Friday), createdRoutine.recurrence)
        assertEquals("Tidy the desk and plan tomorrow.", createdRoutine.description)
        assertEquals(0, createdRoutine.streakDays)

        assertEquals(initialTaskCount + 1, taskRepository.observeTasks().first().size)
        assertNotNull(generatedTask.routineId)
        assertEquals(createdRoutine.id, generatedTask.routineId)
        assertEquals(createdRoutine.title, generatedTask.title)
        assertEquals(createdRoutine.scheduleLabel, generatedTask.timeLabel)
        assertEquals(createdRoutine.category.id, generatedTask.category.id)
        assertEquals(createdRoutine.category.label, generatedTask.category.label)
        assertEquals(createdRoutine.description, generatedTask.description)
        assertEquals(TaskStatus.Pending, generatedTask.status)
        assertEquals("Today", generatedTask.dueLabel)
    }

    @Test
    fun createRoutine_keepsSeedTasksAndRoutinesAvailable() = runBlocking {
        val taskRepository = FakeTaskRepository(seedStringProvider)
        val routineRepository = FakeRoutineRepository(
            seedStringProvider = seedStringProvider,
            taskRepository = taskRepository
        )

        assertTrue(routineRepository.observeDashboard().first().routines.any { it.id == "hydration" })
        assertEquals(
            RoutineRecurrence.Daily,
            routineRepository.observeDashboard().first().routines.single { it.id == "hydration" }.recurrence
        )
        assertTrue(taskRepository.observeTasks().first().any { it.id == "task-hydration-today" })
    }
}

private class StaticFakeSeedStringProvider : FakeSeedStringProvider {
    override fun strings(): FakeSeedStrings {
        return FakeSeedStrings(
            sampleUserName = "Builder",
            homeHighlight = "Small routines create stable days.",
            hydrationTitle = "Hydrate and stretch",
            hydrationSchedule = "06:30 AM",
            hydrationDescription = "Drink water and stretch.",
            planningTitle = "Plan top 3 tasks",
            planningSchedule = "07:00 AM",
            planningDescription = "Write the three outcomes.",
            focusTitle = "Deep work block",
            focusSchedule = "09:00 AM",
            focusDescription = "Focus on the hardest task.",
            learningTitle = "Android learning sprint",
            learningSchedule = "08:30 PM",
            learningDescription = "Improve one native Android skill.",
            taskTodayDueLabel = "Today",
            taskTomorrowDueLabel = "Tomorrow"
        )
    }
}
