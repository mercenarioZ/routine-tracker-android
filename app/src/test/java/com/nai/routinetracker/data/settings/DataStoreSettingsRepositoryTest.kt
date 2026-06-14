package com.nai.routinetracker.data.settings

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.nai.routinetracker.domain.settings.ReminderTime
import com.nai.routinetracker.domain.settings.ThemeMode
import com.nai.routinetracker.domain.settings.UserSettings
import java.io.File
import java.nio.file.Files
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DataStoreSettingsRepositoryTest {
    private lateinit var tempDir: File
    private lateinit var dataStoreScope: CoroutineScope

    @Before
    fun setUp() {
        tempDir = Files.createTempDirectory("settings-repository-test").toFile()
    }

    @After
    fun tearDown() {
        if (::dataStoreScope.isInitialized) {
            dataStoreScope.cancel()
        }
        tempDir.deleteRecursively()
    }

    @Test
    fun observeSettings_emitsDefaultReminderSettings() = runBlocking {
        val repository = repositoryFor("default.preferences_pb")

        val settings = repository.observeSettings().first()

        assertEquals(UserSettings(), settings)
    }

    @Test
    fun setReminderPreferences_persistsReminderSettings() = runBlocking {
        val repository = repositoryFor("reminders.preferences_pb")
        val selectedTime = ReminderTime(hour = 20, minute = 30)

        repository.setReminderEnabled(true)
        repository.setReminderTime(selectedTime)

        val settings = repository.observeSettings().first()

        assertTrue(settings.reminderEnabled)
        assertEquals(selectedTime, settings.reminderTime)
    }

    @Test
    fun setThemeMode_persistsThemeMode() = runBlocking {
        val repository = repositoryFor("theme.preferences_pb")

        repository.setThemeMode(ThemeMode.Dark)

        val settings = repository.observeSettings().first()

        assertEquals(ThemeMode.Dark, settings.themeMode)
    }

    private fun repositoryFor(fileName: String): DataStoreSettingsRepository {
        dataStoreScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        val dataStore = PreferenceDataStoreFactory.create(
            scope = dataStoreScope,
            produceFile = {
                File(tempDir, fileName)
            }
        )
        return DataStoreSettingsRepository(dataStore)
    }
}
