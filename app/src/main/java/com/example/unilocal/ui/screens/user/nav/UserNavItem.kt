package com.example.unilocal.ui.screens.user.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.unilocal.R

enum class UserNavItem(
    override val route: String,
    override val icon: ImageVector,
    override val labelRes: Int
) : NavBarItem {
    HOME("home", Icons.Default.Person, R.string.nav_profile),
    ADD("add", Icons.Default.Add, R.string.nav_add_place),
    SEARCH("search", Icons.Default.Search, R.string.nav_search),

    SETTINGS("settings", Icons.Default.Settings, R.string.settings_content_des)
}