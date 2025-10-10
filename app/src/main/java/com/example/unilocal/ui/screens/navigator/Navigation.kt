package com.example.unilocal.ui.screens.navigator

import android.app.Application
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
import com.example.unilocal.viewmodel.data.UserSessionViewModel
import com.example.unilocal.viewmodel.data.UserSessionViewModelFactory
import com.example.unilocal.viewmodel.login.LoginViewModel
import com.example.unilocal.viewmodel.login.LoginViewModelFactory
import com.example.unilocal.viewmodel.register.RegisterViewModel
import com.example.unilocal.viewmodel.register.RegisterViewModelFactory
import com.example.unilocal.viewmodel.user.UserViewModel

/**
 * Configuración del grafo de navegación principal de la app UniLocal.
 * Gestiona los flujos de autenticación, registro, usuario y moderador.
 */
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val context = LocalContext.current.applicationContext

    // --- ViewModel global de sesión ---
    val userSessionViewModel: UserSessionViewModel = viewModel(
        factory = UserSessionViewModelFactory(context)
    )

    // --- ViewModel de datos de usuario (plazas, perfil, etc.) ---
    val userViewModel: UserViewModel = viewModel()

    val currentUser by userSessionViewModel.currentUser.collectAsState()
    val isSessionLoaded by userSessionViewModel.isSessionLoaded.collectAsState()

    // --- Mostrar pantalla de carga mientras se recupera la sesión ---
    if (!isSessionLoaded) {
        SplashScreen()
        return
    }

    // --- Vincular usuario actual al UserViewModel ---
    LaunchedEffect(currentUser) {
        currentUser?.let { userViewModel.setActiveUser(it) }
    }

    // --- Determinar destino inicial según el rol ---
    val startDestination = when (currentUser?.role) {
        Role.USER -> RouteScreen.NavHomeUser
        Role.MODERATOR -> RouteScreen.ModeratorScreen
        else -> RouteScreen.WelcomeScreen
    }

    // --- Grafo principal de navegación ---
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // --- Pantalla de bienvenida ---
        composable<RouteScreen.WelcomeScreen> {
            WelcomeScreen(
                onLoginClick = { navController.navigate(RouteScreen.Login) },
                onRegisterClick = { navController.navigate(RouteScreen.Register) }
            )
        }

        // --- Pantalla de login ---
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

        // --- Pantalla de registro ---
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
                    // Al completar el registro, regresar al login
                    navController.navigate(RouteScreen.Login) {
                        popUpTo(RouteScreen.Register) { inclusive = true }
                    }
                }
            )
        }

        // --- Pantalla de recuperación de contraseña ---
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

        // --- Navegación principal del usuario ---
        composable<RouteScreen.NavHomeUser> {
            NavHomeUser(
                rootNavController = navController,
                userSessionViewModel = userSessionViewModel,
                userViewModel = userViewModel // 🔥 Inyectamos UserViewModel
            )
        }

        // --- Navegación del moderador ---
        composable<RouteScreen.ModeratorScreen> {
            ModeratorScreen()
        }
    }
}

/**
 * Pantalla de carga mientras se inicializa la sesión del usuario.
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
