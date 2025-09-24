package com.example.unilocal.ui.components.users

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.unilocal.ui.screens.user.tabs.Destination

@Composable
fun BottomNavBar(
    selectedRoute: String,
    navController: NavController
) {
    NavigationBar {
        Destination.entries.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedRoute == item.route,
                onClick = {
                    if (selectedRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(id = item.label)
                    )
                },
                label = {
                    Text(text = stringResource(id = item.label))
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    val navController = rememberNavController()
    BottomNavBar(selectedRoute = Destination.MY_PLACES.route, navController = navController)
}
