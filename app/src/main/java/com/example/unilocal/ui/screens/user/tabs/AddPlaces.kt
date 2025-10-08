package com.example.unilocal.ui.screens.user.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.unilocal.R
import com.example.unilocal.ui.components.home.SocialButton
import com.example.unilocal.ui.components.users.SimpleTopBar
import com.example.unilocal.ui.theme.OrangePrimary

/**
 * Main screen for adding a new place. Displays an image, title, description, and a button to start the process.
 * @param onBackClick Callback for the back button in the top bar.
 * @param onStartClick Callback for the start button at the bottom.
 */
@Composable
fun AddPlace(
    onBackClick: () -> Unit = {},
    onStartClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            SimpleTopBar(
                title = stringResource(R.string.add_place_title),
                onLogoutClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AddPlaceImage()
            Spacer(modifier = Modifier.height(32.dp))
            AddPlaceTitle()
            Spacer(modifier = Modifier.height(16.dp))
            AddPlaceDescription()
            Spacer(modifier = Modifier.height(32.dp))
            AddPlaceButton(onClick = onStartClick)
        }
    }
}

/**
 * Displays the main image for the Add Place screen.
 */
@Composable
fun AddPlaceImage() {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = stringResource(R.string.add_place_image_desc),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

/**
 * Displays the subtitle for the Add Place screen.
 */
@Composable
fun AddPlaceTitle() {
    Text(
        text = stringResource(R.string.add_place_subtitle),
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
    )
}

/**
 * Displays the description for the Add Place screen.
 */
@Composable
fun AddPlaceDescription() {
    Text(
        text = stringResource(R.string.add_place_description),
        fontSize = 14.sp,
        color = Color.Gray,
        textAlign = TextAlign.Center
    )
}

/**
 * Button to start the process of adding a new place, reusing SocialButton.
 * @param onClick Callback for the button click.
 */
@Composable
fun AddPlaceButton(onClick: () -> Unit) {
    SocialButton(
        icon = painterResource(id = R.drawable.ic_launcher_foreground),
        text = stringResource(R.string.add_place_button_start),
        containerColor = OrangePrimary,
        contentColor = Color.White,
        onClick = onClick
    )
}

/**
 * Preview for the AddPlace screen.
 */
@Preview(showBackground = true)
@Composable
fun AddPlacePreview() {
    AddPlace()
}
