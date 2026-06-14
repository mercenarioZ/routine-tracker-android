package com.nai.routinetracker.domain.settings

data class UserSettings(
    val reminderEnabled: Boolean = false,
    val reminderTime: ReminderTime = ReminderTime.Default
)
