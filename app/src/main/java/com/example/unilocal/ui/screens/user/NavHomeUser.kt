package com.example.unilocal.ui.screens.user // Package declaration for user screens

import androidx.compose.foundation.layout.padding // Import for padding modifier
import androidx.compose.material3.AlertDialog // Import for alert dialog
import androidx.compose.material3.Scaffold // Import for Scaffold layout
import androidx.compose.material3.Text // Import for text components
import androidx.compose.material3.TextButton // Import for dialog buttons
import androidx.compose.runtime.* // Import for composable state management
import androidx.compose.ui.Modifier // Import for Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController // Import for NavController
import androidx.navigation.compose.rememberNavController // Import for remembering NavController
import com.example.unilocal.ui.components.users.BottomNavBar // Import for bottom navigation bar component
import com.example.unilocal.ui.config.RouteScreen // Import for route screen configuration
import com.example.unilocal.ui.screens.user.nav.UserNavigation // Import for user navigation composable
import com.example.unilocal.ui.screens.user.nav.UserNavItem // Import for user navigation items
import com.example.unilocal.ui.screens.user.nav.rememberCurrentRoute // Import for remembering current route
import com.example.unilocal.R // Import for string resources
import com.example.unilocal.viewmodel.data.UserSessionViewModel

/**
 * Main composable that manages navigation for the authenticated user.
 * Receives the global NavController (rootNavController) to allow global actions such as logout.
 * @param rootNavController The global navigation controller for the app.
 */
@Composable // Marks the function as a composable
fun NavHomeUser(
    rootNavController: NavController,
    userSessionViewModel: UserSessionViewModel
) {
    // Main navigation composable for authenticated user
    val userNavController = rememberNavController() // Local NavController for user navigation
    val currentRoute = rememberCurrentRoute(userNavController) // Gets the current route for navigation

    // State variable for logout confirmation dialog
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold( // Scaffold layout for the screen
        bottomBar = { // Bottom navigation bar slot
            BottomNavBar( // Bottom navigation bar composable
                items = UserNavItem.entries.toList(), // List of navigation items
                selectedRoute = currentRoute ?: UserNavItem.HOME.route, // Currently selected route
                onNavigate = { route -> // Navigation callback for bottom bar
                    if (route != currentRoute) { // Only navigate if route is different
                        userNavController.navigate(route) { // Navigate to selected route
                            popUpTo(userNavController.graph.startDestinationId) { // Pop up to start destination
                                saveState = true // Save state for navigation
                            }
                            launchSingleTop = true // Launch as single top
                            restoreState = true // Restore previous state
                        }
                    }
                }
            )
        }
    ) { innerPadding -> // Content slot for Scaffold
        UserNavigation( // User navigation composable
            navController = userNavController, // Pass local NavController
            startDestination = UserNavItem.HOME.route, // Set start destination
            modifier = Modifier.padding(innerPadding), // Apply inner padding from Scaffold
            onLogout = { // Logout callback
                // Show logout confirmation dialog instead of immediate logout
                showLogoutDialog = true
                userSessionViewModel.clearSession()
            }
        )
    }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false }, // Close dialog when tapping outside or back button
            title = { Text(text = stringResource(R.string.logout_dialog_title)) }, // Dialog title
            text = { Text(text = stringResource(R.string.logout_dialog_message)) }, // Dialog description
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false // Close dialog
                        // Global logout action: navigates to Login and clears the stack
                        rootNavController.navigate(RouteScreen.Login) { // Navigate to Login screen
                            popUpTo(RouteScreen.NavHomeUser) { inclusive = true } // Clear navigation stack
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

