package com.example.unilocal.ui.config

import kotlinx.serialization.Serializable

/**
 * Defines internal routes related to place management within the user section.
 *
 * These destinations are not exposed in the bottom navigation bar
 * and are only accessible through contextual actions (e.g., from HomeUser).
 */
sealed class RoutePlace {

    /** Displays details of a specific place selected by the user. */
    @Serializable
    data object ViewPlaceScreen : RoutePlace()

    /** Opens the edit screen for a specific place. */
    @Serializable
    data object EditPlaceScreen : RoutePlace()
}
