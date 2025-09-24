package com.example.unilocal.ui.screens.user.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.unilocal.R
import com.example.unilocal.ui.screens.user.nav.NavBarItem

enum class UserNavItem(
    override val route: String,
    override val icon: ImageVector,
    override val labelRes: Int
) : NavBarItem {
    HOME("home", Icons.Default.Person, R.string.nav_profile),
    MY_PLACES("places", Icons.Default.Place, R.string.nav_my_places),
    ADD("add", Icons.Default.Add, R.string.nav_add_place),
    SEARCH("search", Icons.Default.Search, R.string.nav_search)
}



