package com.example.unilocal.ui.screens.user.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.unilocal.R

enum class Destination(
    val route: String,
    val icon: ImageVector,
    val label: Int
) {
    MY_PLACES("my_places", Icons.Default.Place, R.string.nav_my_places),
    ADD_PLACE("add_place", Icons.Default.Add, R.string.nav_add_place),
    SEARCH("search", Icons.Default.Search, R.string.nav_search),
    PROFILE("profile", Icons.Default.Person, R.string.nav_profile)
}

