package com.nai.routinetracker.domain.settings

data class ReminderTime(
    val hour: Int,
    val minute: Int
) {
    init {
        require(hour in HOURS_RANGE) { "Reminder hour must be between 0 and 23." }
        require(minute in MINUTES_RANGE) { "Reminder minute must be between 0 and 59." }
    }

    val minutesFromMidnight: Int
        get() = hour * MINUTES_PER_HOUR + minute

    companion object {
        private const val MINUTES_PER_HOUR = 60
        private const val MAX_MINUTES_FROM_MIDNIGHT = 23 * MINUTES_PER_HOUR + 59
        private val HOURS_RANGE = 0..23
        private val MINUTES_RANGE = 0..59

        val Default = ReminderTime(hour = 7, minute = 0)

        fun fromMinutesFromMidnight(minutes: Int): ReminderTime {
            val boundedMinutes = minutes.coerceIn(0, MAX_MINUTES_FROM_MIDNIGHT)
            return ReminderTime(
                hour = boundedMinutes / MINUTES_PER_HOUR,
                minute = boundedMinutes % MINUTES_PER_HOUR
            )
        }
    }
}
