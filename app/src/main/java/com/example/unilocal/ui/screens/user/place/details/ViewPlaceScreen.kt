package com.example.unilocal.ui.screens.user.place.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.unilocal.R
import com.example.unilocal.model.Place
import com.example.unilocal.viewmodel.place.PlaceViewModel
import com.example.unilocal.viewmodel.schedule.ScheduleViewModel
import com.example.unilocal.ui.components.users.SimpleTopBar

// Design tokens for consistent spacing and sizing
private object PlaceDetailTokens {
    val HorizontalPadding = 20.dp
    val VerticalPadding = 24.dp
    val SectionSpacing = 28.dp
    val CardRadius = 20.dp
    val SmallCardRadius = 14.dp
    val IconContainerSize = 44.dp
    val IconSize = 22.dp
    val CardElevation = 8.dp
    val ImageHeight = 220.dp
    val ImageWidth = 280.dp
}

/**
 * Displays detailed information of a specific [Place] with enhanced visual design.
 *
 * Sections:
 *  - Hero image carousel with gradient overlay
 *  - Basic info with category badge and rating
 *  - Contact information with icon cards
 *  - Schedule list with modern styling
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewPlaceScreen(
    placeId: String,
    placeViewModel: PlaceViewModel,
    scheduleViewModel: ScheduleViewModel,
    onBackClick: () -> Unit = {}
) {
    val places by placeViewModel.places.collectAsState()
    val place = places.find { it.id == placeId }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = place?.name ?: stringResource(R.string.place_new_title),
                onLogoutClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (place == null) {
            PlaceNotFoundState(modifier = Modifier.padding(padding))
        } else {
            PlaceDetailContent(
                place = place,
                scheduleViewModel = scheduleViewModel,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun PlaceNotFoundState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(R.string.msg_place_not_found),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun PlaceDetailContent(
    place: Place,
    scheduleViewModel: ScheduleViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PlaceImagesGallery(place)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = PlaceDetailTokens.HorizontalPadding,
                    vertical = PlaceDetailTokens.VerticalPadding
                ),
            verticalArrangement = Arrangement.spacedBy(PlaceDetailTokens.SectionSpacing)
        ) {
            PlaceBasicInfoSection(place)
            PlaceContactSection(place)
            PlaceScheduleSection(scheduleViewModel, place)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Enhanced image gallery with improved sizing and visual effects.
 */
@Composable
private fun PlaceImagesGallery(place: Place) {
    if (place.imageUrls.isNotEmpty()) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = PlaceDetailTokens.HorizontalPadding),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            items(place.imageUrls.size) { index ->
                PlaceImageCard(imageUrl = place.imageUrls[index].toString())
            }
        }
    } else {
        PlaceImagePlaceholder()
    }
}

@Composable
private fun PlaceImageCard(imageUrl: String) {
    Card(
        modifier = Modifier
            .width(PlaceDetailTokens.ImageWidth)
            .height(PlaceDetailTokens.ImageHeight),
        shape = RoundedCornerShape(PlaceDetailTokens.CardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = PlaceDetailTokens.CardElevation)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .error(R.drawable.place_holder_svgrepo_com)
                    .placeholder(R.drawable.place_holder_svgrepo_com)
                    .build(),
                contentDescription = stringResource(R.string.cd_place_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Subtle gradient overlay for better text readability if needed
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.25f)
                            ),
                            startY = 300f
                        )
                    )
            )
        }
    }
}

@Composable
private fun PlaceImagePlaceholder() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(PlaceDetailTokens.ImageHeight)
            .padding(horizontal = PlaceDetailTokens.HorizontalPadding, vertical = 20.dp),
        shape = RoundedCornerShape(PlaceDetailTokens.CardRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = stringResource(R.string.cd_place_image),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.msg_no_images),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Enhanced basic info section with better typography hierarchy.
 */
@Composable
private fun PlaceBasicInfoSection(place: Place) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Place name with improved typography
        Text(
            text = place.name,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                letterSpacing = (-0.5).sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        // Category badge with enhanced styling
        Surface(
            modifier = Modifier.wrapContentSize(),
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Face,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = place.category.name,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Description with improved readability
        Text(
            text = place.description,
            style = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 26.sp,
                letterSpacing = 0.15.sp
            ),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f)
        )
    }
}

/**
 * Enhanced contact section with modern card design.
 */
@Composable
private fun PlaceContactSection(place: Place) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(title = "Información de Contacto")

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(PlaceDetailTokens.CardRadius),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                ContactRow(
                    icon = Icons.Filled.Person,
                    label = place.owner.name,
                    contentDescription = stringResource(R.string.cd_owner_icon)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 2.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )

                ContactRow(
                    icon = Icons.Filled.LocationOn,
                    label = place.address,
                    contentDescription = stringResource(R.string.cd_location_icon)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 2.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )

                ContactRow(
                    icon = Icons.Filled.Phone,
                    label = place.phone,
                    contentDescription = stringResource(R.string.cd_phone_icon)
                )
            }
        }
    }
}

/**
 * Reusable section header component.
 */
@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold
        ),
        color = MaterialTheme.colorScheme.onBackground
    )
}

/**
 * Enhanced contact row with better icon containers.
 */
@Composable
private fun ContactRow(
    icon: ImageVector,
    label: String,
    contentDescription: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(PlaceDetailTokens.IconContainerSize),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(PlaceDetailTokens.IconSize)
                )
            }
        }

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Enhanced schedule section with improved visual hierarchy.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun PlaceScheduleSection(scheduleViewModel: ScheduleViewModel, place: Place) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(title = "Horarios")

        if (place.schedules.isEmpty()) {
            ScheduleEmptyState()
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                place.schedules.forEach { schedule ->
                    ScheduleItem(scheduleViewModel.formatSchedule(schedule))
                }
            }
        }
    }
}

@Composable
private fun ScheduleEmptyState() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(PlaceDetailTokens.SmallCardRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = stringResource(R.string.msg_schedule_required),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Enhanced schedule item with modern card design.
 */
@Composable
private fun ScheduleItem(scheduleText: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(PlaceDetailTokens.SmallCardRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                text = scheduleText,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}