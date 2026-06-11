package com.nai.routinetracker.data.remote.dto

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class RoutineQueryDto(
    val search: String? = null,
    val fromDate: String? = null,
    val toDate: String? = null,
    val isActive: Boolean? = true,
    val sortBy: String? = null,
    val sortDirection: String? = null
) {
    fun toQueryParameters(): Map<String, String> {
        return mapOf(
            "search" to search,
            "fromDate" to fromDate,
            "toDate" to toDate,
            "isActive" to isActive?.toString(),
            "sortBy" to sortBy,
            "sortDirection" to sortDirection
        ).filterValues { !it.isNullOrBlank() }
            .mapValues { it.value.orEmpty() }
    }

    companion object {
        fun activeForToday(): RoutineQueryDto {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
            return RoutineQueryDto(
                fromDate = today,
                toDate = today,
                isActive = true
            )
        }
    }
}
