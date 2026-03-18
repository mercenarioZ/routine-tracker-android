package com.nai.routinetracker.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nai.routinetracker.data.repository.FakeRoutineRepository
import com.nai.routinetracker.ui.home.HomeRoute
import com.nai.routinetracker.ui.home.HomeViewModel
import com.nai.routinetracker.ui.settings.SettingsScreen
import com.nai.routinetracker.ui.stats.StatsScreen
import com.nai.routinetracker.ui.theme.RoutineTrackerTheme

@Composable
fun AppNavHost(homeViewModel: HomeViewModel) {
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
                HomeRoute(viewModel = homeViewModel)
            }
            composable(route = AppDestination.Stats.route) {
                StatsScreen()
            }
            composable(route = AppDestination.Settings.route) {
                SettingsScreen()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AppNavHostPreview() {
    val context = LocalContext.current
    val previewViewModel = remember {
        HomeViewModel(
            repository = FakeRoutineRepository(context)
        )
    }

    RoutineTrackerTheme {
        AppNavHost(homeViewModel = previewViewModel)
    }
}
