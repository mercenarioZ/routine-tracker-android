package com.nai.routinetracker.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.nai.routinetracker.R

sealed class AppDestination(
    val route: String,
    @param:StringRes val labelRes: Int,
    val icon: ImageVector
) {
    data object Home : AppDestination(
        route = "home",
        labelRes = R.string.tab_home,
        icon = Icons.Outlined.Home
    )

    data object Stats : AppDestination(
        route = "stats",
        labelRes = R.string.tab_stats,
        icon = Icons.Outlined.BarChart
    )

    data object Settings : AppDestination(
        route = "settings",
        labelRes = R.string.tab_settings,
        icon = Icons.Outlined.Settings
    )
}

val bottomBarDestinations = listOf(
    AppDestination.Home,
    AppDestination.Stats,
    AppDestination.Settings
)
