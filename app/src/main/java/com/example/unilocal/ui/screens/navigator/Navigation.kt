package com.example.unilocal.ui.screens.navigator

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.unilocal.model.Role
import com.example.unilocal.ui.config.RouteScreen
import com.example.unilocal.ui.screens.*
import com.example.unilocal.ui.screens.moderator.ModeratorScreen
import com.example.unilocal.ui.screens.user.NavHomeUser
import com.example.unilocal.viewmodel.data.UserRepository
import com.example.unilocal.viewmodel.data.session.UserSessionViewModel
import com.example.unilocal.viewmodel.data.session.UserSessionViewModelFactory
import com.example.unilocal.viewmodel.login.LoginViewModel
import com.example.unilocal.viewmodel.login.LoginViewModelFactory
import com.example.unilocal.viewmodel.place.PlaceViewModel
import com.example.unilocal.viewmodel.user.register.RegisterViewModel
import com.example.unilocal.viewmodel.user.register.RegisterViewModelFactory
import com.example.unilocal.viewmodel.schedule.ScheduleViewModel
import com.example.unilocal.viewmodel.schedule.ScheduleViewModelFactory
import com.example.unilocal.viewmodel.user.UserViewModel
import com.example.unilocal.viewmodel.user.update.UserUpdateViewModel

/**
 * Main navigation graph configuration for the UniLocal application.
 *
 * Manages authentication, registration, user, and moderator flows,
 * depending on the current user role.
 */
@SuppressLint("ViewModelConstructorInComposable")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val context = LocalContext.current.applicationContext

    // -------------------------------------------------------------------------
    // 🔹 GLOBAL SESSION VIEWMODEL
    // -------------------------------------------------------------------------
    val userSessionViewModel: UserSessionViewModel = viewModel(
        factory = UserSessionViewModelFactory(context)
    )

    // -------------------------------------------------------------------------
    // 🔹 MAIN APP VIEWMODELS
    // -------------------------------------------------------------------------
    val userViewModel: UserViewModel = viewModel()

    val scheduleViewModel: ScheduleViewModel = viewModel(
        factory = ScheduleViewModelFactory(context as Application)
    )

    val placeViewModel: PlaceViewModel = viewModel(
        factory = PlaceViewModel.Factory(context)
    )

    val userUpdateViewModel: UserUpdateViewModel = viewModel(
        factory = UserUpdateViewModel.Factory(context)
    )

    // -------------------------------------------------------------------------
    // 🔹 SESSION STATE
    // -------------------------------------------------------------------------
    val currentUser by userSessionViewModel.currentUser.collectAsState()
    val isSessionLoaded by userSessionViewModel.isSessionLoaded.collectAsState()

    // -------------------------------------------------------------------------
    // 🔹 SPLASH SCREEN (WHILE LOADING SESSION)
    // -------------------------------------------------------------------------
    if (!isSessionLoaded) {
        SplashScreen()
        return
    }

    // -------------------------------------------------------------------------
    // 🔹 LINK CURRENT USER TO USER VIEWMODEL
    // -------------------------------------------------------------------------
    LaunchedEffect(currentUser) {
        currentUser?.let { userViewModel.setActiveUser(it) }
    }

    // -------------------------------------------------------------------------
    // 🔹 DETERMINE INITIAL DESTINATION BASED ON USER ROLE
    // -------------------------------------------------------------------------
    val startDestination = when (currentUser?.role) {
        Role.USER -> RouteScreen.NavHomeUser
        Role.MODERATOR -> RouteScreen.ModeratorScreen
        else -> RouteScreen.WelcomeScreen
    }

    // -------------------------------------------------------------------------
    // 🔹 MAIN NAVIGATION GRAPH
    // -------------------------------------------------------------------------
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // --- Welcome Screen ---
        composable<RouteScreen.WelcomeScreen> {
            WelcomeScreen(
                onLoginClick = { navController.navigate(RouteScreen.Login) },
                onRegisterClick = { navController.navigate(RouteScreen.Register) }
            )
        }

        // --- Login Screen ---
        composable<RouteScreen.Login> {
            val context = LocalContext.current
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(
                    application = context.applicationContext as Application,
                    userRepository = UserRepository
                )
            )

            Login(
                viewModel = loginViewModel,
                userSessionViewModel = userSessionViewModel,
                onLoginSuccessUser = {
                    navController.navigate(RouteScreen.NavHomeUser) {
                        popUpTo(RouteScreen.Login) { inclusive = true }
                    }
                },
                onLoginSuccessModerator = {
                    navController.navigate(RouteScreen.ModeratorScreen) {
                        popUpTo(RouteScreen.Login) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(RouteScreen.Register) },
                onBackClick = { navController.navigate(RouteScreen.WelcomeScreen) },
                onForgotPassword = { navController.navigate(RouteScreen.ForgotPasswordScreen) }
            )
        }

        // --- Registration Screen ---
        composable<RouteScreen.Register> {
            val context = LocalContext.current
            val registerViewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModelFactory(
                    application = context.applicationContext as Application,
                    userRepository = UserRepository
                )
            )

            Register(
                viewModel = registerViewModel,
                onBackClick = { navController.navigate(RouteScreen.WelcomeScreen) },
                onRegisterSuccess = {
                    navController.navigate(RouteScreen.Login) {
                        popUpTo(RouteScreen.Register) { inclusive = true }
                    }
                }
            )
        }

        // --- Forgot Password Screen ---
        composable<RouteScreen.ForgotPasswordScreen> {
            val context = LocalContext.current
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(
                    application = context.applicationContext as Application,
                    userRepository = UserRepository
                )
            )

            ForgotPasswordScreen(
                viewModel = loginViewModel,
                onBackToLogin = { navController.navigate(RouteScreen.Login) }
            )
        }

        // --- User Navigation Graph ---
        composable<RouteScreen.NavHomeUser> {
            NavHomeUser(
                rootNavController = navController,
                userSessionViewModel = userSessionViewModel,
                userViewModel = userViewModel,
                userUpdateViewModel= userUpdateViewModel,
                scheduleViewModel = scheduleViewModel,
                placeViewModel = placeViewModel
            )
        }

        // --- Moderator Navigation Graph ---
        composable<RouteScreen.ModeratorScreen> {
            ModeratorScreen(
                userSessionViewModel = userSessionViewModel,
                userViewModel = userViewModel,
                onLogoutConfirmed = {
                    navController.navigate(RouteScreen.WelcomeScreen) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}

/**
 * Displays a loading indicator while the user session is being initialized.
 */
@Composable
fun SplashScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
