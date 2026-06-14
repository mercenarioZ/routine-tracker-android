package com.nai.routinetracker.ui.routines

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import com.nai.routinetracker.model.RoutineRecurrence
import com.nai.routinetracker.model.RoutineWeekday

class CreateRoutineUiStateTest {
    @Test
    fun defaultScheduleLabel_allowsSubmitWhenOtherRequiredFieldsAreFilled() {
        val state = CreateRoutineUiState(
            title = "Morning walk",
            description = "Walk around the block before work."
        )

        assertEquals(DefaultRoutineScheduleLabel, state.scheduleLabel)
        assertTrue(state.canSubmit)
    }

    @Test
    fun customRecurrence_requiresAtLeastOneWeekday() {
        val state = CreateRoutineUiState(
            title = "Morning walk",
            description = "Walk around the block before work.",
            recurrenceMode = CreateRoutineRecurrenceMode.Custom,
            selectedWeekdays = emptySet()
        )

        assertFalse(state.canSubmit)
    }

    @Test
    fun weeklyRecurrence_usesSelectedWeekday() {
        val state = CreateRoutineUiState(
            title = "Morning walk",
            description = "Walk around the block before work.",
            recurrenceMode = CreateRoutineRecurrenceMode.Weekly,
            selectedWeekdays = setOf(RoutineWeekday.Friday)
        )

        assertEquals(RoutineRecurrence.Weekly(RoutineWeekday.Friday), state.recurrence)
        assertTrue(state.canSubmit)
    }
}
