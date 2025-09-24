package com.example.unilocal.ui.screens.user.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.unilocal.ui.screens.user.tabs.HomeUser
import com.example.unilocal.ui.screens.user.tabs.AddPlace
import com.example.unilocal.ui.screens.user.tabs.MyPlaces
import com.example.unilocal.ui.screens.user.tabs.SearchPlace
import com.example.unilocal.ui.screens.user.tabs.UserNavItem

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
        composable(UserNavItem.MY_PLACES.route) {
            MyPlaces()
        }
        composable(UserNavItem.ADD.route) {
            AddPlace()
        }
        composable(UserNavItem.SEARCH.route) {
            SearchPlace()
        }
    }
}

@Composable
fun rememberCurrentRoute(navController: NavHostController): String? {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    return navBackStackEntry.value?.destination?.route
}
