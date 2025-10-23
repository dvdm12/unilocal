package com.example.unilocal.ui.screens.user.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.example.unilocal.ui.config.RoutePlace
import com.example.unilocal.ui.screens.user.place.create_places.PlaceDetailsScreen
import com.example.unilocal.ui.screens.user.place.details.ViewPlaceScreen
import com.example.unilocal.ui.screens.user.tabs.EditProfileScreen
import com.example.unilocal.ui.screens.user.tabs.HomeUser
import com.example.unilocal.ui.screens.user.tabs.SearchPlace
import com.example.unilocal.viewmodel.data.session.UserSessionViewModel
import com.example.unilocal.viewmodel.place.PlaceViewModel
import com.example.unilocal.viewmodel.schedule.ScheduleViewModel
import com.example.unilocal.viewmodel.user.UserViewModel
import com.example.unilocal.viewmodel.user.update.UserUpdateViewModel

/**
 * Gestiona la navegación entre las pestañas internas del usuario (Home, Buscar, Agregar, Perfil).
 *
 * @param modifier Modifier para el contenedor del NavHost.
 * @param navController Controlador de navegación local.
 * @param startDestination Ruta inicial al cargar la vista.
 * @param userViewModel ViewModel compartido que maneja los datos del usuario activo.
 * @param onLogout Callback de cierre de sesión (invocado desde cualquier pantalla).
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = UserNavItem.HOME.route,
    userViewModel: UserViewModel,
    userUpdateViewModel: UserUpdateViewModel,
    userSessionViewModel: UserSessionViewModel,
    scheduleViewModel: ScheduleViewModel,
    placeViewModel: PlaceViewModel,
    onLogout: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // --- Pantalla principal (inicio del usuario) ---
        composable(UserNavItem.HOME.route) {
            HomeUser(
                userViewModel = userViewModel,
                placeViewModel = placeViewModel,
                onView = { placeId ->
                    navController.navigate(RoutePlace.ViewPlaceScreen(placeId))
                },
                onBackClick = onLogout
            )
        }

        // --- Ver detalles de un lugar ---
        composable<RoutePlace.ViewPlaceScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<RoutePlace.ViewPlaceScreen>()

            ViewPlaceScreen(
                placeId = args.placeId,
                placeViewModel = placeViewModel,
                scheduleViewModel = scheduleViewModel,
                onBackClick = {
                    navController.popBackStack(UserNavItem.HOME.route, inclusive = false)
                }
            )
        }

        // --- Crear nuevo lugar ---
        composable(UserNavItem.ADD.route) {
            PlaceDetailsScreen(
                userSessionViewModel=userSessionViewModel,
                userViewModel = userViewModel,
                placeViewModel= placeViewModel,
                scheduleViewModel,
                onBackClick = onLogout
            )
        }

        // --- Buscar lugares ---
        composable(UserNavItem.SEARCH.route) {
            SearchPlace(
                //userViewModel = userViewModel,
                onClose = onLogout
            )
        }

        // --- Editar perfil ---
        composable(UserNavItem.SETTINGS.route) {
            EditProfileScreen(
                userSessionViewModel=userSessionViewModel,
                userUpdateViewModel=userUpdateViewModel,
                onBackClick = onLogout
            )
        }
    }
}

/**
 * Retorna la ruta activa actual dentro del NavHost.
 * Se usa para resaltar el ítem correspondiente en la barra de navegación inferior.
 *
 * @param navController Controlador de navegación.
 * @return Ruta actual como String, o null si no hay una activa.
 */
@Composable
fun rememberCurrentRoute(navController: NavHostController): String? {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    return navBackStackEntry.value?.destination?.route
}
