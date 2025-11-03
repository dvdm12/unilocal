package com.example.unilocal.ui.screens.moderator.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.unilocal.R
import com.example.unilocal.ui.components.users.BottomNavBar
import com.example.unilocal.ui.config.RouteScreen
import com.example.unilocal.ui.screens.user.nav.rememberCurrentRoute
import com.example.unilocal.viewmodel.data.session.UserSessionViewModel
import com.example.unilocal.viewmodel.user.UserViewModel

/**
 * Main screen for moderator navigation.
 *
 * This composable manages:
 * - Local navigation within the moderator section
 * - Global navigation actions (e.g., logout)
 * - Displaying the bottom navigation bar
 * - Showing a logout confirmation dialog
 *
 * @param rootNavController Global NavController for the app
 * @param userSessionViewModel ViewModel that manages user session state
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavHomeModerator(
    rootNavController: NavController,
    userSessionViewModel: UserSessionViewModel,
    userViewModel: UserViewModel
) {
    // Local navigation controller for the moderator section
    val moderatorNavController = rememberNavController()
    val currentRoute = rememberCurrentRoute(moderatorNavController)

    // State for logout confirmation dialog
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                items = ModeratorNavItem.entries.toList(),
                selectedRoute = currentRoute ?: ModeratorNavItem.HOME.route,
                onNavigate = { route ->
                    if (route != currentRoute) {
                        moderatorNavController.navigate(route) {
                            popUpTo(moderatorNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        ModeratorNavigation(
            navController = moderatorNavController,
            startDestination = ModeratorNavItem.HOME.route,
            modifier = Modifier.padding(innerPadding),
            userSessionViewModel = userSessionViewModel,
            userViewModel = userViewModel,
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
                        userSessionViewModel.clearSession()
                        rootNavController.navigate(RouteScreen.Login) {
                            popUpTo(RouteScreen.NavHomeModerator) { inclusive = true }
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
