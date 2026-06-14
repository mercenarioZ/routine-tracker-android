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

private val DarkColorScheme = darkColorScheme(
    primary = Evergreen80,
    secondary = Forest80,
    tertiary = Clay80,
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
    primary = Evergreen40,
    secondary = Forest40,
    tertiary = Clay40,
    background = Mist,
    surface = Linen,
    primaryContainer = ColorTokens.primaryContainerLight,
    secondaryContainer = ColorTokens.secondaryContainerLight,
    tertiaryContainer = ColorTokens.tertiaryContainerLight,
    onPrimary = Card,
    onSecondary = Card,
    onTertiary = Card,
    onBackground = Ink,
    onSurface = Ink,
    onSurfaceVariant = InkSoft,
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
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = RoutineShapes,
        content = content
    )
}

private object ColorTokens {
    val primaryContainerLight = Evergreen80.copy(alpha = 0.45f)
    val secondaryContainerLight = Forest80.copy(alpha = 0.4f)
    val tertiaryContainerLight = Sunrise80.copy(alpha = 0.45f)
    val surfaceVariantLight = MorningBlue.copy(alpha = 0.75f)
    val outlineLight = OutlineSoft
    val outlineVariantLight = OutlineSoft.copy(alpha = 0.72f)
    val primaryContainerDark = Evergreen40.copy(alpha = 0.45f)
    val secondaryContainerDark = Forest40.copy(alpha = 0.55f)
    val tertiaryContainerDark = Clay40.copy(alpha = 0.55f)
    val surfaceVariantDark = Color(0xFF2A3631)
    val surfaceDark = Color(0xFF1E2824)
    val onSurfaceVariantDark = Color(0xFFC4CFCA)
    val outlineDark = Color(0xFF53615B)
    val outlineVariantDark = Color(0xFF36443E)
}
