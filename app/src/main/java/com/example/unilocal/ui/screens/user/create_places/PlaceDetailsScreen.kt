package com.example.unilocal.ui.screens.user.create_places

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.unilocal.R
import com.example.unilocal.model.PlaceCategory
import com.example.unilocal.model.Schedule
import com.example.unilocal.model.buildPlace
import com.example.unilocal.ui.components.home.AuthTextField
import com.example.unilocal.ui.components.home.DropdownField
import com.example.unilocal.ui.components.home.UniPrimaryButton
import com.example.unilocal.ui.components.users.SimpleTopBar
import com.example.unilocal.viewmodel.user.UserViewModel
import java.net.URL
import java.time.LocalTime
import java.util.UUID

// --- MAIN SCREEN --------------------------------------------------------------
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailsScreen(
    userViewModel: UserViewModel,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val message by userViewModel.message.collectAsState()

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            userViewModel.clearMessage()
        }
    }

    // --- Shared states ---
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(PlaceCategory.RESTAURANT) }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("08:00") }
    var endTime by remember { mutableStateOf("18:00") }
    var schedules by remember { mutableStateOf(mutableListOf<Schedule>()) }
    val imageUrls = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = stringResource(R.string.place_details_title),
                onLogoutClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            PlaceBasicInfoSection(
                name = name,
                onNameChange = { name = it },
                description = description,
                onDescriptionChange = { description = it },
                category = category,
                onCategoryChange = { category = it },
                phone = phone,
                onPhoneChange = { phone = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            PlaceLocationSection(
                address = address,
                onAddressChange = { address = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            PlaceScheduleSection(
                startTime = startTime,
                onStartTimeChange = { startTime = it },
                endTime = endTime,
                onEndTimeChange = { endTime = it },
                schedules = schedules,
                onAddSchedule = { schedule ->
                    schedules.add(schedule)
                    userViewModel.addScheduleToPlace(UUID.randomUUID().toString(), schedule)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            PlaceImagesSection(imageUrls)

            Spacer(modifier = Modifier.height(24.dp))

            // --- Publish Button ---
            UniPrimaryButton(
                text = stringResource(R.string.place_details_publish),
                onClick = {
                    val currentUser = userViewModel.user.value ?: return@UniPrimaryButton

                    if (name.isBlank() || description.isBlank() || address.isBlank()) {
                        Toast.makeText(context, "Por favor complete todos los campos.", Toast.LENGTH_SHORT).show()
                        return@UniPrimaryButton
                    }

                    val newPlace = buildPlace {
                        this.name = name
                        this.description = description
                        this.category = category
                        this.address = address
                        this.phone = phone
                        this.owner = currentUser
                        this.schedules = schedules
                        setImageUrls(imageUrls.mapNotNull { runCatching { URL(it) }.getOrNull() })
                    }

                    userViewModel.addPlace(newPlace)
                    Toast.makeText(context, "Lugar creado exitosamente.", Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}

// --- MODULE: Basic Info -------------------------------------------------------
@Composable
fun PlaceBasicInfoSection(
    name: String,
    onNameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    category: PlaceCategory,
    onCategoryChange: (PlaceCategory) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit
) {
    Text(
        text = stringResource(R.string.place_details_basic_info),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
    Spacer(modifier = Modifier.height(12.dp))

    AuthTextField(
        value = name,
        onValueChange = onNameChange,
        label = stringResource(R.string.place_details_name),
        placeholder = stringResource(R.string.place_details_name_placeholder),
        leadingIcon = Icons.Filled.Home
    )
    Spacer(modifier = Modifier.height(12.dp))

    AuthTextField(
        value = description,
        onValueChange = onDescriptionChange,
        label = stringResource(R.string.place_details_description),
        placeholder = stringResource(R.string.place_details_description_placeholder),
        leadingIcon = Icons.Filled.Info
    )
    Spacer(modifier = Modifier.height(12.dp))

    DropdownField(
        label = stringResource(R.string.place_details_category),
        options = PlaceCategory.entries.map { it.name },
        selectedOption = category.name,
        onOptionSelected = { onCategoryChange(PlaceCategory.valueOf(it)) }
    )
    Spacer(modifier = Modifier.height(12.dp))

    AuthTextField(
        value = phone,
        onValueChange = onPhoneChange,
        label = stringResource(R.string.place_details_phone),
        placeholder = stringResource(R.string.place_details_phone_placeholder),
        leadingIcon = Icons.Filled.Phone
    )
}

// --- MODULE: Location ---------------------------------------------------------
@Composable
fun PlaceLocationSection(
    address: String,
    onAddressChange: (String) -> Unit
) {
    Text(
        text = stringResource(R.string.place_details_location_title),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
    Spacer(modifier = Modifier.height(12.dp))

    AuthTextField(
        value = address,
        onValueChange = onAddressChange,
        label = stringResource(R.string.place_details_address),
        placeholder = stringResource(R.string.place_details_address_placeholder),
        leadingIcon = Icons.Filled.LocationOn
    )
}

// --- MODULE: Schedule ---------------------------------------------------------
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlaceScheduleSection(
    startTime: String,
    onStartTimeChange: (String) -> Unit,
    endTime: String,
    onEndTimeChange: (String) -> Unit,
    schedules: MutableList<Schedule>,
    onAddSchedule: (Schedule) -> Unit
) {
    val context = LocalContext.current

    Text(
        text = stringResource(R.string.place_details_schedule_title),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
    Spacer(modifier = Modifier.height(12.dp))

    AuthTextField(
        value = startTime,
        onValueChange = onStartTimeChange,
        label = stringResource(R.string.place_details_open_time),
        placeholder = "HH:mm",
        leadingIcon = Icons.Filled.Info
    )
    Spacer(modifier = Modifier.height(8.dp))

    AuthTextField(
        value = endTime,
        onValueChange = onEndTimeChange,
        label = stringResource(R.string.place_details_close_time),
        placeholder = "HH:mm",
        leadingIcon = Icons.Filled.Info
    )
    Spacer(modifier = Modifier.height(8.dp))

    UniPrimaryButton(
        text = stringResource(R.string.place_details_add_schedule),
        onClick = {
            try {
                val start = LocalTime.parse(startTime)
                val end = LocalTime.parse(endTime)
                if (start >= end) {
                    Toast.makeText(context, "La hora de apertura no puede ser mayor o igual a la de cierre.",
                        Toast.LENGTH_SHORT).show()
                } else {
                    onAddSchedule(Schedule(1, 1, start, end))
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Formato de hora inválido (use HH:mm)", Toast.LENGTH_SHORT).show()
            }
        }
    )

    if (schedules.isNotEmpty()) {
        Spacer(modifier = Modifier.height(12.dp))
        schedules.forEach {
            Text("🕒 ${it.start} - ${it.end}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// --- MODULE: Images -----------------------------------------------------------
@Composable
fun PlaceImagesSection(imageUrls: MutableList<String>) {
    val context = LocalContext.current

    Text(
        text = stringResource(R.string.place_details_images_title),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
    Spacer(modifier = Modifier.height(12.dp))

    UniPrimaryButton(
        text = "Agregar imagen",
        onClick = {
            val newUrl = "https://picsum.photos/seed/${System.currentTimeMillis()}/900/500"
            imageUrls.add(newUrl)
        }
    )

    if (imageUrls.isNotEmpty()) {
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            itemsIndexed(imageUrls) { index, url ->
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(130.dp)
                ) {
                    Card(
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxSize(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(url)
                                .crossfade(true)
                                .error(R.drawable.place_holder_svgrepo_com)
                                .placeholder(R.drawable.place_holder_svgrepo_com)
                                .build(),
                            contentDescription = stringResource(R.string.place_image_desc),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    IconButton(
                        onClick = {
                            imageUrls.removeAt(index)
                            Toast.makeText(context, "Imagen eliminada.", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(28.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Eliminar imagen",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
