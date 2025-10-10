package com.example.unilocal.ui.screens.moderator

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.unilocal.R
import com.example.unilocal.model.Place
import com.example.unilocal.model.PlaceStatus
import com.example.unilocal.ui.components.users.SimpleTopBar
import com.example.unilocal.viewmodel.data.UserSessionViewModel
import com.example.unilocal.viewmodel.user.UserViewModel

/**
 * Pantalla principal del moderador.
 * Lista todos los lugares pendientes y permite aprobar/rechazar.
 * Además, gestiona el cierre de sesión con confirmación.
 */
@Composable
fun ModeratorScreen(
    userSessionViewModel: UserSessionViewModel,
    userViewModel: UserViewModel,
    onLogoutConfirmed: () -> Unit = {}
) {
    // 🔹 Obtener todos los lugares pendientes desde todos los usuarios
    val pendingPlaces by remember {
        mutableStateOf(
            userViewModel.getAllUsers()
                .flatMap { it.places }
                .filter { it.status == PlaceStatus.PENDING }
        )
    }

    // 🔸 Estado del diálogo de confirmación de cierre de sesión
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = stringResource(R.string.moderator_label),
                onLogoutClick = { showLogoutDialog = true }
            )
        }
    ) { padding ->
        // 🔹 Contenido principal: lista de lugares pendientes
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.places_pending_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (pendingPlaces.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.no_pending_places),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            } else {
                items(pendingPlaces) { place ->
                    PlaceCardModeration(
                        place = place,
                        onApprove = { userViewModel.updatePlaceStatus(place, PlaceStatus.APPROVED) },
                        onReject = { userViewModel.updatePlaceStatus(place, PlaceStatus.REJECTED) },
                        onDetails = { /* TODO: Navegar a detalles */ }
                    )
                }
            }
        }
    }

    // 🔸 Diálogo de confirmación de cierre de sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "Cerrar sesión",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que quieres cerrar sesión como moderador?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        userSessionViewModel.clearSession() // ✅ Cierre de sesión real
                        onLogoutConfirmed() // ✅ Notifica al navegador principal
                    }
                ) {
                    Text("Sí, cerrar", color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }
}

/**
 * Tarjeta para mostrar información de un lugar pendiente.
 */
@Composable
fun PlaceCardModeration(
    place: Place,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 👤 Información del creador
            Text(
                text = "Creado por: @${place.owner.username}",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🏠 Detalles del lugar
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = place.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${place.category.name} | ⭐ ${place.avgRating}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }

                if (place.images.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(place.images.first()),
                        contentDescription = stringResource(R.string.place_image_desc),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFEFEFEF))
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🔘 Botones compactos de acción
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ElevatedButton(
                    onClick = onApprove,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color(0xFF49C96D)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.approve_button),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }

                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.reject_button),
                        fontSize = 12.sp
                    )
                }

                FilledTonalButton(
                    onClick = onDetails,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color(0xFFF3B340)
                    ),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.details_button),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
