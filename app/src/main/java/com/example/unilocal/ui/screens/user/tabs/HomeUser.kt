package com.example.unilocal.ui.screens.user.tabs

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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.unilocal.R
import com.example.unilocal.ui.screens.user.PlaceCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeUserTopBar(
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.profile_title),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(R.string.back_content_desc),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = stringResource(R.string.settings_content_desc),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.fillMaxWidth(),
        windowInsets = WindowInsets(0)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeUser(
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
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
            .background(MaterialTheme.colorScheme.background)
    ) {
        HomeUserTopBar(
            onBackClick = onBackClick,
            onSettingsClick = onSettingsClick
        )
        Spacer(modifier = Modifier.height(64.dp)) // Prevent overlap, matches TopAppBar height
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {

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
                placeholder = { Text(stringResource(R.string.search_placeholder), style = TextStyle(fontSize = 15.sp)) },
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
    }
}

@Preview(showBackground = true)
@Composable
fun HomeUserPreview() {
    HomeUser()
}
