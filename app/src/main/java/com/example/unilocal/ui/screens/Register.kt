package com.example.unilocal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unilocal.R
import com.example.unilocal.ui.components.home.*
import com.example.unilocal.viewmodel.register.RegisterViewModel

/**
 * Pantalla de registro de usuario conectada al RegisterViewModel.
 * Muestra validaciones en tiempo real y retroalimentación contextual.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(
    viewModel: RegisterViewModel,
    onRegisterSuccess: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // --- Relación País → Ciudades ---
    val countryCities = mapOf(
        "Colombia" to listOf("Armenia", "Bogotá", "Medellín", "Cali"),
        "México" to listOf("CDMX", "Guadalajara", "Monterrey"),
        "Argentina" to listOf("Buenos Aires", "Córdoba", "Rosario"),
        "Perú" to listOf("Lima", "Cusco", "Arequipa")
    )

    val countries = countryCities.keys.toList()
    val cities = countryCities[uiState.country] ?: emptyList()

    // --- Reacción ante éxito o error ---
    LaunchedEffect(uiState.isSuccess, uiState.errorMessage) {
        if (uiState.isSuccess) showSuccessDialog = true
        if (uiState.errorMessage != null) showErrorDialog = true
    }

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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_content_desc)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // --- Campos del formulario ---
                RegisterFields(
                    uiState = uiState,
                    countries = countries,
                    cities = cities,
                    onNameChange = viewModel::onNameChange,
                    onLastNameChange = viewModel::onLastNameChange,
                    onEmailChange = viewModel::onEmailChange,
                    onPhoneChange = viewModel::onPhoneChange,
                    onCountryChange = viewModel::onCountryChange,
                    onCityChange = viewModel::onCityChange,
                    onPasswordChange = viewModel::onPasswordChange,
                    onConfirmPasswordChange = viewModel::onConfirmPasswordChange
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- Botón principal ---
                UniPrimaryButton(
                    text = stringResource(R.string.register_button),
                    onClick = {
                        // Solo permitir el registro si el formulario es válido
                        if (uiState.isValid) {
                            viewModel.onRegisterClick()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))
                DividerWithText(stringResource(R.string.register_or_social))
                Spacer(modifier = Modifier.height(28.dp))
                SocialLoginSection()
                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }

    // --- Diálogo de error ---
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(stringResource(R.string.login_error_title)) },
            text = { Text(uiState.errorMessage ?: "") },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text(stringResource(R.string.login_error_accept))
                }
            }
        )
    }

    // --- Diálogo de éxito ---
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text(stringResource(R.string.register_create_account)) },
            text = { Text(stringResource(R.string.snackbar_user_updated)) },
            confirmButton = {
                TextButton(onClick = {
                    showSuccessDialog = false
                    onRegisterSuccess()
                }) {
                    Text(stringResource(R.string.accept))
                }
            }
        )
    }
}

/**
 * Campos del formulario conectados al estado del ViewModel.
 */
@Composable
private fun RegisterFields(
    uiState: com.example.unilocal.viewmodel.register.RegisterUiState,
    countries: List<String>,
    cities: List<String>,
    onNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onCountryChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AuthTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                label = stringResource(R.string.register_name),
                placeholder = stringResource(R.string.register_name),
                leadingIcon = Icons.Default.Person,
                fieldType = AuthFieldType.Text,
                errorMessage = uiState.nameError
            )
            AuthTextField(
                value = uiState.lastName,
                onValueChange = onLastNameChange,
                label = stringResource(R.string.register_lastname),
                placeholder = stringResource(R.string.register_lastname),
                leadingIcon = Icons.Default.Person,
                fieldType = AuthFieldType.Text,
                errorMessage = uiState.lastNameError
            )
            AuthTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                label = stringResource(R.string.register_email),
                placeholder = stringResource(R.string.register_email),
                leadingIcon = Icons.Default.Email,
                fieldType = AuthFieldType.Email,
                errorMessage = uiState.emailError
            )
            AuthTextField(
                value = uiState.phone,
                onValueChange = onPhoneChange,
                label = stringResource(R.string.register_phone),
                placeholder = stringResource(R.string.register_phone),
                leadingIcon = Icons.Default.Phone,
                fieldType = AuthFieldType.Text,
                errorMessage = uiState.phoneError
            )
            DropdownField(
                label = stringResource(R.string.register_country),
                options = countries,
                selectedOption = uiState.country,
                onOptionSelected = onCountryChange
            )
            DropdownField(
                label = stringResource(R.string.register_city),
                options = cities,
                selectedOption = uiState.city,
                onOptionSelected = onCityChange
            )
            AuthTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = stringResource(R.string.register_password),
                placeholder = stringResource(R.string.register_password),
                leadingIcon = Icons.Default.Lock,
                fieldType = AuthFieldType.Password,
                errorMessage = uiState.passwordError
            )
            AuthTextField(
                value = uiState.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = stringResource(R.string.register_confirm_password),
                placeholder = stringResource(R.string.register_confirm_password),
                leadingIcon = Icons.Default.Lock,
                fieldType = AuthFieldType.Password,
                errorMessage = uiState.confirmPasswordError
            )
        }
    }
}

/**
 * Sección de botones sociales (Google y Facebook).
 */
@Composable
private fun SocialLoginSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SocialButton(
            icon = painterResource(id = R.drawable.ic_launcher_foreground),
            text = stringResource(R.string.login_google),
            containerColor = Color(0xFFDB4437),
            contentColor = Color.White,
            onClick = {}
        )
        SocialButton(
            icon = painterResource(id = R.drawable.ic_launcher_foreground),
            text = stringResource(R.string.login_facebook),
            containerColor = Color(0xFF1877F2),
            contentColor = Color.White,
            onClick = {}
        )
    }
}

/**
 * Línea divisoria con texto centrado.
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
