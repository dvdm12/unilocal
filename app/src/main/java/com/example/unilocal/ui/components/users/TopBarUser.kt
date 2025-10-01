package com.example.unilocal.ui.components.users

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.unilocal.R
import com.example.unilocal.ui.theme.OrangePrimary

/**
 * Role-based reusable top bar for User, Moderator, Admin, etc.
 * Displays a centered title, a back/logout icon, and an optional account settings icon.
 * Shows a confirmation dialog when the back/logout icon is pressed.
 *
 * @param title Title displayed in the center of the top bar.
 * @param showLogoutDialog If true, shows logout confirmation dialog on back icon click.
 * @param onLogoutConfirmed Callback executed when user confirms logout.
 * @param showAccountSettings If true, shows the account settings icon.
 * @param onAccountSettingsClick Callback executed when user clicks the settings icon.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleBasedTopBar(
    title: String,
    showLogoutDialog: Boolean = true,
    onLogoutConfirmed: () -> Unit = {},
    showAccountSettings: Boolean = true,
    onAccountSettingsClick: () -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }

    // Show logout confirmation dialog if requested
    if (showDialog && showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = stringResource(R.string.logout_dialog_title)) },
            text = { Text(text = stringResource(R.string.logout_dialog_text)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        onLogoutConfirmed()
                    }
                ) {
                    Text(stringResource(R.string.logout_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.logout_dialog_cancel))
                }
            }
        )
    }

    // Top App Bar UI
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        navigationIcon = {
            if (showLogoutDialog) {
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.logout_content_desc),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        actions = {
            if (showAccountSettings) {
                IconButton(onClick = onAccountSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = stringResource(R.string.settings_content_desc),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = OrangePrimary
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * Preview composable for the RoleBasedTopBar.
 * Used to display a preview of the top bar in the Android Studio design editor.
 */
@Preview(showBackground = true)
@Composable
fun RoleBasedTopBarPreview() {
    RoleBasedTopBar(
        title = "User Home",
        showLogoutDialog = true,
        showAccountSettings = true
    )
}
