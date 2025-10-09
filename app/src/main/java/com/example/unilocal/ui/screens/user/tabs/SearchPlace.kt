package com.example.unilocal.ui.screens.user.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unilocal.R
import com.example.unilocal.ui.components.users.SimpleTopBar

/**
 * Screen for searching and selecting a place location.
 * Allows the user to input an address, preview a map, and confirm the location.
 * @param onClose Callback when the user closes the screen.
 * @param onUseLocation Callback when the user confirms the location.
 */
@Composable
fun SearchPlace(
    onClose: () -> Unit={},
    onUseLocation: () -> Unit={}
) {
    var address by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        SimpleTopBar(
            title = stringResource(R.string.location_screen_title),
            onLogoutClick = onClose
        )

        Spacer(modifier = Modifier.height(16.dp))

        SearchLocationBar(
            address = address,
            onAddressChange = { address = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        MapPreview()

        Spacer(modifier = Modifier.height(16.dp))

        SelectedAddressView(address)

        Spacer(modifier = Modifier.height(16.dp))

        ConfirmLocationButton(onUseLocation)
    }
}

/**
 * Text field for searching an address or location.
 * @param address Current address input value.
 * @param onAddressChange Callback for address input changes.
 */
@Composable
fun SearchLocationBar(
    address: String,
    onAddressChange: (String) -> Unit
) {
    OutlinedTextField(
        value = address,
        onValueChange = onAddressChange,
        placeholder = { Text(stringResource(R.string.search_placeholder)) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color(0xFFF6F2EE),
            unfocusedContainerColor = Color(0xFFF6F2EE),
            cursorColor = Color.Black
        )
    )
}

/**
 * Displays a preview image of the map.
 */
@Composable
fun MapPreview() {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = stringResource(R.string.map_preview_description),
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
    )
}

/**
 * Shows the selected address or a placeholder if none is selected.
 * @param address The selected address string.
 */
@Composable
fun SelectedAddressView(address: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = address.ifBlank { stringResource(R.string.no_address_selected) },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * Button to confirm and use the selected location.
 * @param onUseLocation Callback when the button is clicked.
 */
@Composable
fun ConfirmLocationButton(onUseLocation: () -> Unit) {
    Button(
        onClick = onUseLocation,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEB9B00)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(stringResource(R.string.use_this_location))
    }
}

/**
 * Preview for the SearchPlace screen.
 */
@Preview(showBackground = true)
@Composable
fun PreviewSearchPlace() {
    SearchPlace(
        onClose = {},
        onUseLocation = {}
    )
}
