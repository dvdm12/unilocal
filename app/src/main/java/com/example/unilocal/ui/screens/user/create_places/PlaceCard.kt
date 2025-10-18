package com.example.unilocal.ui.screens.user.create_places

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.unilocal.R
import com.example.unilocal.model.*

/**
 * Tarjeta visual que muestra la información resumida de un lugar.
 *
 * Muestra la imagen almacenada en el objeto [Place].
 * Si no tiene imágenes, usa un placeholder local.
 */
@Composable
fun PlaceCard(
    place: Place,
    onView: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(2.dp, MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Imagen principal del lugar ---
            Box(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(72.dp)
            ) {
                val mainImage = place.images.firstOrNull()

                if (!mainImage.isNullOrBlank()) {
                    AsyncImage(
                        model = mainImage,
                        contentDescription = stringResource(R.string.place_image_desc),
                        modifier = Modifier
                            .size(72.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.place_holder_svgrepo_com),
                        contentDescription = stringResource(R.string.place_image_desc),
                        modifier = Modifier
                            .size(72.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)),
                        contentScale = ContentScale.Crop
                    )
                }

                // --- Indicador visual de estado ---
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-4).dp, y = (-4).dp)
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(
                            when (place.status) {
                                PlaceStatus.APPROVED -> Color(0xFF4CAF50) // Verde
                                PlaceStatus.PENDING -> Color(0xFFFFC107) // Amarillo
                                PlaceStatus.REJECTED -> Color(0xFFF44336) // Rojo
                            }
                        )
                        .border(1.dp, MaterialTheme.colorScheme.surface, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // --- Información textual del lugar ---
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = place.category.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 1.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = stringResource(R.string.place_rating_desc),
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = " ${"%.1f".format(place.avgRating)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = place.address,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                // --- Botones de acción ---
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Button(
                        onClick = onView,
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text(
                            stringResource(R.string.view),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    OutlinedButton(
                        onClick = onEdit,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text(
                            stringResource(R.string.edit),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    OutlinedButton(
                        onClick = onDelete,
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text(
                            stringResource(R.string.delete),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

/**
 * Vista previa con datos simulados.
 */
@Preview(showBackground = true)
@Composable
fun PlaceCardPreview() {
    val fakePlace = Place(
        id = "1",
        name = "Café El Paraíso",
        description = "Café artesanal con vista al valle.",
        category = PlaceCategory.CAFE,
        address = "Carrera 12 #5-67, Armenia",
        phone = "+57 310 555 1234",
        status = PlaceStatus.APPROVED,
        avgRating = 4.8,
        images = listOf("https://picsum.photos/seed/paraiso/400/200"), // ✅ ahora List<String>
        owner = User(
            id = "2",
            name = "Laura Admin",
            username = "lauraadmin",
            password = "123456",
            email = "laura@correo.com",
            country = "Colombia",
            city = "Armenia",
            isActive = true,
            role = Role.USER
        ),
        motive = ""
    )

    PlaceCard(place = fakePlace)
}
