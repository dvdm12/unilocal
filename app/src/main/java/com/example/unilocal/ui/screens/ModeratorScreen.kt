package com.example.unilocal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unilocal.ui.components.UniPrimaryButton

data class Place(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val createdBy: String,
    var status: String // "pending", "authorized", "rejected"
)

@Composable
fun ModeratorScreen() {
    var places by remember {
        mutableStateOf(
            listOf(
                Place(1, "Café Central", "Un café acogedor en el centro de la ciudad.", "Cafetería", "usuario1", "pending"),
                Place(2, "Museo de Arte Moderno", "Exhibiciones de arte contemporáneo.", "Museo", "usuario2", "authorized"),
                Place(3, "Hotel Sol", "Hotel con vista al mar y excelente servicio.", "Hotel", "usuario3", "pending")
            )
        )
    }

    val pendingPlaces = places.filter { it.status == "pending" }
    val authorizedPlaces = places.filter { it.status == "authorized" }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(text = "🧾 Lugares Pendientes", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        pendingPlaces.forEach { place ->
            PlaceModerationItem(
                place = place,
                onAuthorize = {
                    places = places.map {
                        if (it.id == place.id) it.copy(status = "authorized") else it
                    }
                },
                onReject = {
                    places = places.map {
                        if (it.id == place.id) it.copy(status = "rejected") else it
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "✅ Lugares Autorizados", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        authorizedPlaces.forEach { place ->
            PlaceAuthorizedItem(place = place)
        }
    }

}


@Composable
@Preview(showBackground = true)
fun ModeratorScreenPreview() {
    ModeratorScreen()
}
