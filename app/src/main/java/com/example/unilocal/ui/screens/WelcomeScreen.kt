package com.example.unilocal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.1f))

        // Título
        Text(
            text = stringResource(R.string.welcome_title),
            style = MaterialTheme.typography.titleLarge,
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

        // Botón de login (naranja)
        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonOrange,
                contentColor = buttonTextColor
            )
        ) {
            Text(
                text = stringResource(R.string.button_login),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón de registro (gris claro)
        OutlinedButton(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Black
            ),
            border = ButtonDefaults.outlinedButtonBorder(enabled = true)
        ) {
            Text(
                text = stringResource(R.string.button_register),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

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
    val modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp)

    Column(modifier = modifier) {
        Text("• ${stringResource(R.string.feature_nearby)}", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
        Text("• ${stringResource(R.string.feature_review)}", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
        Text("• ${stringResource(R.string.feature_favorites)}", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
    }
}
