package com.example.unilocal.ui.screens.user.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.unilocal.ui.screens.user.create_places.PlaceDetailsScreen
import com.example.unilocal.ui.screens.user.tabs.EditProfileScreen
import com.example.unilocal.ui.screens.user.tabs.HomeUser
import com.example.unilocal.ui.screens.user.tabs.SearchPlace

@Composable
fun UserNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = UserNavItem.HOME.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(UserNavItem.HOME.route) {
            HomeUser()
        }

        composable(UserNavItem.ADD.route) {
            PlaceDetailsScreen()
        }
        composable(UserNavItem.SEARCH.route) {
            SearchPlace()
        }

        composable(UserNavItem.SETTINGS.route) {
            EditProfileScreen()
        }

    }
}

@Composable
fun rememberCurrentRoute(navController: NavHostController): String? {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    return navBackStackEntry.value?.destination?.route
}
