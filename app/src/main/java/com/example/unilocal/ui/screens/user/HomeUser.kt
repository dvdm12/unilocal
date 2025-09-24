package com.example.unilocal.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back_content_desc))
            Text(text = stringResource(R.string.profile_title), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Icon(imageVector = Icons.Default.Build, contentDescription = stringResource(R.string.settings_content_desc))
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
                        .background(Color.LightGray)
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
                .height(50.dp)
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
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown, // Cambiar por ícono de filtro
                contentDescription = stringResource(R.string.sort_content_desc),
                modifier = Modifier.size(50.dp),
                tint= Color.Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Lugar listado
        PlaceCard(
            date = stringResource(R.string.play_card_message),
            name = stringResource(R.string.place_name),
            category = stringResource(R.string.place_category),
            rating = 4.5f,
            imageRes = R.drawable.ic_launcher_background
        )
    }

    // TODO: Puedes añadir aquí un BottomNavigation si lo necesitas globalmente
}



@Preview(showBackground = true)
@Composable
fun HomeUserPreview() {
    HomeUser()
}

