package com.example.unilocal.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.unilocal.ui.config.RouteScreen


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
            Login() // Show Login screen
        }

        composable<RouteScreen.Register>{
            Register() // Show Register screen
        }

        composable<RouteScreen.Moderator> {
            ModeratorScreen()
        }

    }
}
