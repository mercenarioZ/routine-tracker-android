package com.nai.routinetracker.ui.settings

import com.nai.routinetracker.domain.settings.ReminderTime
import com.nai.routinetracker.domain.settings.ThemeMode

data class SettingsUiState(
    val reminderEnabled: Boolean = false,
    val reminderTime: ReminderTime = ReminderTime.Default,
    val themeMode: ThemeMode = ThemeMode.System
)
