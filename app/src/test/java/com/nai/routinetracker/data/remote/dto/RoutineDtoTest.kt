package com.nai.routinetracker.data.remote.dto

import com.nai.routinetracker.model.RoutineRecurrence
import com.nai.routinetracker.model.RoutineWeekday
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.junit.Assert.assertEquals
import org.junit.Test

class RoutineDtoTest {
    @Test
    fun fromJson_parsesWeeklyRecurrence() {
        val dto = RoutineDto.fromJson(
            Json.parseToJsonElement(
                """
                {
                  "id": "focus",
                  "title": "Deep work",
                  "frequency": "weekly",
                  "daysOfWeek": ["monday"]
                }
                """.trimIndent()
            ).jsonObject
        )

        assertEquals(RoutineRecurrence.Weekly(RoutineWeekday.Monday), dto.toDomain().recurrence)
    }

    @Test
    fun fromJson_parsesCustomRecurrenceObject() {
        val dto = RoutineDto.fromJson(
            Json.parseToJsonElement(
                """
                {
                  "id": "learning",
                  "title": "Learning sprint",
                  "recurrence": {
                    "type": "custom",
                    "weekdays": [2, "thu"]
                  }
                }
                """.trimIndent()
            ).jsonObject
        )

        assertEquals(
            RoutineRecurrence.Custom(setOf(RoutineWeekday.Tuesday, RoutineWeekday.Thursday)),
            dto.toDomain().recurrence
        )
    }

    @Test
    fun fromJson_defaultsMissingRecurrenceToDaily() {
        val dto = RoutineDto.fromJson(
            Json.parseToJsonElement(
                """
                {
                  "id": "hydration",
                  "title": "Hydrate"
                }
                """.trimIndent()
            ).jsonObject
        )

        assertEquals(RoutineRecurrence.Daily, dto.toDomain().recurrence)
    }
}
