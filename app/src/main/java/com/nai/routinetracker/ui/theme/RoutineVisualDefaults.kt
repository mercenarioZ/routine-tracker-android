package com.nai.routinetracker.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object RoutineVisualDefaults {
    val CardShape = RoundedCornerShape(8.dp)
    val CompactShape = RoundedCornerShape(6.dp)
    val PillShape = CircleShape
    val ScreenHorizontalPadding = 20.dp
    val CardPadding = 16.dp

    @Composable
    fun categoryAccent(categoryId: String): Color {
        return when (categoryId) {
            "health", "personal" -> LeafWash
            "focus", "work" -> MorningBlue
            "learning" -> Sky80.copy(alpha = 0.5f)
            "planning", "admin" -> Sunrise80.copy(alpha = 0.9f)
            else -> MaterialTheme.colorScheme.secondaryContainer
        }
    }

    @Composable
    fun onCategoryAccent(categoryId: String): Color {
        return when (categoryId) {
            "focus", "work" -> Sky40
            "planning", "admin" -> InkSoft
            "health", "personal" -> Evergreen40
            "learning" -> Sky40
            else -> MaterialTheme.colorScheme.onSecondaryContainer
        }
    }
}
