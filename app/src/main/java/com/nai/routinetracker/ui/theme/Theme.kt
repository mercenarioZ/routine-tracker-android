package com.nai.routinetracker.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nai.routinetracker.domain.settings.ThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = Sunrise80,
    secondary = Evergreen80,
    tertiary = Sky80,
    background = Ink,
    surface = ColorTokens.surfaceDark,
    primaryContainer = ColorTokens.primaryContainerDark,
    secondaryContainer = ColorTokens.secondaryContainerDark,
    tertiaryContainer = ColorTokens.tertiaryContainerDark,
    onPrimary = Ink,
    onSecondary = Ink,
    onTertiary = Ink,
    onBackground = Mist,
    onSurface = Mist,
    onSurfaceVariant = ColorTokens.onSurfaceVariantDark,
    onPrimaryContainer = Mist,
    onSecondaryContainer = Mist,
    onTertiaryContainer = Mist,
    surfaceVariant = ColorTokens.surfaceVariantDark,
    outline = ColorTokens.outlineDark,
    outlineVariant = ColorTokens.outlineVariantDark
)

private val LightColorScheme = lightColorScheme(
    primary = Sunrise40,
    secondary = Forest40,
    tertiary = Evergreen40,
    background = Mist,
    surface = Linen,
    primaryContainer = ColorTokens.primaryContainerLight,
    secondaryContainer = ColorTokens.secondaryContainerLight,
    tertiaryContainer = ColorTokens.tertiaryContainerLight,
    onPrimary = Card,
    onSecondary = Card,
    onTertiary = Card,
    onBackground = ColorTokens.onSurfaceLight,
    onSurface = ColorTokens.onSurfaceLight,
    onSurfaceVariant = ColorTokens.onSurfaceVariantLight,
    onPrimaryContainer = Ink,
    onSecondaryContainer = Ink,
    onTertiaryContainer = Ink,
    surfaceVariant = ColorTokens.surfaceVariantLight,
    outline = ColorTokens.outlineLight,
    outlineVariant = ColorTokens.outlineVariantLight
)

private val RoutineShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(6.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(8.dp),
    extraLarge = RoundedCornerShape(8.dp)
)

@Composable
fun RoutineTrackerTheme(
    themeMode: ThemeMode = ThemeMode.System,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = RoutineShapes,
        content = content
    )
}

private object ColorTokens {
    val primaryContainerLight = Sunrise80.copy(alpha = 0.58f)
    val secondaryContainerLight = LeafWash
    val tertiaryContainerLight = Evergreen80.copy(alpha = 0.34f)
    val surfaceVariantLight = MorningBlue.copy(alpha = 0.75f)
    val onSurfaceLight = Color(0xFF2D3834)
    val onSurfaceVariantLight = Color(0xFF6B756F)
    val outlineLight = OutlineSoft
    val outlineVariantLight = OutlineSoft.copy(alpha = 0.72f)
    val primaryContainerDark = Sunrise40.copy(alpha = 0.5f)
    val secondaryContainerDark = Evergreen40.copy(alpha = 0.5f)
    val tertiaryContainerDark = Sky40.copy(alpha = 0.5f)
    val surfaceVariantDark = Color(0xFF303731)
    val surfaceDark = Color(0xFF252B26)
    val onSurfaceVariantDark = Color(0xFFD6DDD6)
    val outlineDark = Color(0xFF53615B)
    val outlineVariantDark = Color(0xFF36443E)
}
