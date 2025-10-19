package com.example.unilocal.ui.screens.user.tabs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.example.unilocal.R
import com.example.unilocal.viewmodel.user.UserViewModel


/**
 * Dialog that confirms whether the user really wants to delete a place.
 * It directly interacts with [UserViewModel] to execute the removal.
 *
 * @param userViewModel The ViewModel responsible for managing user data.
 * @param placeId The ID of the place to be deleted.
 * @param placeName The name of the place to display in the confirmation text.
 * @param showDialog Mutable state controlling dialog visibility.
 */
@Composable
fun ConfirmDeleteDialog(
    userViewModel: UserViewModel,
    placeId: String,
    placeName: String,
    showDialog: MutableState<Boolean>
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        userViewModel.removePlace(placeId)
                        showDialog.value = false
                    }
                ) {
                    Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text(stringResource(R.string.logout_dialog_cancel))
                }
            },
            title = { Text(stringResource(R.string.dialog_delete_title)) },
            text = { Text(stringResource(R.string.dialog_delete_message)) }
        )
    }
}
