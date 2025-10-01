package com.example.unilocal.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.unilocal.R
import com.example.unilocal.ui.theme.OrangePrimary
import com.example.unilocal.ui.components.home.AuthTextField
import com.example.unilocal.ui.components.home.DropdownField
import com.example.unilocal.ui.components.home.UniPrimaryButton
import kotlinx.coroutines.launch

/**
 * Main composable for the Edit Profile screen.
 * Allows users to update their profile information, location, email, and password.
 * Displays a top bar, input fields, dropdowns, and action buttons.
 * Shows snackbar messages for field validation and save actions.
 *
 * @param onBackClick Callback for the back navigation icon.
 * @param onSaveClick Callback for the save button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("Colombia") }
    var department by remember { mutableStateOf("Quindío") }
    var city by remember { mutableStateOf("Armenia") }
    val email by remember { mutableStateOf("sofia.ramirez@email.com" ) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ✅ Strings capturados en contexto composable
    val msgFieldsCleared = stringResource(R.string.snackbar_fields_cleared)
    val msgEmptyFields = stringResource(R.string.snackbar_error_empty_fields)
    val msgUserUpdated = stringResource(R.string.snackbar_user_updated)

    Scaffold(
        topBar = { EditProfileTopBar(onBackClick) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            UserInfoSection(
                name, { name = it },
                lastname, { lastname = it },
                username, { username = it },
                phone, { phone = it }
            )

            LocationDropdowns(
                country, { country = it },
                department, { department = it },
                city, { city = it }
            )

            EmailField(email)

            PasswordSection(
                currentPassword, { currentPassword = it },
                newPassword, { newPassword = it },
                confirmPassword, { confirmPassword = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            ActionButtons(
                onClear = {
                    name = ""
                    lastname = ""
                    username = ""
                    phone = ""
                    country = ""
                    department = ""
                    city = ""
                    currentPassword = ""
                    newPassword = ""
                    confirmPassword = ""
                    scope.launch {
                        snackbarHostState.showSnackbar(msgFieldsCleared)
                    }
                },
                onSave = {
                    if (name.isBlank() || lastname.isBlank() || username.isBlank() ||
                        phone.isBlank() || country.isBlank() || department.isBlank() ||
                        city.isBlank() || currentPassword.isBlank() ||
                        newPassword.isBlank() || confirmPassword.isBlank()
                    ) {
                        scope.launch {
                            snackbarHostState.showSnackbar(msgEmptyFields)
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(msgUserUpdated)
                        }
                        onSaveClick()
                    }
                }
            )
        }
    }
}

/**
 * Top app bar for the Edit Profile screen.
 * Displays the screen title and a back navigation icon.
 *
 * @param onBackClick Callback for the back navigation icon.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileTopBar(onBackClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.edit_profile_title), fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back_content_desc))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = OrangePrimary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

/**
 * Section for user information fields: name, lastname, username, and phone.
 *
 * @param name Current name value.
 * @param onNameChange Callback for updating the name.
 * @param lastname Current lastname value.
 * @param onLastnameChange Callback for updating the lastname.
 * @param username Current username value.
 * @param onUsernameChange Callback for updating the username.
 * @param phone Current phone value.
 * @param onPhoneChange Callback for updating the phone.
 */
@Composable
fun UserInfoSection(
    name: String, onNameChange: (String) -> Unit,
    lastname: String, onLastnameChange: (String) -> Unit,
    username: String, onUsernameChange: (String) -> Unit,
    phone: String, onPhoneChange: (String) -> Unit
) {
    AuthTextField(
        value = name,
        onValueChange = onNameChange,
        label = stringResource(R.string.register_name),
        placeholder = stringResource(R.string.edit_profile_name_placeholder),
        leadingIcon = Icons.Default.Person,
        fieldType = "text"
    )

    AuthTextField(
        value = lastname,
        onValueChange = onLastnameChange,
        label = stringResource(R.string.register_lastname),
        placeholder = stringResource(R.string.register_lastname),
        leadingIcon = Icons.Default.Person,
        fieldType = "text"
    )

    AuthTextField(
        value = username,
        onValueChange = onUsernameChange,
        label = stringResource(R.string.edit_profile_username_label),
        placeholder = stringResource(R.string.edit_profile_username_placeholder),
        leadingIcon = Icons.Default.Person,
        fieldType = "text"
    )

    AuthTextField(
        value = phone,
        onValueChange = onPhoneChange,
        label = stringResource(R.string.register_phone),
        placeholder = stringResource(R.string.register_phone),
        leadingIcon = Icons.Default.Phone,
        fieldType = "text"
    )
}

