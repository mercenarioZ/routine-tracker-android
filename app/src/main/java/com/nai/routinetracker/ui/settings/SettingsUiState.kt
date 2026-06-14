package com.nai.routinetracker.ui.settings

import com.nai.routinetracker.domain.settings.ReminderTime

data class SettingsUiState(
    val reminderEnabled: Boolean = false,
    val reminderTime: ReminderTime = ReminderTime.Default,
    val isResettingLocalData: Boolean = false,
    val resetMessage: SettingsResetMessage? = null
)

enum class SettingsResetMessage {
    Success,
    Error
}
