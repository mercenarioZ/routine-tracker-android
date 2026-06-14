package com.nai.routinetracker.domain.repository

import com.nai.routinetracker.domain.settings.ReminderTime
import com.nai.routinetracker.domain.settings.UserSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeSettings(): Flow<UserSettings>

    suspend fun setReminderEnabled(enabled: Boolean)

    suspend fun setReminderTime(time: ReminderTime)
}
