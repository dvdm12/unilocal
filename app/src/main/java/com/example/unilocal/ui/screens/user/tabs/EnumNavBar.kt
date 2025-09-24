package com.example.unilocal.ui.screens.user.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.unilocal.R
import com.example.unilocal.ui.config.RouteTab

enum class Destination(
    val route: RouteTab,
    val icon: ImageVector,
    val label: Int
) {
    MY_PLACES(RouteTab.Places, Icons.Default.Place, R.string.nav_my_places),
    ADD_PLACE(RouteTab.AddPlace, Icons.Default.Add, R.string.nav_add_place),
    SEARCH(RouteTab.SearchPlace, Icons.Default.Search, R.string.nav_search),
    HOME_USER(RouteTab.HomeUser, Icons.Default.Person, R.string.nav_profile)
}

