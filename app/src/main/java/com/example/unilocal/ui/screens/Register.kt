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
import com.example.unilocal.ui.components.home.AuthFieldType
import com.example.unilocal.ui.components.home.AuthTextField
import com.example.unilocal.ui.components.home.DropdownField
import com.example.unilocal.ui.components.home.SocialButton
import com.example.unilocal.ui.components.home.UniPrimaryButton

/**
 * Renders the full user registration screen.
 * Includes form fields, social login buttons, and top navigation.
 *
 * @param onRegisterClick Callback triggered when the user submits the form.
 * @param onGoogleClick Callback for Google login button.
 * @param onFacebookClick Callback for Facebook login button.
 * @param onBackClick Callback for back navigation icon.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(
    onRegisterClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
    onFacebookClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
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

/**
 * Renders the form fields used in the registration screen.
 *
 * @param name, lastName, email, phone, country, city, password, confirmPassword: Current field values.
 * @param countries List of available countries for dropdown.
 * @param cities List of available cities for dropdown.
 */
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
                leadingIcon = Icons.Default.Person,
                fieldType = AuthFieldType.Text
            )
            AuthTextField(
                value = lastName,
                onValueChange = onLastNameChange,
                label = stringResource(R.string.register_lastname),
                placeholder = stringResource(R.string.register_lastname),
                leadingIcon = Icons.Default.Person,
                fieldType = AuthFieldType.Text
            )
            AuthTextField(
                value = email,
                onValueChange = onEmailChange,
                label = stringResource(R.string.register_email),
                placeholder = stringResource(R.string.register_email),
                leadingIcon = Icons.Default.Email,
                fieldType = AuthFieldType.Email
            )
            AuthTextField(
                value = phone,
                onValueChange = onPhoneChange,
                label = stringResource(R.string.register_phone),
                placeholder = stringResource(R.string.register_phone),
                leadingIcon = Icons.Default.Phone,
                fieldType = AuthFieldType.Text
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
                fieldType = AuthFieldType.Password
            )
            AuthTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = stringResource(R.string.register_confirm_password),
                placeholder = stringResource(R.string.register_confirm_password),
                leadingIcon = Icons.Default.Lock,
                fieldType = AuthFieldType.Password
            )
        }
    }
}


/**
 * Renders a section with Google and Facebook login buttons.
 *
 * @param onGoogleClick Callback for Google login.
 * @param onFacebookClick Callback for Facebook login.
 */
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
            containerColor = Color(0xFFDB4437), // Google red
            contentColor = Color.White,
            onClick = onGoogleClick
        )
        SocialButton(
            icon = painterResource(id = R.drawable.ic_launcher_foreground),
            text = stringResource(R.string.login_facebook),
            containerColor = Color(0xFF1877F2), // Facebook blue
            contentColor = Color.White,
            onClick = onFacebookClick
        )
    }
}

/**
 * Displays a horizontal divider line with centered text in between.
 *
 * @param text The text to display between the dividers.
 */
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

/**
 * Preview for the Register screen, shown in Android Studio preview.
 */
@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    Register()
}
