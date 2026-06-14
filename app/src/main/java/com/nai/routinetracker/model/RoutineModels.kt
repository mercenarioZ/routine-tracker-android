package com.nai.routinetracker.model

data class RoutineCategory(
    val id: String,
    val label: String,
    val isSystem: Boolean = true
)

object RoutineCategories {
    val Health = RoutineCategory(
        id = "health",
        label = "Health"
    )

    val Focus = RoutineCategory(
        id = "focus",
        label = "Focus"
    )

    val Learning = RoutineCategory(
        id = "learning",
        label = "Learning"
    )

    val Planning = RoutineCategory(
        id = "planning",
        label = "Planning"
    )

    val defaults = listOf(Health, Focus, Learning, Planning)
}

data class RoutineItem(
    val id: String,
    val title: String,
    val scheduleLabel: String,
    val category: RoutineCategory,
    val recurrence: RoutineRecurrence = RoutineRecurrence.Daily,
    val streakDays: Int,
    val isActive: Boolean = true,
    val description: String
)

data class RoutineDashboardState(
    val userName: String,
    val dateLabel: String,
    val highlight: String,
    val routines: List<RoutineItem>
)

enum class RoutineWeekday(
    val storageValue: Int,
    val shortLabel: String
) {
    Monday(storageValue = 1, shortLabel = "Mon"),
    Tuesday(storageValue = 2, shortLabel = "Tue"),
    Wednesday(storageValue = 3, shortLabel = "Wed"),
    Thursday(storageValue = 4, shortLabel = "Thu"),
    Friday(storageValue = 5, shortLabel = "Fri"),
    Saturday(storageValue = 6, shortLabel = "Sat"),
    Sunday(storageValue = 7, shortLabel = "Sun");

    companion object {
        fun fromStorageValue(value: Int): RoutineWeekday? {
            return entries.firstOrNull { it.storageValue == value }
        }

        fun fromMondayFirstIndex(index: Int): RoutineWeekday {
            return entries[index.coerceIn(0, entries.lastIndex)]
        }
    }
}

sealed interface RoutineRecurrence {
    fun matches(weekday: RoutineWeekday): Boolean

    data object Daily : RoutineRecurrence {
        override fun matches(weekday: RoutineWeekday): Boolean = true
    }

    data class Weekly(
        val weekday: RoutineWeekday
    ) : RoutineRecurrence {
        override fun matches(weekday: RoutineWeekday): Boolean {
            return this.weekday == weekday
        }
    }

    data class Custom(
        val weekdays: Set<RoutineWeekday>
    ) : RoutineRecurrence {
        override fun matches(weekday: RoutineWeekday): Boolean {
            return weekday in weekdays
        }
    }
}
