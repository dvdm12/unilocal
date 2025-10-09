package com.example.unilocal.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.unilocal.ui.config.RouteScreen
import com.example.unilocal.ui.screens.user.NavHomeUser

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

        composable<RouteScreen.Login>{
            Login(
                onLoginClick = {
                    navController.navigate(RouteScreen.NavHomeUser) // Navigate to User Screen
                },
                onRegisterClick = {
                    navController.navigate(RouteScreen.Register) // Navigate to Register screen
                },
                onBackClick = {
                    navController.navigate(RouteScreen.WelcomeScreen)
                },
                onForgotPassword={
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
            NavHomeUser(navController) // Show User screen
        }

        composable<RouteScreen.ForgotPasswordScreen>{
            ForgotPasswordScreen(
                onBackToLogin = {
                    navController.navigate(RouteScreen.Login)
                }
            )
        }
    }
}
