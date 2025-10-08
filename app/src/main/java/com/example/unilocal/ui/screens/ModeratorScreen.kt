package com.example.unilocal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unilocal.R

object AppColors {
    val Background = Color(0xFFFFF9F7)
    val PrimaryOrange = Color(0xFFF58349)
    val LightGray = Color(0xFFF4ECE8)
    val TextPrimary = Color(0xFF333333)
    val TextSecondary = Color(0xFF757575)
    val TextPlaceholder = Color(0xFFBDBDBD)
}

@Composable
fun ModeratorScreen(
    moderatorName: String,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = { ModeratorTopAppBar() },
        bottomBar = { ModeratorBottomNavBar() },
        containerColor = AppColors.Background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item { SearchBar() }

            item { CategoryFilters() }

            item { SortOptions() }

            item {
                Text(
                    text = "Lugares",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = AppColors.TextPrimary
                )
            }

            val pendingPlaces = listOf(
                Place("Sabor de la Abuela", "Restaurante", 4.5f, 1.2f, "@isabellaRojas", "2h", R.drawable.ic_launcher_background),
                Place("Café del Parque", "Café", 4.2f, 0.8f, "@MateoVargas", "3h", R.drawable.ic_launcher_background)
            )
            items(pendingPlaces) { place ->
                PendingPlaceCard(item = place)
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeratorTopAppBar() {
    TopAppBar(
        title = {
            Text(
                "@Moderador",
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Logo",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp),
                tint = AppColors.TextPrimary
            )
        },
        actions = {
            IconButton(onClick = { /* Acción de búsqueda */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = AppColors.TextPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.Background)
    )
}

@Composable
fun SearchBar() {
    TextField(
        value = "",
        onValueChange = {},
        placeholder = { Text("Buscar por nombre o categoría", color = AppColors.TextPlaceholder) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AppColors.TextPlaceholder) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedContainerColor = AppColors.LightGray,
            focusedContainerColor = AppColors.LightGray
        ),
        singleLine = true
    )
}

@Composable
fun CategoryFilters() {
    val categories = listOf("Todos", "Restaurantes", "Cafés", "Comida Rápida")
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(categories) { category ->
            Chip(text = category, isSelected = category == "Todos")
        }
    }
}

@Composable
fun SortOptions() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Ordenar por",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = AppColors.TextPrimary
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Chip(text = "Más Reciente", isSelected = true)
            Chip(text = "Más Cercano", isSelected = false)
        }
    }
}

data class Place(
    val name: String,
    val category: String,
    val rating: Float,
    val distance: Float,
    val author: String,
    val time: String,
    val imageRes: Int
)

@Composable
fun PendingPlaceCard(item: Place) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Creado por ${item.author} · hace ${item.time}", fontSize = 12.sp, color = AppColors.TextSecondary)
                    Text(item.name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = AppColors.TextPrimary)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(item.category, fontSize = 14.sp, color = AppColors.TextSecondary)
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.Gray, modifier = Modifier
                            .size(16.dp)
                            .padding(start = 8.dp))
                        Text(" ${item.rating} · ${item.distance} km", fontSize = 14.sp, color = AppColors.TextSecondary)
                    }
                }
                // Imagen
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ActionButton(text = "Aprobar", icon = Icons.Default.CheckCircle, backgroundColor = AppColors.LightGray)
                    ActionButton(text = "Rechazar", backgroundColor = AppColors.LightGray)
                }
                Button(
                    onClick = { /* Acción de ver detalles */ },
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.PrimaryOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Detalles", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun Chip(text: String, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) AppColors.PrimaryOrange.copy(alpha = 0.2f) else AppColors.LightGray,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) AppColors.PrimaryOrange else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = if (isSelected) AppColors.PrimaryOrange else AppColors.TextSecondary, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector? = null,
    backgroundColor: Color
) {
    Button(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text, color = AppColors.TextPrimary)
            if (icon != null) {
                Spacer(Modifier.width(4.dp))
                Icon(icon, contentDescription = null, tint = Color.Gray)
            }
        }
    }
}

@Composable
fun ModeratorBottomNavBar() {
    NavigationBar(containerColor = AppColors.Background, contentColor = AppColors.TextSecondary) {
        val items = listOf(
            BottomNavItem("Pendiente", Icons.Default.Info, true),
            BottomNavItem("Aprobados", Icons.Default.Check, false),
            BottomNavItem("Rechazados", Icons.Default.Clear, false),
            BottomNavItem("Perfil", Icons.Default.AccountCircle, false),
            BottomNavItem("Cerrar Sesión", Icons.AutoMirrored.Filled.ExitToApp, false)
        )
        items.forEach { item ->
            NavigationBarItem(
                selected = item.isSelected,
                onClick = { /*TODO*/ },
                icon = { Icon(item.icon, contentDescription = item.label, modifier = Modifier.size(24.dp)) },
                label = { Text(item.label, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppColors.PrimaryOrange,
                    selectedTextColor = AppColors.PrimaryOrange,
                    unselectedIconColor = AppColors.TextSecondary,
                    unselectedTextColor = AppColors.TextSecondary,
                    indicatorColor = AppColors.Background
                )
            )
        }
    }
}

data class BottomNavItem(val label: String, val icon: ImageVector, val isSelected: Boolean)

@Preview(showBackground = true)
@Composable
fun ModeratorScreenPreview() {
    MaterialTheme {
        ModeratorScreen(
            moderatorName = "Jhovanny Ejemplo",
            onLogout = {}
        )
    }
}
