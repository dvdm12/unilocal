package com.example.unilocal.ui.screens.moderator.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.unilocal.ui.screens.moderator.ModeratorScreen
import com.example.unilocal.viewmodel.data.session.UserSessionViewModel
import com.example.unilocal.viewmodel.user.UserViewModel

/**
 * Handles internal navigation for the moderator section (Home, Settings).
 *
 * @param modifier Modifier used to adjust the layout inside the Scaffold.
 * @param navController Local navigation controller for moderator screens.
 * @param startDestination Initial route when the view is loaded.
 * @param userSessionViewModel ViewModel used to manage the user session state.
 * @param onLogout Callback executed when the moderator chooses to log out.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ModeratorNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = ModeratorNavItem.HOME.route,
    userSessionViewModel: UserSessionViewModel,
    userViewModel: UserViewModel,
    onLogout: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // --- Main screen (moderator home) ---
        composable(ModeratorNavItem.HOME.route) {
            ModeratorScreen(
                userSessionViewModel = userSessionViewModel,
                userViewModel = userViewModel,
                onLogout = onLogout
            )
        }

        // --- Settings / Profile screen ---
        composable(ModeratorNavItem.SETTINGS.route) {
            // TODO: Add SettingsModeratorScreen() here
        }
    }
}
