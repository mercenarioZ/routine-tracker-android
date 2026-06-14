package com.nai.routinetracker.ui.calendar

import com.nai.routinetracker.model.RoutineCategories
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.RoutineRecurrence
import com.nai.routinetracker.model.RoutineWeekday
import com.nai.routinetracker.model.TaskCategories
import com.nai.routinetracker.model.TaskItem
import com.nai.routinetracker.model.TaskStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CalendarUiStateTest {
    private val today = CalendarDate(year = 2026, month = 6, day = 14)

    @Test
    fun build_createsStableMonthGridAndMarksSelectedDate() {
        val state = CalendarStateFactory.build(
            today = today,
            visibleMonth = CalendarMonth(year = 2026, month = 6),
            selectedDate = today,
            routines = listOf(activeRoutine()),
            tasks = listOf(todayTask())
        )

        assertEquals(42, state.monthDays.size)
        assertEquals(CalendarDate(year = 2026, month = 6, day = 1), state.monthDays.first().date)
        assertEquals(CalendarDate(year = 2026, month = 7, day = 12), state.monthDays.last().date)

        val selectedDay = state.monthDays.single { it.date == today }
        assertTrue(selectedDay.isToday)
        assertTrue(selectedDay.isSelected)
        assertEquals(1, selectedDay.taskCount)
        assertEquals(1, selectedDay.routineCount)
    }

    @Test
    fun build_clampsSelectedDateWhenVisibleMonthHasFewerDays() {
        val state = CalendarStateFactory.build(
            today = CalendarDate(year = 2026, month = 1, day = 31),
            visibleMonth = CalendarMonth(year = 2026, month = 2),
            selectedDate = CalendarDate(year = 2026, month = 1, day = 31),
            routines = emptyList(),
            tasks = emptyList()
        )

        assertEquals(CalendarDate(year = 2026, month = 2, day = 28), state.selectedDate)
        assertTrue(state.monthDays.single { it.date == state.selectedDate }.isSelected)
    }

    @Test
    fun mapTasksToDates_mapsTodayAndTomorrowLabelsOnly() {
        val todayTask = todayTask()
        val tomorrowTask = tomorrowTask()
        val unmappedTask = todayTask.copy(id = "later", dueLabel = "Next week")

        val mapped = CalendarStateFactory.mapTasksToDates(
            tasks = listOf(todayTask, tomorrowTask, unmappedTask),
            today = today
        )

        assertEquals(listOf(todayTask), mapped[today])
        assertEquals(listOf(tomorrowTask), mapped[today.plusDays(1)])
        assertFalse(mapped.values.flatten().contains(unmappedTask))
    }

    @Test
    fun build_usesActiveRoutineContextOnly() {
        val inactiveRoutine = activeRoutine().copy(id = "paused", isActive = false)

        val state = CalendarStateFactory.build(
            today = today,
            visibleMonth = today.toMonth(),
            selectedDate = today,
            routines = listOf(activeRoutine(), inactiveRoutine),
            tasks = emptyList()
        )

        assertEquals(listOf(activeRoutine()), state.selectedRoutines)
        assertEquals(1, state.monthDays.single { it.date == today }.routineCount)
    }

    @Test
    fun build_filtersWeeklyRoutineBySelectedWeekday() {
        val weeklyRoutine = activeRoutine().copy(
            recurrence = RoutineRecurrence.Weekly(RoutineWeekday.Monday)
        )

        val sundayState = CalendarStateFactory.build(
            today = today,
            visibleMonth = today.toMonth(),
            selectedDate = today,
            routines = listOf(weeklyRoutine),
            tasks = emptyList()
        )
        val monday = CalendarDate(year = 2026, month = 6, day = 15)
        val mondayState = CalendarStateFactory.build(
            today = today,
            visibleMonth = today.toMonth(),
            selectedDate = monday,
            routines = listOf(weeklyRoutine),
            tasks = emptyList()
        )

        assertEquals(emptyList<RoutineItem>(), sundayState.selectedRoutines)
        assertEquals(0, sundayState.monthDays.single { it.date == today }.routineCount)
        assertEquals(listOf(weeklyRoutine), mondayState.selectedRoutines)
        assertEquals(1, mondayState.monthDays.single { it.date == monday }.routineCount)
    }

    @Test
    fun build_filtersCustomRoutineBySelectedWeekdays() {
        val customRoutine = activeRoutine().copy(
            recurrence = RoutineRecurrence.Custom(
                setOf(RoutineWeekday.Tuesday, RoutineWeekday.Thursday)
            )
        )
        val tuesday = CalendarDate(year = 2026, month = 6, day = 16)
        val wednesday = CalendarDate(year = 2026, month = 6, day = 17)

        val tuesdayState = CalendarStateFactory.build(
            today = today,
            visibleMonth = today.toMonth(),
            selectedDate = tuesday,
            routines = listOf(customRoutine),
            tasks = emptyList()
        )
        val wednesdayState = CalendarStateFactory.build(
            today = today,
            visibleMonth = today.toMonth(),
            selectedDate = wednesday,
            routines = listOf(customRoutine),
            tasks = emptyList()
        )

        assertEquals(listOf(customRoutine), tuesdayState.selectedRoutines)
        assertEquals(1, tuesdayState.monthDays.single { it.date == tuesday }.routineCount)
        assertEquals(emptyList<RoutineItem>(), wednesdayState.selectedRoutines)
        assertEquals(0, wednesdayState.monthDays.single { it.date == wednesday }.routineCount)
    }

    private fun activeRoutine(): RoutineItem {
        return RoutineItem(
            id = "hydration",
            title = "Hydrate and stretch",
            scheduleLabel = "06:30 AM",
            category = RoutineCategories.Health,
            streakDays = 12,
            description = "Drink water and stretch."
        )
    }

    private fun todayTask(): TaskItem {
        return TaskItem(
            id = "task-today",
            routineId = "hydration",
            title = "Hydrate and stretch",
            timeLabel = "06:30 AM",
            dueLabel = "Today",
            category = TaskCategories.Personal,
            status = TaskStatus.Pending,
            description = "Drink water and stretch."
        )
    }

    private fun tomorrowTask(): TaskItem {
        return todayTask().copy(
            id = "task-tomorrow",
            dueLabel = "Tomorrow"
        )
    }
}
