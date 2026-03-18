package com.nai.routinetracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Evergreen80,
    secondary = Forest80,
    tertiary = Clay80,
    background = Ink,
    surface = ColorTokens.surfaceDark,
    primaryContainer = ColorTokens.primaryContainerDark,
    secondaryContainer = ColorTokens.secondaryContainerDark,
    onPrimary = Ink,
    onSecondary = Ink,
    onTertiary = Ink,
    onBackground = Mist,
    onSurface = Mist,
    onSurfaceVariant = ColorTokens.onSurfaceVariantDark,
    onPrimaryContainer = Mist,
    onSecondaryContainer = Mist
)

private val LightColorScheme = lightColorScheme(
    primary = Evergreen40,
    secondary = Forest40,
    tertiary = Clay40,
    background = Mist,
    surface = Card,
    primaryContainer = ColorTokens.primaryContainerLight,
    secondaryContainer = ColorTokens.secondaryContainerLight,
    onPrimary = Card,
    onSecondary = Card,
    onTertiary = Card,
    onBackground = Ink,
    onSurface = Ink,
    onSurfaceVariant = InkSoft,
    onPrimaryContainer = Ink,
    onSecondaryContainer = Ink
)

@Composable
fun RoutineTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

private object ColorTokens {
    val primaryContainerLight = Evergreen80.copy(alpha = 0.45f)
    val secondaryContainerLight = Forest80.copy(alpha = 0.4f)
    val primaryContainerDark = Evergreen40.copy(alpha = 0.45f)
    val secondaryContainerDark = Forest40.copy(alpha = 0.55f)
    val surfaceDark = Color(0xFF1E2824)
    val onSurfaceVariantDark = Color(0xFFC4CFCA)
}
