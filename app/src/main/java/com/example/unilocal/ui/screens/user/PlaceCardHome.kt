package com.example.unilocal.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unilocal.R

@Composable
fun PlaceCard(
    date: String,
    name: String,
    category: String,
    rating: Float,
    imageRes: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp) // Reduced vertical padding
            .shadow(4.dp, MaterialTheme.shapes.medium), // Reduced shadow and shape size
        shape = MaterialTheme.shapes.medium, // Use a slightly smaller shape
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp) // Reduced elevation
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp), // Reduced internal padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.padding(end = 12.dp)) { // Reduced end padding
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp) // Reduced image size
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-4).dp, y = (-4).dp) // Slightly smaller offset
                        .size(20.dp) // Reduced badge size
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .border(1.dp, MaterialTheme.colorScheme.surface, CircleShape), // Thinner border
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert, // Use a proper star icon if available
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp) // Smaller icon
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium, // Slightly smaller typography
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelMedium, // Slightly smaller typography
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 1.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MoreVert, // Use a proper star icon if available
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(12.dp) // Smaller icon
                    )
                    Text(
                        text = " $rating",
                        style = MaterialTheme.typography.labelSmall, // Slightly smaller typography
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Spacer(modifier = Modifier.height(2.dp)) // Reduced spacing
                Text(
                    text = stringResource(R.string.created_on, date),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(6.dp)) // Reduced spacing
                val actions = listOf(
                    Triple(stringResource(R.string.view), ButtonType.Filled, MaterialTheme.colorScheme.primary),
                    Triple(stringResource(R.string.edit), ButtonType.Outlined, MaterialTheme.colorScheme.onSurface),
                    Triple(stringResource(R.string.delete), ButtonType.OutlinedError, MaterialTheme.colorScheme.error)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(actions) { (label, type, color) ->
                        when (type) {
                            ButtonType.Filled -> Button(
                                onClick = {},
                                shape = MaterialTheme.shapes.small,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text(label, fontSize = 12.sp, color = color)
                            }
                            ButtonType.Outlined -> OutlinedButton(
                                onClick = {},
                                shape = MaterialTheme.shapes.small,
                                border = ButtonDefaults.outlinedButtonBorder(enabled = true),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text(label, fontSize = 12.sp, color = color)
                            }
                            ButtonType.OutlinedError -> OutlinedButton(
                                onClick = {},
                                shape = MaterialTheme.shapes.small,
                                border = ButtonDefaults.outlinedButtonBorder(enabled = true),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = color),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text(label, fontSize = 12.sp, color = color)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Preview composable for the PlaceCard.
 * Used to display a preview of PlaceCard in the Android Studio design editor.
 */
@Composable
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
fun PlaceCardPreview() {
    PlaceCard(
        date = "14 de Marzo",
        name = "Café El Paraíso",
        category = "Cafetería",
        rating = 4.5f,
        imageRes = R.drawable.ic_launcher_background
    )
}

// Helper enum for button types
private enum class ButtonType { Filled, Outlined, OutlinedError }
