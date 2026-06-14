package com.nai.routinetracker.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nai.routinetracker.R
import com.nai.routinetracker.domain.settings.ReminderTime
import com.nai.routinetracker.ui.theme.RoutineTrackerTheme
import java.util.Locale

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onReminderEnabledChanged: (Boolean) -> Unit,
    onReminderTimeChanged: (ReminderTime) -> Unit,
    onResetLocalDataConfirmed: () -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showReminderTimeDialog by rememberSaveable { mutableStateOf(false) }
    var showResetDialog by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(
                start = 20.dp,
                top = 20.dp,
                end = 20.dp,
                bottom = 28.dp
            ),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                SettingsHeader()
            }

            item {
                SettingsSection(
                    title = stringResource(R.string.settings_reminders_section),
                    icon = Icons.Outlined.Notifications
                ) {
                    PreferenceRow(
                        icon = Icons.Outlined.Notifications,
                        title = stringResource(R.string.settings_reminder_enabled_title),
                        body = stringResource(R.string.settings_reminder_enabled_body),
                        trailing = {
                            Switch(
                                checked = state.reminderEnabled,
                                onCheckedChange = onReminderEnabledChanged
                            )
                        }
                    )
                    HorizontalDivider()
                    PreferenceRow(
                        icon = Icons.Outlined.AccessTime,
                        title = stringResource(R.string.settings_reminder_time_title),
                        body = state.reminderTime.formatForDisplay(),
                        onClick = { showReminderTimeDialog = true }
                    )
                }
            }

            item {
                SettingsSection(
                    title = stringResource(R.string.settings_local_data_section),
                    icon = Icons.Outlined.Storage
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.settings_reset_data_title),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stringResource(R.string.settings_reset_data_body),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        state.resetMessage?.let { resetMessage ->
                            Text(
                                text = stringResource(
                                    when (resetMessage) {
                                        SettingsResetMessage.Success -> {
                                            R.string.settings_reset_success
                                        }
                                        SettingsResetMessage.Error -> {
                                            R.string.settings_reset_error
                                        }
                                    }
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = when (resetMessage) {
                                    SettingsResetMessage.Success -> {
                                        MaterialTheme.colorScheme.primary
                                    }
                                    SettingsResetMessage.Error -> {
                                        MaterialTheme.colorScheme.error
                                    }
                                }
                            )
                        }
                        Button(
                            onClick = { showResetDialog = true },
                            enabled = !state.isResettingLocalData,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            )
                        ) {
                            if (state.isResettingLocalData) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = MaterialTheme.colorScheme.onError,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Outlined.RestartAlt,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = stringResource(R.string.settings_reset_data_action))
                        }
                    }
                }
            }

            item {
                SettingsSection(
                    title = stringResource(R.string.settings_app_info_section),
                    icon = Icons.Outlined.Info
                ) {
                    PreferenceRow(
                        icon = Icons.Outlined.Info,
                        title = stringResource(R.string.app_name),
                        body = stringResource(R.string.settings_app_info_body)
                    )
                    HorizontalDivider()
                    PreferenceRow(
                        icon = Icons.Outlined.Info,
                        title = stringResource(R.string.settings_app_version_title),
                        body = stringResource(R.string.settings_app_version_value)
                    )
                }
            }

            item {
                SettingsSection(
                    title = stringResource(R.string.settings_account_section),
                    icon = Icons.AutoMirrored.Outlined.Logout
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.settings_sign_out_body),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedButton(onClick = onSignOutClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.Logout,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = stringResource(R.string.settings_sign_out_action))
                        }
                    }
                }
            }
        }
    }

    if (showReminderTimeDialog) {
        ReminderTimeDialog(
            selectedTime = state.reminderTime,
            onTimeSelected = { selectedTime ->
                onReminderTimeChanged(selectedTime)
                showReminderTimeDialog = false
            },
            onDismiss = {
                showReminderTimeDialog = false
            }
        )
    }

    if (showResetDialog) {
        ResetLocalDataDialog(
            onConfirm = {
                showResetDialog = false
                onResetLocalDataConfirmed()
            },
            onDismiss = {
                showResetDialog = false
            }
        )
    }
}

@Composable
private fun SettingsHeader() {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_screen_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.settings_screen_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(7.dp)
                        .size(16.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        ) {
            Column(content = content)
        }
    }
}

@Composable
private fun PreferenceRow(
    icon: ImageVector,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    val rowModifier = if (onClick != null) {
        modifier.clickable(onClick = onClick)
    } else {
        modifier
    }

    Row(
        modifier = rowModifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        trailing?.invoke()
    }
}

@Composable
private fun ReminderTimeDialog(
    selectedTime: ReminderTime,
    onTimeSelected: (ReminderTime) -> Unit,
    onDismiss: () -> Unit
) {
    val options = remember(selectedTime) {
        (listOf(selectedTime) + reminderTimeOptions).distinct()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.settings_reminder_time_dialog_title))
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTimeSelected(option) }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = option == selectedTime,
                            onClick = { onTimeSelected(option) }
                        )
                        Text(
                            text = option.formatForDisplay(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.settings_dialog_done))
            }
        }
    )
}

@Composable
private fun ResetLocalDataDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.settings_reset_confirm_title))
        },
        text = {
            Text(text = stringResource(R.string.settings_reset_confirm_body))
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(text = stringResource(R.string.settings_reset_confirm_action))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.settings_reset_cancel_action))
            }
        }
    )
}

private fun ReminderTime.formatForDisplay(): String {
    val period = if (hour < 12) "AM" else "PM"
    val displayHour = when (val hourOnClock = hour % 12) {
        0 -> 12
        else -> hourOnClock
    }
    return String.format(Locale.US, "%d:%02d %s", displayHour, minute, period)
}

private val reminderTimeOptions = listOf(
    ReminderTime(hour = 6, minute = 30),
    ReminderTime(hour = 7, minute = 0),
    ReminderTime(hour = 8, minute = 0),
    ReminderTime(hour = 12, minute = 0),
    ReminderTime(hour = 20, minute = 30)
)

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun SettingsScreenPreview() {
    RoutineTrackerTheme {
        SettingsScreen(
            state = SettingsUiState(
                reminderEnabled = true,
                reminderTime = ReminderTime(hour = 7, minute = 0)
            ),
            onReminderEnabledChanged = {},
            onReminderTimeChanged = {},
            onResetLocalDataConfirmed = {},
            onSignOutClick = {}
        )
    }
}
