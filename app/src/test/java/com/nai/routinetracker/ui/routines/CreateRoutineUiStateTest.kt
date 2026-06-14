package com.nai.routinetracker.ui.routines

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

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
}
