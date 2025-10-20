package com.example.unilocal.ui.screens.user.place.delete

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.example.unilocal.R
import com.example.unilocal.viewmodel.place.PlaceViewModel
import com.example.unilocal.viewmodel.user.UserViewModel

/**
 * Diálogo de confirmación para eliminar un lugar.
 * Se comunica directamente con [PlaceViewModel] y utiliza recursos del strings.xml.
 *
 * @param placeViewModel ViewModel responsable de gestionar los lugares.
 * @param placeId ID del lugar a eliminar.
 * @param placeName Nombre del lugar, mostrado en el texto de confirmación.
 * @param showDialog Estado mutable que controla la visibilidad del diálogo.
 */
@Composable
fun ConfirmDeleteDialog(
    placeViewModel: PlaceViewModel,
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

                        placeViewModel.removePlace(placeId)

                        userViewModel.removePlaceFromUserList(placeId)

                        showDialog.value = false
                    }
                ) {
                    Text(
                        text = stringResource(R.string.action_delete),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },

            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },

            title = { Text(stringResource(R.string.dialog_delete_title)) },

            text = {
                Text(
                    stringResource(
                        R.string.dialog_delete_message,
                        placeName
                    )
                )
            }
        )
    }
}
