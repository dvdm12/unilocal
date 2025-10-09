package com.example.unilocal.ui.screens.moderator

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unilocal.R

@Composable
fun ModeratorScreen() {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 🔝 TopBar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Star,
                contentDescription = stringResource(R.string.icon_star_desc)
            )
            Text(
                text = stringResource(R.string.moderator_label),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔍 Search bar
        SearchBarModeration(value = searchQuery) { searchQuery = it }

        Spacer(modifier = Modifier.height(12.dp))

        // 🏷️ Chips por categoría
        ChipRow(
            listOf(
                stringResource(R.string.chip_all),
                stringResource(R.string.chip_restaurants),
                stringResource(R.string.chip_cafes),
                stringResource(R.string.chip_fast_food)
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.order_by),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔃 Chips de orden
        ChipRow(
            listOf(
                stringResource(R.string.order_recent),
                stringResource(R.string.order_nearby),
                stringResource(R.string.order_top_rated)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.places_title),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 📍 Card de lugar hardcodeada
        PlaceCardModeration(
            author = "@IsabellaRojas",
            timeAgo = "hace 2h",
            name = "Sabor de la Abuela",
            category = "Restaurante",
            rating = 4.5,
            distance = "1.2 km",
            imageRes = R.drawable.ic_launcher_background
        )
    }
}

@Composable
fun SearchBarModeration(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(stringResource(R.string.search_placeholder))
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = stringResource(R.string.icon_search_desc)
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF6F2EE),
            unfocusedContainerColor = Color(0xFFF6F2EE),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    )
}

@Composable
fun ChipRow(chips: List<String>) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        chips.forEach { label ->
            AssistChip(
                onClick = { /* TODO */ },
                label = { Text(label) },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun PlaceCardModeration(
    author: String,
    timeAgo: String,
    name: String,
    category: String,
    rating: Double,
    distance: String,
    imageRes: Int
) {
    val createdByText = stringResource(R.string.created_by, author, timeAgo)
    val placeInfoText = stringResource(R.string.place_info_format, category, rating, distance)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = createdByText,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = placeInfoText,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = stringResource(R.string.place_image_desc),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEFEFEF))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ElevatedButton(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.elevatedButtonColors(containerColor = Color(0xFF49C96D)),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(stringResource(R.string.approve_button), color = Color.White)
                }

                OutlinedButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(stringResource(R.string.reject_button), fontSize = 12.sp)
                }

                FilledTonalButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color(0xFFF3B340)
                    )
                ) {
                    Text(stringResource(R.string.details_button), color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewModeratorScreen() {
    ModeratorScreen()
}
