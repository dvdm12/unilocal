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
import com.example.unilocal.ui.components.home.AuthTextField
import com.example.unilocal.ui.components.home.DropdownField
import com.example.unilocal.ui.components.home.UniPrimaryButton
import com.example.unilocal.ui.components.users.SimpleTopBar
import com.example.unilocal.viewmodel.place.PlaceViewModel
import com.example.unilocal.viewmodel.schedule.ScheduleViewModel
import com.example.unilocal.viewmodel.user.UserViewModel

/**
 * Composable screen for creating a new **Place** within the UniLocal application.
 *
 * This screen allows users to:
 *  - Enter general place information (name, description, category, phone, address)
 *  - Configure business schedules (using AM/PM format)
 *  - Upload and remove preview images
 *  - Publish the place after passing all validation checks
 *
 * The function observes reactive state from [PlaceViewModel] and [ScheduleViewModel],
 * ensuring UI updates are immediately reflected when ViewModel data changes.
 *
 * @param userViewModel Handles user-specific actions such as adding a new place.
 * @param placeViewModel Manages the current place creation form fields and validation logic.
 * @param scheduleViewModel Handles schedule creation, validation, and formatting.
 * @param onBackClick Lambda triggered when the user presses the back button in the top bar.
 */
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailsScreen(
    userViewModel: UserViewModel,
    placeViewModel: PlaceViewModel,
    scheduleViewModel: ScheduleViewModel,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val name by placeViewModel.name.collectAsState()
    val description by placeViewModel.description.collectAsState()
    val category by placeViewModel.category.collectAsState()
    val phone by placeViewModel.phone.collectAsState()
    val address by placeViewModel.address.collectAsState()
    val imageUrls by placeViewModel.imageUrls.collectAsState()
    val message by placeViewModel.message.collectAsState()
    val user = userViewModel.user.collectAsState().value

    // Displays short Toast messages when validation or success messages are emitted from ViewModel.
    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            placeViewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = stringResource(R.string.place_new_title),
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
            // --- Basic Information Section ---
            PlaceBasicInfoSection(
                name = name,
                onNameChange = placeViewModel::updateName,
                description = description,
                onDescriptionChange = placeViewModel::updateDescription,
                category = category,
                onCategoryChange = placeViewModel::updateCategory,
                phone = phone,
                onPhoneChange = placeViewModel::updatePhone
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Location Section ---
            PlaceLocationSection(
                address = address,
                onAddressChange = placeViewModel::updateAddress
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Schedule Section ---
            PlaceScheduleSection(
                scheduleViewModel = scheduleViewModel,
                onScheduleAdded = { schedule ->
                    placeViewModel.addSchedule(schedule)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Images Section ---
            PlaceImagesSection(
                imageUrls = imageUrls,
                onAddImage = { placeViewModel.addImage(it) },
                onRemoveImage = { index -> placeViewModel.removeImage(index) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Publish Button ---
            UniPrimaryButton(
                text = stringResource(R.string.action_publish),
                onClick = {
                    val owner = user ?: return@UniPrimaryButton
                    val place = placeViewModel.createPlace(owner)

                    if (place != null) {
                        //Add place to user's list
                        userViewModel.addPlace(place)

                        //Show confirmation
                        Toast.makeText(
                            context,
                            context.getString(R.string.msg_place_created),
                            Toast.LENGTH_LONG
                        ).show()

                        // ✅ Reset all form fields and schedules
                        placeViewModel.resetPlaceForm()
                    }
                }
            )
        }
    }
}

/**
 * Displays a form for entering the basic information of the place:
 * name, description, category, and phone number.
 *
 * @param name Current value of the place's name.
 * @param onNameChange Lambda to update the name field in [PlaceViewModel].
 * @param description Current description text.
 * @param onDescriptionChange Lambda to update the description field.
 * @param category Current [PlaceCategory] selection.
 * @param onCategoryChange Lambda to update the selected category.
 * @param phone Current phone number string.
 * @param onPhoneChange Lambda to update the phone field.
 */
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
        text = stringResource(R.string.place_basic_info_title),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
    Spacer(modifier = Modifier.height(12.dp))
    AuthTextField(
        value = name,
        onValueChange = onNameChange,
        label = stringResource(R.string.place_field_name),
        placeholder = stringResource(R.string.place_field_name_placeholder),
        leadingIcon = Icons.Filled.Home
    )
    Spacer(modifier = Modifier.height(12.dp))
    AuthTextField(
        value = description,
        onValueChange = onDescriptionChange,
        label = stringResource(R.string.place_field_description),
        placeholder = stringResource(R.string.place_field_description_placeholder),
        leadingIcon = Icons.Filled.Info
    )
    Spacer(modifier = Modifier.height(12.dp))
    DropdownField(
        label = stringResource(R.string.place_field_category),
        options = PlaceCategory.entries.map { it.name },
        selectedOption = category.name,
        onOptionSelected = { onCategoryChange(PlaceCategory.valueOf(it)) }
    )
    Spacer(modifier = Modifier.height(12.dp))
    AuthTextField(
        value = phone,
        onValueChange = onPhoneChange,
        label = stringResource(R.string.place_field_phone),
        placeholder = stringResource(R.string.place_field_phone_placeholder),
        leadingIcon = Icons.Filled.Phone
    )
}

/**
 * Displays the address input field for the place location.
 *
 * @param address Current value of the address field.
 * @param onAddressChange Lambda to update the address value in [PlaceViewModel].
 */
@Composable
fun PlaceLocationSection(address: String, onAddressChange: (String) -> Unit) {
    Text(
        text = stringResource(R.string.place_location_title),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
    Spacer(modifier = Modifier.height(12.dp))
    AuthTextField(
        value = address,
        onValueChange = onAddressChange,
        label = stringResource(R.string.place_field_address),
        placeholder = stringResource(R.string.place_field_address_placeholder),
        leadingIcon = Icons.Filled.LocationOn
    )
}

/**
 * Manages and displays the section for adding schedules to the place.
 *
 * Observes the list of schedules from [ScheduleViewModel] and
 * renders a [ScheduleForm] for adding new ones.
 *
 * @param scheduleViewModel ViewModel responsible for schedule management.
 * @param onScheduleAdded Callback invoked when a new schedule is added.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlaceScheduleSection(
    scheduleViewModel: ScheduleViewModel,
    onScheduleAdded: (Schedule) -> Unit
) {
    val context = LocalContext.current
    val schedules by scheduleViewModel.schedules.collectAsState()
    val message by scheduleViewModel.message.collectAsState()

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            scheduleViewModel.clearMessage()
        }
    }

    ScheduleForm(
        scheduleViewModel = scheduleViewModel,
        onAddSchedule = onScheduleAdded
    )

    if (schedules.isNotEmpty()) {
        Spacer(modifier = Modifier.height(12.dp))
        schedules.forEach {
            Text(
                text = scheduleViewModel.formatSchedule(it),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * Displays the schedule creation form allowing users to specify:
 * - Day range (start and end day)
 * - Opening and closing hours (AM/PM)
 *
 * When submitted, the schedule is validated and added via [ScheduleViewModel.addSchedule].
 *
 * @param scheduleViewModel Manages validation and creation of schedules.
 * @param onAddSchedule Callback triggered after a schedule is successfully created.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleForm(
    scheduleViewModel: ScheduleViewModel,
    onAddSchedule: (Schedule) -> Unit
) {
    val context = LocalContext.current

    val days = listOf(
        stringResource(R.string.day_monday),
        stringResource(R.string.day_tuesday),
        stringResource(R.string.day_wednesday),
        stringResource(R.string.day_thursday),
        stringResource(R.string.day_friday)
    )
    val hours = (1..12).map { it.toString().padStart(2, '0') }
    val minutes = listOf("00", "15", "30", "45")
    val periods = listOf(
        stringResource(R.string.period_am),
        stringResource(R.string.period_pm)
    )

    var startDay by remember { mutableStateOf(days.first()) }
    var endDay by remember { mutableStateOf(days.last()) }
    var openHour by remember { mutableStateOf("08") }
    var openMinute by remember { mutableStateOf("00") }
    var openPeriod by remember { mutableStateOf(periods.first()) }
    var closeHour by remember { mutableStateOf("05") }
    var closeMinute by remember { mutableStateOf("00") }
    var closePeriod by remember { mutableStateOf(periods.last()) }

    Text(
        text = stringResource(R.string.place_schedule_title),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
    Spacer(modifier = Modifier.height(12.dp))

    DropdownField(
        label = stringResource(R.string.schedule_label_start_day),
        options = days,
        selectedOption = startDay,
        onOptionSelected = { startDay = it }
    )
    Spacer(modifier = Modifier.height(8.dp))
    DropdownField(
        label = stringResource(R.string.schedule_label_end_day),
        options = days,
        selectedOption = endDay,
        onOptionSelected = { endDay = it }
    )

    Spacer(modifier = Modifier.height(16.dp))
    Text(stringResource(R.string.schedule_label_opening), fontWeight = FontWeight.SemiBold)
    Spacer(modifier = Modifier.height(8.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        DropdownField(label = stringResource(R.string.schedule_label_hour), options = hours, selectedOption = openHour, onOptionSelected = { openHour = it }, modifier = Modifier.weight(1f))
        DropdownField(label = stringResource(R.string.schedule_label_minute), options = minutes, selectedOption = openMinute, onOptionSelected = { openMinute = it }, modifier = Modifier.weight(1f))
        DropdownField(label = stringResource(R.string.schedule_label_period), options = periods, selectedOption = openPeriod, onOptionSelected = { openPeriod = it }, modifier = Modifier.weight(1f))
    }

    Spacer(modifier = Modifier.height(16.dp))
    Text(stringResource(R.string.schedule_label_closing), fontWeight = FontWeight.SemiBold)
    Spacer(modifier = Modifier.height(8.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        DropdownField(label = stringResource(R.string.schedule_label_hour), options = hours, selectedOption = closeHour, onOptionSelected = { closeHour = it }, modifier = Modifier.weight(1f))
        DropdownField(label = stringResource(R.string.schedule_label_minute), options = minutes, selectedOption = closeMinute, onOptionSelected = { closeMinute = it }, modifier = Modifier.weight(1f))
        DropdownField(label = stringResource(R.string.schedule_label_period), options = periods, selectedOption = closePeriod, onOptionSelected = { closePeriod = it }, modifier = Modifier.weight(1f))
    }

    Spacer(modifier = Modifier.height(16.dp))

    UniPrimaryButton(
        text = stringResource(R.string.schedule_button_add),
        onClick = {
            scheduleViewModel.addSchedule(
                startDay = startDay,
                endDay = endDay,
                openHour = openHour,
                openMinute = openMinute,
                openPeriod = openPeriod,
                closeHour = closeHour,
                closeMinute = closeMinute,
                closePeriod = closePeriod
            )
            val lastSchedule = scheduleViewModel.schedules.value.lastOrNull()
            if (lastSchedule != null) {
                onAddSchedule(lastSchedule)
                Toast.makeText(context, context.getString(R.string.msg_schedule_added), Toast.LENGTH_SHORT).show()
            }
        }
    )
}

/**
 * Displays the image upload and preview section for a place.
 *
 * Allows users to:
 *  - Add new placeholder images from Picsum
 *  - Preview existing images using Coil
 *  - Remove unwanted images
 *
 * @param imageUrls List of current image URLs to display.
 * @param onAddImage Callback triggered when a new image is added.
 * @param onRemoveImage Callback triggered when an image is deleted.
 */
@Composable
fun PlaceImagesSection(
    imageUrls: List<String>,
    onAddImage: (String) -> Unit,
    onRemoveImage: (Int) -> Unit
) {
    val context = LocalContext.current

    Text(
        text = stringResource(R.string.place_images_title),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
    Spacer(modifier = Modifier.height(12.dp))

    UniPrimaryButton(
        text = stringResource(R.string.action_add),
        onClick = {
            val newUrl = "https://picsum.photos/seed/${System.currentTimeMillis()}/900/500"
            onAddImage(newUrl)
            Toast.makeText(context, context.getString(R.string.msg_image_added), Toast.LENGTH_SHORT).show()
        }
    )

    if (imageUrls.isNotEmpty()) {
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            itemsIndexed(imageUrls) { index, url ->
                Box(modifier = Modifier.width(200.dp).height(130.dp)) {
                    Card(shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(url)
                                .crossfade(true)
                                .error(R.drawable.place_holder_svgrepo_com)
                                .placeholder(R.drawable.place_holder_svgrepo_com)
                                .build(),
                            contentDescription = stringResource(R.string.cd_place_image),
                            contentScale = ContentScale.Crop
                        )
                    }
                    IconButton(
                        onClick = {
                            onRemoveImage(index)
                            Toast.makeText(context, context.getString(R.string.msg_image_removed), Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(28.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.cd_delete_image), tint = Color.White)
                    }
                }
            }
        }
    }

}


