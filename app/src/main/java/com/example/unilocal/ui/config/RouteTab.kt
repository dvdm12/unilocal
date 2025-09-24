package com.example.unilocal.ui.config

import kotlinx.serialization.Serializable

sealed class RouteTab(){
    @Serializable
    data object HomeUser: RouteTab()

    @Serializable
    data object Places: RouteTab()

    @Serializable
    data object SearchPlace: RouteTab()

    @Serializable
    data object AddPlace: RouteTab()
}