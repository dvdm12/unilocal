package com.example.unilocal.ui.screens.user.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.unilocal.ui.components.users.SimpleTopBar
import com.example.unilocal.ui.screens.user.create_places.PlaceCard

@Composable
fun HomeUser(
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val filters = listOf(
        stringResource(R.string.filter_all),
        stringResource(R.string.filter_published),
        stringResource(R.string.filter_pending),
        stringResource(R.string.filter_rejected)
    )

    var selectedFilter by remember { mutableStateOf(filters[0]) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = { HomeTopBar(onBackClick = onBackClick) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
        ) {
            // User profile section with avatar and user info
            UserProfileSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Section title
            Text(
                text = stringResource(R.string.my_places),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Search bar
            SearchBar(
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                onSearchClear = { searchQuery = "" },
                onSearchApply = {
                    println("Searching: $searchQuery with filter: $selectedFilter")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filter chips
            FilterRow(
                filters = filters,
                selected = selectedFilter,
                onSelectedChange = { selectedFilter = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sorting section
            SortSection()

            Spacer(modifier = Modifier.height(12.dp))

            // List of places
            PlacesList()
        }
    }
}


@Composable
fun HomeTopBar(onBackClick: () -> Unit) {
    SimpleTopBar(
        title = stringResource(R.string.profile_title),
        onLogoutClick = onBackClick
    )
}

@Composable
fun UserProfileSection() {
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
}

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onSearchClear: () -> Unit,
    onSearchApply: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = {
                Text(stringResource(R.string.search_placeholder), style = TextStyle(fontSize = 15.sp))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = onSearchClear) {
                        Icon(Icons.Default.Close, contentDescription = "Clear search")
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onSearchApply) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Search")
            }
            OutlinedButton(onClick = onSearchClear) {
                Text("Clear")
            }
        }
    }
}

@Composable
fun FilterRow(
    filters: List<String>,
    selected: String,
    onSelectedChange: (String) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(filters) { filter ->
            FilterChip(
                selected = selected == filter,
                onClick = { onSelectedChange(filter) },
                label = { Text(filter) }
            )
        }
    }
}

@Composable
fun SortSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = stringResource(R.string.sort_content_desc),
            modifier = Modifier.size(50.dp),
            tint = Color.Black
        )
    }
}

@Composable
fun PlacesList() {
    PlaceCard(
        date = stringResource(R.string.play_card_message),
        name = stringResource(R.string.place_name),
        category = stringResource(R.string.place_category),
        rating = 4.5f,
        imageRes = R.drawable.ic_launcher_background
    )
}

@Preview(showBackground = true)
@Composable
fun HomeUserPreview() {
    HomeUser()
}
