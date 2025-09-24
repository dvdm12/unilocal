package com.example.unilocal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unilocal.R
import com.example.unilocal.ui.components.home.UniLocalImage
import com.example.unilocal.ui.components.home.UniPrimaryButton
import com.example.unilocal.ui.theme.FooterTextColor

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFF8F5), Color(0xFFFCE9E3))
    )
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .verticalScroll(scrollState) // 👈 habilitamos scroll
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        WelcomeHeaderImage()

        Spacer(modifier = Modifier.height(24.dp))

        WelcomeTexts()

        Spacer(modifier = Modifier.height(36.dp))

        WelcomeActions(
            onLoginClick = onLoginClick,
            onRegisterClick = onRegisterClick
        )

        Spacer(modifier = Modifier.height(48.dp))

        WelcomeFeatures()

        Spacer(modifier = Modifier.height(40.dp))

        WelcomeFooter()
    }
}

@Composable
private fun WelcomeHeaderImage() {
    Card(
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        UniLocalImage()
    }
}

@Composable
private fun WelcomeTexts() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.welcome_title),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.welcome_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun WelcomeActions(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    UniPrimaryButton(
        text = stringResource(R.string.button_login),
        onClick = onLoginClick
    )

    Spacer(modifier = Modifier.height(16.dp))

    FilledTonalButton(
        onClick = onRegisterClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = stringResource(R.string.button_register),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
private fun WelcomeFeatures() {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        FeatureItem(
            icon = Icons.Outlined.LocationOn,
            text = stringResource(R.string.feature_nearby),
            iconBgColor = Color(0xFFEEF4FF)
        )
        FeatureItem(
            icon = Icons.Outlined.Star,
            text = stringResource(R.string.feature_review),
            iconBgColor = Color(0xFFFFF3E0)
        )
        FeatureItem(
            icon = Icons.Outlined.FavoriteBorder,
            text = stringResource(R.string.feature_favorites),
            iconBgColor = Color(0xFFFDE2E2)
        )
    }
}

@Composable
private fun FeatureItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    iconBgColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun WelcomeFooter() {
    HorizontalDivider(
        color = Color.LightGray.copy(alpha = 0.3f),
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 12.dp)
    )

    Text(
        text = stringResource(R.string.footer_terms),
        style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 11.sp,
            color = FooterTextColor
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(
        onLoginClick = {},
        onRegisterClick = {}
    )
}
