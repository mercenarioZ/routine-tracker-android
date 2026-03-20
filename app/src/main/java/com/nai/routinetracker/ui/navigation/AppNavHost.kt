package com.nai.routinetracker.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nai.routinetracker.ui.home.HomeRoute
import com.nai.routinetracker.ui.settings.SettingsRoute
import com.nai.routinetracker.ui.stats.StatsRoute
import com.nai.routinetracker.ui.theme.RoutineTrackerTheme

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomBarDestinations.forEach { destination ->
                    val isSelected = currentDestination
                        ?.hierarchy
                        ?.any { it.route == destination.route } == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(text = stringResource(destination.labelRes))
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = AppDestination.Home.route) {
                HomeRoute()
            }
            composable(route = AppDestination.Stats.route) {
                StatsRoute()
            }
            composable(route = AppDestination.Settings.route) {
                SettingsRoute()
            }
        }
    }
}

@Preview(name = "NavHost", device = Devices.PIXEL_7, showBackground = true, showSystemUi = false)
@Composable
private fun AppNavHostPreview() {
    RoutineTrackerTheme {
        AppNavHost()
    }
}