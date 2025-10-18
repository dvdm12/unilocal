package com.example.unilocal.ui.screens.user.tabs

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unilocal.R
import com.example.unilocal.model.PlaceStatus
import com.example.unilocal.model.User
import com.example.unilocal.ui.components.users.SimpleTopBar
import com.example.unilocal.ui.screens.user.create_places.PlaceCard
import com.example.unilocal.viewmodel.user.UserViewModel

/**
 * Pantalla principal del usuario con perfil, filtros y lista de lugares creados.
 */
@Composable
fun HomeUser(
    userViewModel: UserViewModel,
    onBackClick: () -> Unit = {},
    onView: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    // ✅ Etiquetas traducibles dentro del contexto composable
    val allLabel = stringResource(R.string.filter_all)
    val publishedLabel = stringResource(R.string.filter_published)
    val pendingLabel = stringResource(R.string.filter_pending)
    val rejectedLabel = stringResource(R.string.filter_rejected)

    val filters = listOf(allLabel, publishedLabel, pendingLabel, rejectedLabel)

    var selectedFilter by remember { mutableStateOf(allLabel) }
    var searchQuery by remember { mutableStateOf("") }

    // --- Estado reactivo del usuario ---
    val user by userViewModel.user.collectAsState()
    val places = user?.places ?: emptyList()

    // --- Filtro de búsqueda y estado dinámico ---
    val filteredPlaces by remember(user, searchQuery, selectedFilter) {
        derivedStateOf {
            val normalizedQuery = searchQuery.trim().lowercase()

            places.filter { place ->
                val matchesStatus = when (selectedFilter) {
                    publishedLabel -> place.status == PlaceStatus.APPROVED
                    pendingLabel -> place.status == PlaceStatus.PENDING
                    rejectedLabel -> place.status == PlaceStatus.REJECTED
                    else -> true
                }

                // Mostrar todos si no hay texto
                if (normalizedQuery.isEmpty()) return@filter matchesStatus

                // Coincidencia directa o cercana por nombre
                val nameMatch = place.name.lowercase().contains(normalizedQuery)
                matchesStatus && nameMatch
            }
                // Ordenar coincidencias (las que empiezan igual primero)
                .sortedByDescending { it.name.lowercase().startsWith(normalizedQuery) }
                // Evitar duplicados
                .distinctBy { it.id }
        }
    }

    Scaffold(
        topBar = { HomeTopBar(onBackClick = onBackClick) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- Perfil ---
            item {
                Spacer(modifier = Modifier.height(8.dp))
                UserProfileSection(user = user)
            }

            // --- Título ---
            item {
                Text(
                    text = stringResource(R.string.my_places),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // --- Barra de búsqueda ---
            item {
                SearchBar(
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it },
                    onSearchClear = { searchQuery = "" }
                )
            }

            // --- Filtros ---
            item {
                FilterRow(
                    filters = filters,
                    selected = selectedFilter,
                    onSelectedChange = { selectedFilter = it }
                )
            }

            // --- Ordenamiento (placeholder) ---
            item { SortSection() }

            // --- Lista de lugares filtrados ---
            if (filteredPlaces.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.no_places_message),
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                }
            } else {
                items(filteredPlaces) { place ->
                    PlaceCard(
                        place = place,
                        onView = onView,
                        onEdit = onEdit,
                        onDelete = { /* TODO: Eliminar lugar */ }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

/**
 * Barra superior con el título del perfil y opción de logout.
 */
@Composable
fun HomeTopBar(onBackClick: () -> Unit) {
    SimpleTopBar(
        title = stringResource(R.string.profile_title),
        onLogoutClick = onBackClick
    )
}

/**
 * Muestra la información básica del usuario (foto, nombre, username, ciudad).
 */
@Composable
fun UserProfileSection(user: User?) {
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
            Text(
                text = user?.name ?: stringResource(R.string.default_user_name),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "@${user?.username ?: stringResource(R.string.default_user_username)}",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                text = "${user?.city ?: stringResource(R.string.default_user_city)}, ${user?.country ?: ""}",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * Barra de búsqueda reactiva.
 */
@Composable
fun SearchBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onSearchClear: () -> Unit
) {
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
            } else {
                Icon(Icons.Default.Search, contentDescription = null)
            }
        }
    )
}

/**
 * Fila horizontal de filtros.
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
 * Placeholder para futuras opciones de ordenamiento.
 */
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

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun HomeUserPreview() {
    val fakeViewModel = UserViewModel()
    HomeUser(userViewModel = fakeViewModel)
}
