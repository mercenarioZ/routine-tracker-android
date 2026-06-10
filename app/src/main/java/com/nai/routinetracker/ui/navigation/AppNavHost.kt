package com.nai.routinetracker.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nai.routinetracker.ui.auth.AuthViewModel
import com.nai.routinetracker.ui.home.HomeRoute
import com.nai.routinetracker.ui.login.LoginRoute
import com.nai.routinetracker.ui.routines.CreateRoutineRoute
import com.nai.routinetracker.ui.routines.RoutinesRoute
import com.nai.routinetracker.ui.settings.SettingsRoute
import com.nai.routinetracker.ui.stats.StatsRoute
import com.nai.routinetracker.ui.tasks.TasksRoute

@Composable
fun AppNavHost(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authUiState = authViewModel.uiState.collectAsStateWithLifecycle().value

    if (authUiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = backStackEntry?.destination
    val showBottomBar = bottomBarDestinations.any { destination ->
        currentDestination?.hierarchy?.any { it.route == destination.route } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
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
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (authUiState.isLoggedIn) {
                AppDestination.Home.route
            } else {
                AppDestination.Login.route
            },
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = AppDestination.Login.route) {
                LoginRoute(
                    onLoginSuccess = {
                        navController.navigate(AppDestination.Home.route) {
                            popUpTo(AppDestination.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(route = AppDestination.Home.route) {
                HomeRoute(
                    onLogoutClick = {
                        authViewModel.logout()
                        navController.navigate(AppDestination.Login.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(route = AppDestination.Routines.route) {
                RoutinesRoute(
                    onCreateRoutineClick = {
                        navController.navigate(AppDestination.CreateRoutine.route)
                    }
                )
            }
            composable(route = AppDestination.CreateRoutine.route) {
                CreateRoutineRoute(
                    onRoutineCreated = {
                        navController.popBackStack()
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable(route = AppDestination.Tasks.route) {
                TasksRoute()
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
