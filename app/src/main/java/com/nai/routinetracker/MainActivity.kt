package com.nai.routinetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nai.routinetracker.domain.repository.SettingsRepository
import com.nai.routinetracker.domain.settings.UserSettings
import com.nai.routinetracker.ui.navigation.AppNavHost
import com.nai.routinetracker.ui.theme.RoutineTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings by settingsRepository.observeSettings()
                .collectAsStateWithLifecycle(initialValue = UserSettings())

            RoutineTrackerTheme(themeMode = settings.themeMode) {
                AppNavHost()
            }
        }
    }
}
