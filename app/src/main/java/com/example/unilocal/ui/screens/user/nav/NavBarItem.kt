package com.example.unilocal.ui.screens.user.nav

import androidx.compose.ui.graphics.vector.ImageVector

interface NavBarItem {
    val route: String
    val icon: ImageVector
    val labelRes: Int
}
