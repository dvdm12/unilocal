package com.example.unilocal.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.unilocal.R

@Composable
fun HomeUser() {
    val scrollState = rememberScrollState()
    val filters = listOf(
        stringResource(R.string.filter_all),
        stringResource(R.string.filter_published),
        stringResource(R.string.filter_pending),
        stringResource(R.string.filter_rejected)
    )
    var selectedFilter by remember { mutableStateOf(filters[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = stringResource(R.string.back_content_desc))
            Text(text = stringResource(R.string.profile_title), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = stringResource(R.string.settings_content_desc))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Profile
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = stringResource(R.string.user_avatar_desc),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(stringResource(R.string.user_full_name), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(stringResource(R.string.user_username), color = Color.Gray, fontSize = 14.sp)
                Text(stringResource(R.string.user_location), color = Color.Gray, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section Title
        Text(stringResource(R.string.my_places), fontWeight = FontWeight.Bold, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        // Search bar (simulada)
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text(stringResource(R.string.search_placeholder)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filtros
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filters) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ordenar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Cambiar por ícono de filtro
                contentDescription = stringResource(R.string.sort_content_desc)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Lugar listado
        PlaceCard(
            date = "15 de marzo de 2024",
            name = stringResource(R.string.place_name),
            category = stringResource(R.string.place_category),
            rating = 4.5f,
            imageRes = R.drawable.ic_launcher_foreground
        )
    }

    // TODO: Puedes añadir aquí un BottomNavigation si lo necesitas globalmente
}

@Composable
fun PlaceCard(
    date: String,
    name: String,
    category: String,
    rating: Float,
    imageRes: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(text = stringResource(R.string.created_on, date), fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold)
                Text("$category  •  ⭐ $rating", fontSize = 13.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = {}) {
                Text(stringResource(R.string.view))
            }
            OutlinedButton(onClick = {}) {
                Text(stringResource(R.string.edit))
            }
            OutlinedButton(onClick = {}) {
                Text(stringResource(R.string.delete))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeUserPreview() {
    HomeUser()
}
