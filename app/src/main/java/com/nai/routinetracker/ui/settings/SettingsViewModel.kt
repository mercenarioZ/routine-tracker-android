package com.nai.routinetracker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nai.routinetracker.domain.repository.SettingsRepository
import com.nai.routinetracker.domain.settings.ReminderTime
import com.nai.routinetracker.domain.settings.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val uiState = settingsRepository.observeSettings()
        .map { settings ->
            SettingsUiState(
                reminderEnabled = settings.reminderEnabled,
                reminderTime = settings.reminderTime,
                themeMode = settings.themeMode
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState()
        )

    fun onReminderEnabledChanged(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setReminderEnabled(enabled)
        }
    }

    fun onReminderTimeChanged(time: ReminderTime) {
        viewModelScope.launch {
            settingsRepository.setReminderTime(time)
        }
    }

    fun onThemeModeChanged(themeMode: ThemeMode) {
        viewModelScope.launch {
            settingsRepository.setThemeMode(themeMode)
        }
    }
}
