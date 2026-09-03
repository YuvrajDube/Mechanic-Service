package com.example.mechanicservice

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mechanicservice.ui.MainScreen
import com.example.mechanicservice.ui.MechanicDetailsScreen
import com.example.mechanicservice.ui.RequestServiceScreen
import com.example.mechanicservice.ui.auth.LoginScreen
import com.example.mechanicservice.ui.auth.SignupScreen
import com.example.mechanicservice.viewmodel.RequestServiceViewModel
import com.example.mechanicservice.viewmodel.RequestServiceViewModelFactory


private const val LOGIN_ROUTE = "login"
private const val SIGNUP_ROUTE = "signup"
private const val HOME_ROUTE = "home"
private const val MECHANIC_DETAILS_ROUTE = "mechanic"
private const val REQUEST_SERVICE_ROUTE = "request-service"

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val context = LocalContext.current
    val application =
        context.applicationContext as MechanicServiceApplication

    val startDestination =
        if (application.sessionManager.isLoggedIn()) {
            HOME_ROUTE
        } else {
            LOGIN_ROUTE
        }

    NavHost(
        navController = navController,
        startDestination = startDestination
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

        composable(HOME_ROUTE) {

            MainScreen(
                onMechanicClick = { mechanicId ->
                    navController.navigate(
                        "$MECHANIC_DETAILS_ROUTE/$mechanicId"
                    )
                },
                onLogout = {
                    application.sessionManager.clearSession()
                    navController.navigate(LOGIN_ROUTE) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = "$MECHANIC_DETAILS_ROUTE/{mechanicId}"
        ) { backStackEntry ->

            val mechanicId =
                backStackEntry.arguments?.getString("mechanicId")
                    ?: ""

            MechanicDetailsScreen(
                mechanicId = mechanicId,
                onBackClick = {
                    navController.popBackStack()
                },
                onRequestServiceClick = {
                    navController.navigate(
                        "$REQUEST_SERVICE_ROUTE/$mechanicId"
                    )
                }
            )
        }

        composable("$REQUEST_SERVICE_ROUTE/{mechanicId}") { backStackEntry ->

            val mechanicId =
                backStackEntry.arguments?.getString("mechanicId") ?: ""

            val context = LocalContext.current
            val application =
                context.applicationContext as MechanicServiceApplication

            val factory = RequestServiceViewModelFactory(
                application.sessionManager
            )

            val viewModel: RequestServiceViewModel = viewModel(
                factory = factory
            )

            RequestServiceScreen(
                mechanicId = mechanicId,
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }


    }
}