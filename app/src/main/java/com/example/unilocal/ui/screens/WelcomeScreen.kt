package com.example.unilocal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unilocal.R
import com.example.unilocal.ui.components.home.Feature
import com.example.unilocal.ui.components.home.FeatureList
import com.example.unilocal.ui.components.home.UniLocalImage
import com.example.unilocal.ui.components.home.UniPrimaryButton
import com.example.unilocal.ui.theme.FooterTextColor

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val backgroundColor = Color(0xFFFDF4F0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Imagen superior reutilizable
        UniLocalImage()

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

        // Botón primario reutilizable
        UniPrimaryButton(
            text = stringResource(R.string.button_login),
            onClick = onLoginClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Botón secundario (outlined)
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

        // Lista de características reutilizable
        FeatureList(
            features = listOf(
                Feature(Icons.Outlined.LocationOn, stringResource(R.string.feature_nearby)),
                Feature(Icons.Outlined.Star, stringResource(R.string.feature_review)),
                Feature(Icons.Outlined.FavoriteBorder, stringResource(R.string.feature_favorites))
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        // Footer legal
        Text(
            text = stringResource(R.string.footer_terms),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = FooterTextColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
    }
}
