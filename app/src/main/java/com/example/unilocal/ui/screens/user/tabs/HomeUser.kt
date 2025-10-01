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
import com.example.unilocal.ui.components.users.RoleBasedTopBar
import com.example.unilocal.ui.screens.user.PlaceCard

/**
 * Main composable for the HomeUser screen.
 * Displays the user's profile, search bar, filters, sorting section, and a list of places.
 *
*/
@OptIn(ExperimentalMaterial3Api::class)
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
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Use the generic top bar for role-based navigation and actions
        RoleBasedTopBar(
            title = stringResource(R.string.profile_title),
            showLogoutDialog = true,
            onLogoutConfirmed = {},
            showAccountSettings = true,
            onAccountSettingsClick = {}
        )

        Spacer(modifier = Modifier.height(64.dp)) // Prevent overlap with top bar

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // User profile section with avatar and user info
            UserProfileSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Section title for user's places
            Text(
                text = stringResource(R.string.my_places),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Search bar for filtering places by name or category
            SearchBar(
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                onSearchClear = { searchQuery = "" },
                onSearchApply = {
                    // You can implement search logic here
                    println("Searching: $searchQuery with filter: $selectedFilter")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filter chips for place status
            FilterRow(
                filters = filters,
                selected = selectedFilter,
                onSelectedChange = { selectedFilter = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sorting section (can be expanded with more sorting options)
            SortSection()

            Spacer(modifier = Modifier.height(12.dp))

            // List of places (replace with dynamic list as needed)
            PlacesList()
        }
    }
}

/**
 * Displays the user's profile section with avatar, name, username, and location.
 */
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

/**
 * Search bar composable for filtering places.
 *
 * @param searchQuery Current search query string.
 * @param onSearchChange Callback for updating the search query.
 * @param onSearchClear Callback for clearing the search query.
 * @param onSearchApply Callback for applying the search.
 */
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

/**
 * Displays a row of filter chips for selecting place status.
 *
 * @param filters List of filter options.
 * @param selected Currently selected filter.
 * @param onSelectedChange Callback for changing the selected filter.
 */
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

/**
 * Displays the sorting section for places.
 * Can be expanded to include more sorting options.
 */
@Composable
fun SortSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.Left
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = stringResource(R.string.sort_content_desc),
            modifier = Modifier.size(50.dp),
            tint = Color.Black
        )
    }
}

/**
 * Displays a list of places. Replace with a dynamic list as needed.
 */
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

/**
 * Preview composable for the HomeUser screen.
 * Used to display a preview of HomeUser in the Android Studio design editor.
 */
@Preview(showBackground = true)
@Composable
fun HomeUserPreview() {
    HomeUser()
}
