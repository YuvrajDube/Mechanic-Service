package com.example.mechanicservice.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mechanicservice.ui.auth.LoginScreen
import com.example.mechanicservice.ui.auth.SignupScreen

private const val LOGIN_ROUTE = "login"
private const val SIGNUP_ROUTE = "signup"
private const val HOME_ROUTE = "home"

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LOGIN_ROUTE
    ) {

        composable(LOGIN_ROUTE) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(HOME_ROUTE) {
                        popUpTo(LOGIN_ROUTE) {
                            inclusive = true
                        }
                    }
                },
                onSignupClick = {
                    navController.navigate(SIGNUP_ROUTE)
                }
            )
        }

        composable(SIGNUP_ROUTE) {
            SignupScreen(
                onSignupSuccess = {
                    navController.navigate(LOGIN_ROUTE) {
                        popUpTo(SIGNUP_ROUTE) {
                            inclusive = true
                        }
                    }
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        // Temporary destination.
        // We'll replace this with the actual HomeScreen later.
        composable(HOME_ROUTE) {
            // Empty for now
        }
    }
}