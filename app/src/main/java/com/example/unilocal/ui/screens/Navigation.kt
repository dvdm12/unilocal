package com.example.unilocal.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.unilocal.ui.config.RouteScreen


@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RouteScreen.WelcomeScreen
    ){
        composable<RouteScreen.WelcomeScreen> {
            WelcomeScreen(
                onLoginClick = {
                    navController.navigate(RouteScreen.Login)
                },
                onRegisterClick = {
                    navController.navigate(RouteScreen.Register)
                }
            )
        }

        composable<RouteScreen.Login>{
            Login()
        }

        composable<RouteScreen.Register>{
            Register()
        }
    }
}
