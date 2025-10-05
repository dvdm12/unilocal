package com.example.unilocal.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unilocal.R
import com.example.unilocal.ui.components.UniPrimaryButton
import kotlinx.coroutines.launch

data class Place(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val createdBy: String,
    var status: String // "pending", "authorized", "rejected"
)

@Composable
fun ModeratorScreen(
    moderatorName: String,
    onLogout: () -> Unit
) {
    var places by remember {
        mutableStateOf(
            listOf(
                Place(1, "Café Central", "Un café acogedor en el centro de la ciudad.", "Cafetería", "usuario1", "pending"),
                Place(2, "Museo de Arte Moderno", "Exhibiciones de arte contemporáneo.", "Museo", "usuario2", "authorized"),
                Place(3, "Hotel Sol", "Hotel con vista al mar y excelente servicio.", "Hotel", "usuario3", "pending"),
                Place(4, "Bar Nocturno", "Ambiente nocturno con música en vivo.", "Bar", "usuario4", "rejected")
            )
        )
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val pendingPlaces = places.filter { it.status == "pending" }
    val authorizedPlaces = places.filter { it.status == "authorized" }
    val rejectedPlaces = places.filter { it.status == "rejected" }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = moderatorName, style = MaterialTheme.typography.titleLarge)
                    }
                    TextButton(onClick = onLogout) {
                        Text(text = "Cerrar sesión")
                    }
                }
            }

            item {
                Text(text = "🧾 Lugares Pendientes", style = MaterialTheme.typography.titleLarge)
            }

            items(pendingPlaces, key = { it.id }) { place ->
                var visible by remember { mutableStateOf(true) }
                var showDialog by remember { mutableStateOf("") }

                AnimatedVisibility(visible = visible) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = place.name, style = MaterialTheme.typography.titleLarge)
                            Text(text = place.category, style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Creado por: ${place.createdBy}", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = place.description, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(12.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                UniPrimaryButton(text = "Autorizar", onClick = {
                                    showDialog = "authorize"
                                })
                                UniPrimaryButton(text = "Rechazar", onClick = {
                                    showDialog = "reject"
                                })
                            }
                        }
                    }
                }

                if (showDialog.isNotEmpty()) {
                    AlertDialog(
                        onDismissRequest = { showDialog = "" },
                        title = { Text("Confirmar acción") },
                        text = {
                            Text("¿Estás seguro de que deseas ${if (showDialog == "authorize") "autorizar" else "rechazar"} el lugar \"${place.name}\"?")
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                places = places.map {
                                    if (it.id == place.id) it.copy(status = if (showDialog == "authorize") "authorized" else "rejected") else it
                                }
                                scope.launch {
                                    snackbarHostState.showSnackbar("Lugar ${if (showDialog == "authorize") "autorizado" else "rechazado"}: ${place.name}")
                                }
                                visible = false
                                showDialog = ""
                            }) {
                                Text("Sí")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = "" }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
            }

            item {
                Text(text = "✅ Lugares Autorizados", style = MaterialTheme.typography.titleLarge)
            }

            items(authorizedPlaces) { place ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = place.name, style = MaterialTheme.typography.titleLarge)
                        Text(text = place.category, style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Creado por: ${place.createdBy}", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = place.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            item {
                Text(text = "❌ Lugares Rechazados", style = MaterialTheme.typography.titleLarge)
            }

            items(rejectedPlaces) { place ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = place.name, style = MaterialTheme.typography.titleLarge)
                        Text(text = place.category, style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Creado por: ${place.createdBy}", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = place.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ModeratorScreenPreview() {
    ModeratorScreen(
        moderatorName = "Jhovanny Ejemplo",
        onLogout = {}
    )
}

