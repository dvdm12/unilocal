package com.example.unilocal.ui.config

import kotlinx.serialization.Serializable

sealed class RouteScreen{
    @Serializable
    data object WelcomeScreen: RouteScreen()

    @Serializable
    data object Login: RouteScreen()

    @Serializable
    data object Register: RouteScreen()

    @Serializable
    data object NavHomeUser: RouteScreen()

    @Serializable
    data object ForgotPasswordScreen: RouteScreen()

    @Serializable
    data object HomeUser: RouteScreen()

    @Serializable
    data object EditProfileScreen: RouteScreen()
}