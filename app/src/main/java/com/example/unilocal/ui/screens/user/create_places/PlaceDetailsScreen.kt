package com.example.unilocal.ui.screens.user.create_places

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unilocal.R
import com.example.unilocal.ui.components.home.*
import com.example.unilocal.ui.components.users.SimpleTopBar

@Composable
fun PlaceDetailsScreen(
    onBackClick: () -> Unit = {},
    onPublishClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

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
            TopMapIcon(modifier = Modifier.align(Alignment.CenterHorizontally))

            val name = remember { mutableStateOf("") }
            val category = remember { mutableStateOf("General") }
            val description = remember { mutableStateOf("") }
            val phone = remember { mutableStateOf("") }
            val country = remember { mutableStateOf("Colombia") }
            val department = remember { mutableStateOf("Quindío") }
            val city = remember { mutableStateOf("Armenia") }
            val street = remember { mutableStateOf("") }
            val images = remember { mutableStateListOf<Int>() } // Simula con resource IDs o URIs

            PlaceFormSection(name, category, description, phone)
            Spacer(modifier = Modifier.height(16.dp))
            PlaceScheduleSection()
            Spacer(modifier = Modifier.height(16.dp))
            PlaceLocationDropdowns(country, department, city, street)
            Spacer(modifier = Modifier.height(16.dp))
            PlaceImagesSection(images)
            Spacer(modifier = Modifier.height(24.dp))
            UniPrimaryButton(
                text = stringResource(R.string.place_details_publish),
                onClick = onPublishClick
            )
        }
    }
}

@Composable
fun PlaceFormSection(
    name: MutableState<String>,
    category: MutableState<String>,
    description: MutableState<String>,
    phone: MutableState<String>
) {
    AuthTextField(
        value = name.value,
        onValueChange = { name.value = it },
        label = stringResource(R.string.place_details_name),
        placeholder = stringResource(R.string.place_details_name_placeholder),
        leadingIcon = Icons.Default.Phone,
        isPassword = false,
        fieldType = "text",
        emailErrorText = "",
        passwordErrorText = ""
    )
    Spacer(modifier = Modifier.height(12.dp))
    DropdownField(
        label = stringResource(R.string.place_details_category),
        options = listOf("General", "Comercio", "Educación", "Salud", "Turismo"),
        selectedOption = category.value,
        onOptionSelected = { category.value = it }
    )
    Spacer(modifier = Modifier.height(12.dp))
    AuthTextField(
        value = description.value,
        onValueChange = { description.value = it },
        label = stringResource(R.string.place_details_description),
        placeholder = stringResource(R.string.place_details_description_placeholder),
        leadingIcon = Icons.Default.Star,
        isPassword = false,
        fieldType = "text",
        emailErrorText = "",
        passwordErrorText = ""
    )
    Spacer(modifier = Modifier.height(12.dp))
    AuthTextField(
        value = phone.value,
        onValueChange = { phone.value = it },
        label = stringResource(R.string.place_details_phone),
        placeholder = stringResource(R.string.place_details_phone_placeholder),
        leadingIcon = Icons.Default.Phone,
        isPassword = false,
        fieldType = "text",
        emailErrorText = "",
        passwordErrorText = ""
    )
}

@Composable
fun PlaceScheduleSection() {
    val days = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
    var selectedDay by remember { mutableStateOf(days[0]) }
    var openTime by remember { mutableStateOf("") }
    var closeTime by remember { mutableStateOf("") }

    Text(
        text = stringResource(R.string.place_details_schedule_title),
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    )
    Spacer(modifier = Modifier.height(8.dp))
    DropdownField(
        label = selectedDay,
        options = days,
        selectedOption = selectedDay,
        onOptionSelected = { selectedDay = it }
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        AuthTextField(
            value = openTime,
            onValueChange = { openTime = it },
            label = stringResource(R.string.place_details_open_time),
            placeholder = stringResource(R.string.place_details_time_example),
            leadingIcon = Icons.Default.Person,
            isPassword = false,
            fieldType = "text",
            emailErrorText = "",
            passwordErrorText = "",
            modifier = Modifier.weight(1f).height(40.dp)
        )
        AuthTextField(
            value = closeTime,
            onValueChange = { closeTime = it },
            label = stringResource(R.string.place_details_close_time),
            placeholder = stringResource(R.string.place_details_time_example),
            leadingIcon = Icons.Default.Add,
            isPassword = false,
            fieldType = "text",
            emailErrorText = "",
            passwordErrorText = "",
            modifier = Modifier.weight(1f).height(40.dp)
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
    UniPrimaryButton(
        text = stringResource(R.string.place_details_add_schedule),
        onClick = {
            // Aquí iría lógica de agregar a la lista de horarios
        }
    )
}

/**
 * Displays dropdowns for selecting country, department, city, and a text field for street.
 * Each dropdown and the text field are separated by vertical spacers for clarity.
 *
 * @param country MutableState holding the selected country.
 * @param department MutableState holding the selected department.
 * @param city MutableState holding the selected city.
 * @param street MutableState holding the entered street.
 */
@Composable
fun PlaceLocationDropdowns(
    country: MutableState<String>,
    department: MutableState<String>,
    city: MutableState<String>,
    street: MutableState<String>
) {
    Text(
        text = stringResource(R.string.place_details_location_title),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    DropdownField(
        label = stringResource(R.string.place_details_country),
        options = listOf("Colombia", "México", "Argentina"),
        selectedOption = country.value,
        onOptionSelected = { country.value = it }
    )
    Spacer(modifier = Modifier.height(12.dp))
    DropdownField(
        label = stringResource(R.string.place_details_department),
        options = listOf("Quindío", "Antioquia", "Valle"),
        selectedOption = department.value,
        onOptionSelected = { department.value = it }
    )
    Spacer(modifier = Modifier.height(12.dp))
    DropdownField(
        label = stringResource(R.string.place_details_city),
        options = listOf("Armenia", "Medellín", "Cali"),
        selectedOption = city.value,
        onOptionSelected = { city.value = it }
    )
    Spacer(modifier = Modifier.height(12.dp))
    AuthTextField(
        value = street.value,
        onValueChange = { street.value = it },
        label = stringResource(R.string.place_details_street),
        placeholder = stringResource(R.string.place_details_street_placeholder),
        leadingIcon = Icons.Default.AccountBox,
        isPassword = false,
        fieldType = "text",
        emailErrorText = "",
        passwordErrorText = "",
        modifier = Modifier.fillMaxWidth().height(45.dp)
    )
}

@Composable
fun PlaceImagesSection(images: MutableList<Int>) {
    Text(
        text = stringResource(R.string.place_details_images_title),
        fontWeight = FontWeight.SemiBold
    )
    Spacer(modifier = Modifier.height(8.dp))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(images.size) { index ->
            Image(
                painter = painterResource(id = images[index]),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedButton(
        onClick = {
            // Simula agregar imagen
            images.add(R.drawable.ic_launcher_foreground)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.place_details_add_image))
    }
}


@Composable
fun TopMapIcon(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.place_holder_svgrepo_com),
        contentDescription = stringResource(R.string.map_icon),
        modifier = modifier
            .size(234.dp)
            .padding(bottom = 16.dp)
    )
}




/**
 * Preview for PlaceDetailsScreen.
 */
@Preview(
    name = "Pantalla completa",
    showSystemUi = true,
    showBackground = true,
    device = "spec:width=411dp,height=1400dp,dpi=440"
)
@Composable
fun PlaceDetailsScreenPreview() {
    PlaceDetailsScreen()
}
