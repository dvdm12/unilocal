package com.example.unilocal.ui.screens.user

import androidx.compose.foundation.layout.padding // Import for padding modifier
import androidx.compose.material3.* // Import Material3 components
import androidx.compose.runtime.* // Import Compose runtime
import androidx.compose.ui.Modifier // Import Modifier for UI customization
import androidx.compose.ui.res.stringResource // Import for string resources
import androidx.navigation.NavController // Import NavController for navigation
import androidx.navigation.compose.rememberNavController // Import for remembering NavController
import com.example.unilocal.R // Import app resources
import com.example.unilocal.ui.components.users.BottomNavBar // Import custom bottom navigation bar
import com.example.unilocal.ui.config.RouteScreen // Import route screen configuration
import com.example.unilocal.ui.screens.user.nav.UserNavigation // Import user navigation graph
import com.example.unilocal.ui.screens.user.nav.UserNavItem // Import navigation items for user
import com.example.unilocal.ui.screens.user.nav.rememberCurrentRoute // Import helper to remember current route
import com.example.unilocal.viewmodel.data.UserSessionViewModel // Import user session ViewModel
import com.example.unilocal.viewmodel.user.UserViewModel // Import user data ViewModel

/**
 * Main screen for authenticated user navigation.
 *
 * This composable manages the navigation for a logged-in user, including:
 * - Local navigation within the user area
 * - Global navigation actions (e.g., logout)
 * - Displaying a bottom navigation bar
 * - Showing a logout confirmation dialog
 *
 * @param rootNavController Global NavController for the app
 * @param userSessionViewModel ViewModel for managing user session state
 * @param userViewModel ViewModel for managing user data (places, profile, etc.)
 */
@Composable
fun NavHomeUser(
    rootNavController: NavController, // Global navigation controller
    userSessionViewModel: UserSessionViewModel, // Session state ViewModel
    userViewModel: UserViewModel // User data ViewModel
) {
    // Local navigation controller for user area
    val userNavController = rememberNavController()
    val currentRoute = rememberCurrentRoute(userNavController)

    // State for showing logout confirmation dialog
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                items = UserNavItem.entries.toList(), // List of navigation items
                selectedRoute = currentRoute ?: UserNavItem.HOME.route, // Currently selected route
                onNavigate = { route ->
                    if (route != currentRoute) {
                        userNavController.navigate(route) {
                            popUpTo(userNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        // Main user navigation content
        UserNavigation(
            navController = userNavController,
            startDestination = UserNavItem.HOME.route,
            modifier = Modifier.padding(innerPadding),
            userViewModel = userViewModel, // Inject UserViewModel
            onLogout = { showLogoutDialog = true }
        )
    }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(text = stringResource(R.string.logout_dialog_title)) },
            text = { Text(text = stringResource(R.string.logout_dialog_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        userSessionViewModel.clearSession() // Clear session state
                        userViewModel.clearUser() // Clear user data
                        rootNavController.navigate(RouteScreen.Login) {
                            popUpTo(RouteScreen.NavHomeUser) { inclusive = true }
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.logout_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(text = stringResource(R.string.logout_dialog_cancel))
                }
            }
        )
    }
}
