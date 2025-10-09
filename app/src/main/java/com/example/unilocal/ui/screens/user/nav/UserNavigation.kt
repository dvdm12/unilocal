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

/**
 * Manages navigation between the user's internal tabs (Home, Search, Add, Settings).
 * @param modifier Modifier for the NavHost composable.
 * @param navController Navigation controller for managing navigation actions.
 * @param startDestination Initial route to display.
 * @param onLogout Callback for logout action.
 */
@Composable
fun UserNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = UserNavItem.HOME.route,
    onLogout: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(UserNavItem.HOME.route) {
            HomeUser(
                onBackClick = onLogout
            )
        }

        composable(UserNavItem.ADD.route) {
            PlaceDetailsScreen(
                onBackClick = onLogout
            )
        }

        composable(UserNavItem.SEARCH.route) {
            SearchPlace(
                onClose = onLogout
            )
        }

        composable(UserNavItem.SETTINGS.route) {
            EditProfileScreen(
                onBackClick = onLogout
            )
        }
    }
}

/**
 * Returns the current active route within the NavHost to highlight the active item
 * in the bottom navigation bar.
 * @param navController Navigation controller to observe the back stack.
 * @return The current route as a String, or null if not available.
 */
@Composable
fun rememberCurrentRoute(navController: NavHostController): String? {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    return navBackStackEntry.value?.destination?.route
}
