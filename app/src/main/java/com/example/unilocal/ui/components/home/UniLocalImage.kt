package com.example.unilocal.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.unilocal.R

@Composable
fun UniLocalImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = stringResource(R.string.welcome_image_description),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}
