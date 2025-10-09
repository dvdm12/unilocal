package com.example.unilocal.ui.screens.user.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import com.example.unilocal.ui.components.home.AuthTextField
import com.example.unilocal.ui.components.home.AuthFieldType
import com.example.unilocal.ui.components.home.DropdownField
import com.example.unilocal.ui.components.home.UniPrimaryButton
import com.example.unilocal.ui.components.users.SimpleTopBar
import kotlinx.coroutines.launch

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
    val email by remember { mutableStateOf("sofia.ramirez@email.com") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileTopBar(onBackClick: () -> Unit) {
    SimpleTopBar(
        title = stringResource(R.string.edit_profile_title),
        onLogoutClick = onBackClick
    )
}

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
        fieldType = AuthFieldType.Text
    )

    AuthTextField(
        value = lastname,
        onValueChange = onLastnameChange,
        label = stringResource(R.string.register_lastname),
        placeholder = stringResource(R.string.register_lastname),
        leadingIcon = Icons.Default.Person,
        fieldType = AuthFieldType.Text
    )

    AuthTextField(
        value = username,
        onValueChange = onUsernameChange,
        label = stringResource(R.string.edit_profile_username_label),
        placeholder = stringResource(R.string.edit_profile_username_placeholder),
        leadingIcon = Icons.Default.Person,
        fieldType = AuthFieldType.Text
    )

    AuthTextField(
        value = phone,
        onValueChange = onPhoneChange,
        label = stringResource(R.string.register_phone),
        placeholder = stringResource(R.string.register_phone),
        leadingIcon = Icons.Default.Phone,
        fieldType = AuthFieldType.Text
    )
}

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
        fieldType = AuthFieldType.Password
    )

    AuthTextField(
        value = newPassword,
        onValueChange = onNewPasswordChange,
        label = stringResource(R.string.edit_profile_new_password_label),
        placeholder = stringResource(R.string.edit_profile_new_password_placeholder),
        leadingIcon = Icons.Default.Lock,
        fieldType = AuthFieldType.Password
    )

    AuthTextField(
        value = confirmPassword,
        onValueChange = onConfirmPasswordChange,
        label = stringResource(R.string.edit_profile_confirm_password_label),
        placeholder = stringResource(R.string.edit_profile_confirm_password_placeholder),
        leadingIcon = Icons.Default.Lock,
        fieldType = AuthFieldType.Password
    )
}

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

@Preview(
    name = "Pantalla completa",
    showSystemUi = true,
    showBackground = true,
    device = "spec:width=411dp,height=1200dp,dpi=440"
)
@Composable
fun EditProfilePreview() {
    EditProfileScreen()
}