/**
 * Section for location dropdowns: country, department, and city.
 *
 * @param country Current country value.
 * @param onCountryChange Callback for updating the country.
 * @param department Current department value.
 * @param onDepartmentChange Callback for updating the department.
 * @param city Current city value.
 * @param onCityChange Callback for updating the city.
 */
@Composable
fun LocationDropdowns(
    country: String, onCountryChange: (String) -> Unit,
    department: String, onDepartmentChange: (String) -> Unit,
    city: String, onCityChange: (String) -> Unit
) {
    DropdownField(
        label = stringResource(R.string.edit_profile_country_label),
        options = listOf("Colombia", "México", "Argentina"),
        selectedOption = country,
        onOptionSelected = onCountryChange
    )

    DropdownField(
        label = stringResource(R.string.edit_profile_department_label),
        options = listOf("Quindío", "Antioquia", "Valle"),
        selectedOption = department,
        onOptionSelected = onDepartmentChange
    )

    DropdownField(
        label = stringResource(R.string.edit_profile_city_label),
        options = listOf("Armenia", "Medellín", "Cali"),
        selectedOption = city,
        onOptionSelected = onCityChange
    )
}

/**
 * Displays the user's email field (read-only).
 *
 * @param email User's email address.
 */
@Composable
fun EmailField(email: String) {
    OutlinedTextField(
        value = email,
        onValueChange = {},
        readOnly = true,
        label = { Text(stringResource(R.string.edit_profile_email_label)) },
        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * Section for password fields: current, new, and confirm password.
 *
 * @param currentPassword Current password value.
 * @param onCurrentPasswordChange Callback for updating the current password.
 * @param newPassword New password value.
 * @param onNewPasswordChange Callback for updating the new password.
 * @param confirmPassword Confirm password value.
 * @param onConfirmPasswordChange Callback for updating the confirm password.
 */
@Composable
fun PasswordSection(
    currentPassword: String, onCurrentPasswordChange: (String) -> Unit,
    newPassword: String, onNewPasswordChange: (String) -> Unit,
    confirmPassword: String, onConfirmPasswordChange: (String) -> Unit
) {
    Text(
        text = stringResource(R.string.edit_profile_password_section),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )

    AuthTextField(
        value = currentPassword,
        onValueChange = onCurrentPasswordChange,
        label = stringResource(R.string.edit_profile_current_password_label),
        placeholder = stringResource(R.string.edit_profile_current_password_placeholder),
        leadingIcon = Icons.Default.Lock,
        isPassword = true,
        fieldType = "password",
        passwordErrorText = stringResource(R.string.login_password_error)
    )

    AuthTextField(
        value = newPassword,
        onValueChange = onNewPasswordChange,
        label = stringResource(R.string.edit_profile_new_password_label),
        placeholder = stringResource(R.string.edit_profile_new_password_placeholder),
        leadingIcon = Icons.Default.Lock,
        isPassword = true,
        fieldType = "password",
        passwordErrorText = stringResource(R.string.login_password_error)
    )

    AuthTextField(
        value = confirmPassword,
        onValueChange = onConfirmPasswordChange,
        label = stringResource(R.string.edit_profile_confirm_password_label),
        placeholder = stringResource(R.string.edit_profile_confirm_password_placeholder),
        leadingIcon = Icons.Default.Lock,
        isPassword = true,
        fieldType = "password"
    )
}

/**
 * Displays action buttons for clearing fields and saving changes.
 *
 * @param onClear Callback for the clear button.
 * @param onSave Callback for the save button.
 */
@Composable
fun ActionButtons(
    onClear: () -> Unit,
    onSave: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onClear,
            modifier = Modifier.weight(1f)
        ) {
            Text(stringResource(R.string.edit_profile_button_clear))
        }

        UniPrimaryButton(
            text = stringResource(R.string.edit_profile_button_save),
            onClick = onSave,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Preview composable for the Edit Profile screen.
 * Used to display a preview of EditProfileScreen in the Android Studio design editor.
 */
@Preview(showBackground = true)
@Composable
fun EditProfilePreview() {
    EditProfileScreen()
}
