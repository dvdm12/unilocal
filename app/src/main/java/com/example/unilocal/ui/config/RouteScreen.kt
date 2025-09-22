package com.example.unilocal.ui.config

import kotlinx.serialization.Serializable

sealed class RouteScreen{
    @Serializable
    data object WelcomeScreen: RouteScreen()

    @Serializable
    data object Login: RouteScreen()

    @Serializable
    data object Register: RouteScreen()
}