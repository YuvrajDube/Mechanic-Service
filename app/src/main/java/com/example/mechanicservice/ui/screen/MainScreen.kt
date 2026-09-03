package com.example.mechanicservice.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mechanicservice.ui.screen.ServiceRequestsScreen
import com.example.mechanicservice.ui.screen.SettingsScreen

private const val HOME_ROUTE = "main_home"
private const val REQUESTS_ROUTE = "service_requests"
private const val SETTINGS_ROUTE = "settings"

@Composable
fun MainScreen(
    onMechanicClick: (String) -> Unit,
    onLogout: () -> Unit
) {

    val navController = rememberNavController()

    val currentBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        currentBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {

            NavigationBar {

                NavigationBarItem(
                    selected = currentRoute == HOME_ROUTE,

                    onClick = {
                        navController.navigate(HOME_ROUTE) {

                            popUpTo(
                                navController.graph.findStartDestination().id
                            ) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    },

                    label = {
                        Text("Home")
                    }
                )

                NavigationBarItem(
                    selected = currentRoute == REQUESTS_ROUTE,

                    onClick = {
                        navController.navigate(REQUESTS_ROUTE) {

                            popUpTo(
                                navController.graph.findStartDestination().id
                            ) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    icon = {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = "Service Requests"
                        )
                    },

                    label = {
                        Text("Requests")
                    }
                )

                NavigationBarItem(
                    selected = currentRoute == SETTINGS_ROUTE,

                    onClick = {
                        navController.navigate(SETTINGS_ROUTE) {

                            popUpTo(
                                navController.graph.findStartDestination().id
                            ) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    },

                    label = {
                        Text("Settings")
                    }
                )
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = HOME_ROUTE,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(HOME_ROUTE) {

                HomeScreen(
                    onMechanicClick = onMechanicClick
                )
            }

            composable(REQUESTS_ROUTE) {

                ServiceRequestsScreen()
            }

            composable(SETTINGS_ROUTE) {

                SettingsScreen(
                    onLogout = onLogout
                )
            }
        }
    }
}