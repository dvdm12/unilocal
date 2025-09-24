package com.example.unilocal.ui.screens.user.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.unilocal.ui.config.RouteTab
import com.example.unilocal.ui.screens.user.*
import com.example.unilocal.ui.screens.user.tabs.AddPlace
import com.example.unilocal.ui.screens.user.tabs.MyPlaces
import com.example.unilocal.ui.screens.user.tabs.SearchPlace

@Composable
fun UserNavigation(
    navController: NavHostController,
    startDestination: RouteTab
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<RouteTab.HomeUser> {
            HomeUser()
        }
        composable<RouteTab.Places> {
            MyPlaces()
        }
        composable<RouteTab.AddPlace> {
            AddPlace()
        }
        composable<RouteTab.SearchPlace> {
            SearchPlace()
        }
    }
}


@Composable
fun rememberCurrentRoute(navController: NavHostController): String? {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    return navBackStackEntry.value?.destination?.route
}
