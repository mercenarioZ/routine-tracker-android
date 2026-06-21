package com.nai.routinetracker.data.remote.dto

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
        fun activeRoutines(): RoutineQueryDto {
            return RoutineQueryDto(
                isActive = true,
                sortBy = "created_at",
                sortDirection = "desc"
            )
        }
    }
}
