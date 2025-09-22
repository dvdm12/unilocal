package com.example.unilocal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.AccountCircle
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
import com.example.unilocal.R

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val backgroundColor = Color(0xFFFDF4F0)
    val buttonOrange = Color(0xFFFF8C1A)
    val buttonTextColor = Color.White
    val footerTextColor = Color(0xFF888888)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Imagen superior
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = stringResource(R.string.welcome_image_description),
            modifier = Modifier
                .height(180.dp)
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Título
        Text(
            text = stringResource(R.string.welcome_title),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtítulo
        Text(
            text = stringResource(R.string.welcome_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de login
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonOrange,
                contentColor = buttonTextColor
            )
        ) {
            Text(
                text = stringResource(R.string.button_login),
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón de registro
        OutlinedButton(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Black
            )
        ) {
            Text(
                text = stringResource(R.string.button_register),
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Características con íconos
        FeatureList()

        Spacer(modifier = Modifier.weight(1f))

        // Footer legal
        Text(
            text = stringResource(R.string.footer_terms),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = footerTextColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun FeatureList() {
    Column(modifier = Modifier.fillMaxWidth()) {
        FeatureItem(icon = Icons.Outlined.LocationOn, text = stringResource(R.string.feature_nearby))
        FeatureItem(icon = Icons.Outlined.AccountCircle, text = stringResource(R.string.feature_review))
        FeatureItem(icon = Icons.Outlined.FavoriteBorder, text = stringResource(R.string.feature_favorites))
    }
}

@Composable
private fun FeatureItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
}
