package com.example.unilocal.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.unilocal.ui.config.RouteScreen
import com.example.unilocal.ui.screens.user.NavHomeUser
import com.example.unilocal.viewmodel.LoginViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController() // Navigation controller for managing navigation stack

    NavHost(
        navController = navController,
        startDestination = RouteScreen.WelcomeScreen // Set initial screen
    ){
        composable<RouteScreen.WelcomeScreen> {
            WelcomeScreen(
                onLoginClick = {
                    navController.navigate(RouteScreen.Login) // Navigate to Login screen
                },
                onRegisterClick = {
                    navController.navigate(RouteScreen.Register) // Navigate to Register screen
                }
            )
        }

        composable<RouteScreen.Login> {
            Login(
                onLoginSuccessUser = {
                    navController.navigate(RouteScreen.NavHomeUser) {
                        popUpTo(RouteScreen.Login) { inclusive = true }
                    }
                },
                onLoginSuccessModerator = {
                    /*navController.navigate(RouteScreen.NavHomeModerator.route) {
                        popUpTo(RouteScreen.Login.route) { inclusive = true }
                    }*/
                },
                onRegisterClick = {
                    navController.navigate(RouteScreen.Register)
                },
                onBackClick = {
                    navController.navigate(RouteScreen.WelcomeScreen)
                },
                onForgotPassword = {
                    navController.navigate(RouteScreen.ForgotPasswordScreen)
                }
            )
        }

        composable<RouteScreen.Register>{
            Register(
                onBackClick = {
                    navController.navigate(RouteScreen.WelcomeScreen)
                }
            ) // Show Register screen
        }

        composable<RouteScreen.NavHomeUser>{
            NavHomeUser(navController)
        }

        composable<RouteScreen.ForgotPasswordScreen> {
            val loginViewModel: LoginViewModel = viewModel()

            ForgotPasswordScreen(
                viewModel = loginViewModel,
                onBackToLogin = {
                    navController.navigate(RouteScreen.Login)
                }
            )
        }
    }
}
