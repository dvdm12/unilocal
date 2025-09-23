package com.example.unilocal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unilocal.R
import com.example.unilocal.ui.components.AuthTextField
import com.example.unilocal.ui.components.DropdownField
import com.example.unilocal.ui.components.SocialButton
import com.example.unilocal.ui.components.UniPrimaryButton

@Composable
fun Register(
    onRegisterClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
    onFacebookClick: () -> Unit = {}
) {
    // Estados de entrada
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Datos para dropdowns
    val countries = listOf("Colombia", "México", "Argentina", "Perú")
    val cities = listOf("Bogotá", "Medellín", "Cali", "Barranquilla")

    // Scroll
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // Add a label at the top
        Text(
            text = stringResource(R.string.register_create_account),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        AuthTextField(
            value = name,
            onValueChange = { name = it },
            label = stringResource(R.string.register_name),
            placeholder = stringResource(R.string.register_name),
            leadingIcon = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = stringResource(R.string.register_lastname),
            placeholder = stringResource(R.string.register_lastname),
            leadingIcon = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthTextField(
            value = email,
            onValueChange = { email = it },
            label = stringResource(R.string.register_email),
            placeholder = stringResource(R.string.register_email),
            leadingIcon = Icons.Default.Email
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthTextField(
            value = phone,
            onValueChange = { phone = it },
            label = stringResource(R.string.register_phone),
            placeholder = stringResource(R.string.register_phone),
            leadingIcon = Icons.Default.Phone
        )

        Spacer(modifier = Modifier.height(12.dp))

        DropdownField(
            label = stringResource(R.string.register_country),
            options = countries,
            selectedOption = country,
            onOptionSelected = { country = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        DropdownField(
            label = stringResource(R.string.register_city),
            options = cities,
            selectedOption = city,
            onOptionSelected = { city = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthTextField(
            value = password,
            onValueChange = { password = it },
            label = stringResource(R.string.register_password),
            placeholder = stringResource(R.string.register_password),
            leadingIcon = Icons.Default.Lock,
            isPassword = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = stringResource(R.string.register_confirm_password),
            placeholder = stringResource(R.string.register_confirm_password),
            leadingIcon = Icons.Default.Lock,
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        UniPrimaryButton(
            text = stringResource(R.string.register_button),
            onClick = onRegisterClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.register_or_social),
            color = Color.Gray,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SocialButton(
                icon = painterResource(id = R.drawable.ic_launcher_foreground),
                text = stringResource(R.string.login_google),
                containerColor = Color(0xFFF5F5F5),
                contentColor = Color.Black,
                onClick = onGoogleClick
            )
            SocialButton(
                icon = painterResource(id = R.drawable.ic_launcher_foreground),
                text = stringResource(R.string.login_facebook),
                containerColor = Color(0xFF1877F2),
                contentColor = Color.White,
                onClick = onFacebookClick
            )
        }
        // Add extra space at the bottom
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    Register()
}
