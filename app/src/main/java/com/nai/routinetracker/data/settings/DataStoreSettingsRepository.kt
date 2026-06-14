package com.nai.routinetracker.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nai.routinetracker.di.SettingsDataStore
import com.nai.routinetracker.domain.repository.SettingsRepository
import com.nai.routinetracker.domain.settings.ReminderTime
import com.nai.routinetracker.domain.settings.ThemeMode
import com.nai.routinetracker.domain.settings.UserSettings
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@Singleton
class DataStoreSettingsRepository @Inject constructor(
    @param:SettingsDataStore private val dataStore: DataStore<Preferences>
) : SettingsRepository {
    override fun observeSettings(): Flow<UserSettings> {
        return dataStore.data
            .catch { throwable ->
                if (throwable is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw throwable
                }
            }
            .map { preferences ->
                UserSettings(
                    reminderEnabled = preferences[REMINDER_ENABLED_KEY] ?: false,
                    reminderTime = ReminderTime.fromMinutesFromMidnight(
                        preferences[REMINDER_TIME_MINUTES_KEY]
                            ?: ReminderTime.Default.minutesFromMidnight
                    ),
                    themeMode = ThemeMode.fromStorageKey(preferences[THEME_MODE_KEY])
                )
            }
    }

    override suspend fun setReminderEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[REMINDER_ENABLED_KEY] = enabled
        }
    }

    override suspend fun setReminderTime(time: ReminderTime) {
        dataStore.edit { preferences ->
            preferences[REMINDER_TIME_MINUTES_KEY] = time.minutesFromMidnight
        }
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = themeMode.storageKey
        }
    }

    private companion object {
        val REMINDER_ENABLED_KEY = booleanPreferencesKey("reminder_enabled")
        val REMINDER_TIME_MINUTES_KEY = intPreferencesKey("reminder_time_minutes")
        val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
    }
}
