package com.example.unilocal.ui.screens.moderator.nav

import com.example.unilocal.ui.screens.user.nav.NavBarItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.unilocal.R

enum class ModeratorNavItem(
    override val route: String,
    override val icon: ImageVector,
    override val labelRes: Int
) : NavBarItem {
    HOME("home", Icons.Default.Person, R.string.nav_profile),
    SETTINGS("settings", Icons.Default.Settings, R.string.settings_content_des)
}