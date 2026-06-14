package com.nai.routinetracker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nai.routinetracker.domain.repository.LocalDataRepository
import com.nai.routinetracker.domain.repository.SettingsRepository
import com.nai.routinetracker.domain.settings.ReminderTime
import com.nai.routinetracker.domain.settings.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val localDataRepository: LocalDataRepository
) : ViewModel() {
    private val resetState = MutableStateFlow(SettingsResetState())

    val uiState = combine(
        settingsRepository.observeSettings(),
        resetState
    ) { settings, reset ->
        SettingsUiState(
            reminderEnabled = settings.reminderEnabled,
            reminderTime = settings.reminderTime,
            themeMode = settings.themeMode,
            isResettingLocalData = reset.isResetting,
            resetMessage = reset.message
        )
    }.stateIn(
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

    fun onResetLocalDataConfirmed() {
        if (resetState.value.isResetting) return

        viewModelScope.launch {
            resetState.value = SettingsResetState(isResetting = true)
            val message = runCatching {
                localDataRepository.resetLocalData()
            }.fold(
                onSuccess = { SettingsResetMessage.Success },
                onFailure = { SettingsResetMessage.Error }
            )
            resetState.value = SettingsResetState(message = message)
        }
    }

    fun onResetMessageConsumed() {
        resetState.update { it.copy(message = null) }
    }

    private data class SettingsResetState(
        val isResetting: Boolean = false,
        val message: SettingsResetMessage? = null
    )
}
