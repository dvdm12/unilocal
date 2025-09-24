package com.example.unilocal.ui.screens.user

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.unilocal.ui.components.users.BottomNavBar
import com.example.unilocal.ui.screens.user.nav.UserNavigation
import com.example.unilocal.ui.screens.user.nav.rememberCurrentRoute
import com.example.unilocal.ui.screens.user.tabs.UserNavItem

@Composable
fun NavHomeUser() {
    val navController = rememberNavController()
    val currentRoute = rememberCurrentRoute(navController)

    Scaffold(
        bottomBar = {
            BottomNavBar(
                items = UserNavItem.entries.toList(),
                selectedRoute = currentRoute ?: UserNavItem.HOME.route,
                onNavigate = { route ->
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        UserNavigation(
            navController = navController,
            startDestination = UserNavItem.HOME.route,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserScreenPreview() {
    NavHomeUser()
}

