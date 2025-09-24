package com.example.unilocal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unilocal.R
import com.example.unilocal.ui.components.home.AuthTextField
import com.example.unilocal.ui.components.home.DropdownField
import com.example.unilocal.ui.components.home.SocialButton
import com.example.unilocal.ui.components.home.UniPrimaryButton

@Composable
private fun RegisterFields(
    name: String,
    lastName: String,
    email: String,
    phone: String,
    country: String,
    city: String,
    password: String,
    confirmPassword: String,
    onNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onCountryChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    countries: List<String>,
    cities: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AuthTextField(
                value = name,
                onValueChange = onNameChange,
                label = stringResource(R.string.register_name),
                placeholder = stringResource(R.string.register_name),
                leadingIcon = Icons.Default.Person
            )
            AuthTextField(
                value = lastName,
                onValueChange = onLastNameChange,
                label = stringResource(R.string.register_lastname),
                placeholder = stringResource(R.string.register_lastname),
                leadingIcon = Icons.Default.Person
            )
            AuthTextField(
                value = email,
                onValueChange = onEmailChange,
                label = stringResource(R.string.register_email),
                placeholder = stringResource(R.string.register_email),
                leadingIcon = Icons.Default.Email
            )
            AuthTextField(
                value = phone,
                onValueChange = onPhoneChange,
                label = stringResource(R.string.register_phone),
                placeholder = stringResource(R.string.register_phone),
                leadingIcon = Icons.Default.Phone
            )
            DropdownField(
                label = stringResource(R.string.register_country),
                options = countries,
                selectedOption = country,
                onOptionSelected = onCountryChange
            )
            DropdownField(
                label = stringResource(R.string.register_city),
                options = cities,
                selectedOption = city,
                onOptionSelected = onCityChange
            )
            AuthTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = stringResource(R.string.register_password),
                placeholder = stringResource(R.string.register_password),
                leadingIcon = Icons.Default.Lock,
                isPassword = true
            )
            AuthTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = stringResource(R.string.register_confirm_password),
                placeholder = stringResource(R.string.register_confirm_password),
                leadingIcon = Icons.Default.Lock,
                isPassword = true
            )
        }
    }
}

@Composable
private fun SocialLoginSection(
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SocialButton(
            icon = painterResource(id = R.drawable.ic_launcher_foreground),
            text = stringResource(R.string.login_google),
            containerColor = Color(0xFFDB4437), // rojo Google
            contentColor = Color.White,
            onClick = onGoogleClick
        )
        SocialButton(
            icon = painterResource(id = R.drawable.ic_launcher_foreground),
            text = stringResource(R.string.login_facebook),
            containerColor = Color(0xFF1877F2), // azul Facebook
            contentColor = Color.White,
            onClick = onFacebookClick
        )
    }
}

@Composable
private fun DividerWithText(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = Color.LightGray,
            thickness = 1.dp
        )
        Text(
            text = text,
            color = Color.Gray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = Color.LightGray,
            thickness = 1.dp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(
    onRegisterClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
    onFacebookClick: () -> Unit = {},
    onBackClick: () -> Unit = {} // callback para retroceder
) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val countries = listOf("Colombia", "México", "Argentina", "Perú")
    val cities = listOf("Bogotá", "Medellín", "Cali", "Barranquilla")

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.register_create_account),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RegisterFields(
                    name = name,
                    lastName = lastName,
                    email = email,
                    phone = phone,
                    country = country,
                    city = city,
                    password = password,
                    confirmPassword = confirmPassword,
                    onNameChange = { name = it },
                    onLastNameChange = { lastName = it },
                    onEmailChange = { email = it },
                    onPhoneChange = { phone = it },
                    onCountryChange = { country = it },
                    onCityChange = { city = it },
                    onPasswordChange = { password = it },
                    onConfirmPasswordChange = { confirmPassword = it },
                    countries = countries,
                    cities = cities
                )

                Spacer(modifier = Modifier.height(24.dp))

                UniPrimaryButton(
                    text = stringResource(R.string.register_button),
                    onClick = onRegisterClick
                )

                Spacer(modifier = Modifier.height(28.dp))

                DividerWithText(stringResource(R.string.register_or_social))

                Spacer(modifier = Modifier.height(28.dp))

                SocialLoginSection(
                    onGoogleClick = onGoogleClick,
                    onFacebookClick = onFacebookClick
                )

                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    Register()
}
