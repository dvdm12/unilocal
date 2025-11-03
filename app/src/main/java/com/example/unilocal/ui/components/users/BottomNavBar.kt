package com.example.unilocal.ui.components.users

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.unilocal.ui.screens.moderator.nav.ModeratorNavItem
import com.example.unilocal.ui.screens.user.nav.NavBarItem
import com.example.unilocal.ui.screens.user.nav.UserNavItem

@Composable
fun <T : NavBarItem> BottomNavBar(
    items: List<T>,
    selectedRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(id = item.labelRes)
                    )
                },
                label = {
                    Text(text = stringResource(id = item.labelRes), fontSize = 7.sp)
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    val items: List<UserNavItem> = UserNavItem.entries.toList()
    BottomNavBar(
        items = items,
        selectedRoute = UserNavItem.HOME.route,
        onNavigate = {}
    )
}

