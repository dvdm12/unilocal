package com.example.unilocal.ui.config

import com.example.unilocal.model.Place
import kotlinx.serialization.Serializable

/**
 * Defines internal routes related to place management within the user section.
 *
 * These destinations are not exposed in the bottom navigation bar
 * and are only accessible through contextual actions (e.g., from HomeUser).
 */
sealed class RoutePlace {

    /**
     * Displays the details of a specific place selected by the user.
     * Example: navigate(RoutePlace.ViewPlaceScreen(placeId))
     */
    @Serializable
    data class ViewPlaceScreen(val placeId: String) : RoutePlace()

    /**
     * Opens the edit screen for a specific place.
     * Example: navigate(RoutePlace.EditPlaceScreen(placeId))
     */
    @Serializable
    data class EditPlaceScreen(val placeId: String) : RoutePlace()
}
