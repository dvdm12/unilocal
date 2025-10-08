package com.example.unilocal.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.unilocal.ui.config.RouteScreen


@Composable
fun Navigation() {
    val navController = rememberNavController() // Navigation controller for managing navigation stack

    // Variables de sesión
    var currentUser by remember { mutableStateOf<String?>(null) }
    var isModerator by remember { mutableStateOf(false) }



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
                onLoginSuccess = { username, moderator ->
                    currentUser = username
                    isModerator = moderator
                    if (moderator) {
                        navController.navigate(RouteScreen.Moderator)
                    } else {
                        // Aquí puedes navegar a la pantalla principal del usuario (por crear)
                        navController.navigate(RouteScreen.WelcomeScreen)
                    }
                }
            )
        }

        composable<RouteScreen.Register>{
            Register() // Show Register screen
        }


        composable<RouteScreen.Moderator> {
            ModeratorScreen(
                moderatorName = currentUser ?: "Moderador",
                onLogout = {
                    currentUser = null
                    isModerator = false
                    navController.navigate(RouteScreen.WelcomeScreen)
                }
            )
        }


    }
}
