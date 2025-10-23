package com.example.unilocal.ui.screens.user.tabs

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unilocal.R
import com.example.unilocal.ui.components.home.AuthFieldType
import com.example.unilocal.ui.components.home.AuthTextField
import com.example.unilocal.ui.components.home.DropdownField
import com.example.unilocal.ui.components.users.SimpleTopBar
import com.example.unilocal.ui.theme.OrangePrimary
import com.example.unilocal.viewmodel.data.session.UserSessionViewModel
import com.example.unilocal.viewmodel.user.update.UserUpdateViewModel
import kotlinx.coroutines.launch

/**
 * EditProfileScreen
 *
 * Displays a scrollable form that allows the user to update their profile information.
 * It integrates with [UserUpdateViewModel] for validation and persistence,
 * and [UserSessionViewModel] for managing the current session state.
 *
 * Features:
 * - Allows editing of name, lastname, username, email, country, city, and password.
 * - Preserves immutable fields such as places, isActive, and role.
 * - Country and city dropdowns are optional and not validated.
 * - Requires current password to authorize password changes.
 * - Displays real-time validation feedback and snackbar messages.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    userSessionViewModel: UserSessionViewModel,
    userUpdateViewModel: UserUpdateViewModel,
    onBackClick: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Observed state fields
    val name by userUpdateViewModel.name.collectAsState()
    val lastname by userUpdateViewModel.lastname.collectAsState()
    val username by userUpdateViewModel.username.collectAsState()
    val email by userUpdateViewModel.email.collectAsState()
    val country by userUpdateViewModel.country.collectAsState()
    val city by userUpdateViewModel.city.collectAsState()
    val currentPassword by userUpdateViewModel.currentPassword.collectAsState()
    val newPassword by userUpdateViewModel.newPassword.collectAsState()
    val confirmPassword by userUpdateViewModel.confirmPassword.collectAsState()
    val message by userUpdateViewModel.message.collectAsState()
    val isUpdating by userUpdateViewModel.isUpdating.collectAsState()

    // Validation error states
    val nameError by userUpdateViewModel.nameError.collectAsState()
    val emailError by userUpdateViewModel.emailError.collectAsState()
    val passwordError by userUpdateViewModel.passwordError.collectAsState()

    // Snackbar handler
    LaunchedEffect(message) {
        message?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            userUpdateViewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = stringResource(R.string.edit_profile_title),
                onLogoutClick = onBackClick
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --------------------------------------------------------------
            // User Information Section
            // --------------------------------------------------------------
            UserInfoSection(
                name = name,
                onNameChange = userUpdateViewModel::updateName,
                lastname = lastname,
                onLastnameChange = userUpdateViewModel::updateLastname,
                username = username,
                onUsernameChange = userUpdateViewModel::updateUsername,
                nameError = nameError
            )

            // --------------------------------------------------------------
            // Email Section
            // --------------------------------------------------------------
            EmailFieldSection(
                email = email,
                onEmailChange = userUpdateViewModel::updateEmail,
                emailError = emailError
            )

            // --------------------------------------------------------------
            // Location Section (not validated)
            // --------------------------------------------------------------
            LocationSection(
                country = country,
                city = city,
                citiesMap = userUpdateViewModel.americanCitiesMap,
                onCountryChange = userUpdateViewModel::updateCountry,
                onCityChange = userUpdateViewModel::updateCity
            )

            // --------------------------------------------------------------
            // Password Section
            // --------------------------------------------------------------
            PasswordSection(
                currentPassword = currentPassword,
                newPassword = newPassword,
                confirmPassword = confirmPassword,
                onCurrentPasswordChange = userUpdateViewModel::updateCurrentPassword,
                onNewPasswordChange = userUpdateViewModel::updateNewPassword,
                onConfirmPasswordChange = userUpdateViewModel::updateConfirmPassword,
                passwordError = passwordError
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --------------------------------------------------------------
            // Action Buttons
            // --------------------------------------------------------------
            EditProfileActions(
                isUpdating = isUpdating,
                onClear = {
                    userUpdateViewModel.apply {
                        updateName("")
                        updateLastname("")
                        updateUsername("")
                        updateEmail("")
                        updateCountry("Colombia")
                        updateCity("Armenia")
                        updateCurrentPassword("")
                        updateNewPassword("")
                        updateConfirmPassword("")
                    }
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            context.getString(R.string.snackbar_fields_cleared)
                        )
                    }
                },
                onSave = {
                    val currentUser = userSessionViewModel.currentUser.value
                    if (currentUser != null) {
                        // Delegate update to ViewModel
                        userUpdateViewModel.updateUser(currentUser) { updated ->
                            userSessionViewModel.setUser(updated)
                        }
                    }
                }
            )
        }
    }
}

/**
 * User information section for editing name, lastname, and username.
 */
@Composable
fun UserInfoSection(
    name: String,
    onNameChange: (String) -> Unit,
    lastname: String,
    onLastnameChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    nameError: String?
) {
    AuthTextField(
        value = name,
        onValueChange = onNameChange,
        label = stringResource(R.string.register_name),
        placeholder = stringResource(R.string.edit_profile_name_placeholder),
        leadingIcon = Icons.Default.Person,
        fieldType = AuthFieldType.Text
    )
    if (nameError != null) FieldErrorText(nameError)

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
}

/**
 * Email field section with basic validation feedback.
 */
@Composable
fun EmailFieldSection(
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: String?
) {
    AuthTextField(
        value = email,
        onValueChange = onEmailChange,
        label = stringResource(R.string.register_email),
        placeholder = stringResource(R.string.register_email),
        leadingIcon = Icons.Default.Email,
        fieldType = AuthFieldType.Text
    )
    if (emailError != null) FieldErrorText(emailError)
}

/**
 * Location section for selecting country and city.
 * These dropdowns are optional and not validated.
 */
@Composable
fun LocationSection(
    country: String,
    city: String,
    citiesMap: Map<String, List<String>>,
    onCountryChange: (String) -> Unit,
    onCityChange: (String) -> Unit
) {
    DropdownField(
        label = stringResource(R.string.register_country),
        options = citiesMap.keys.toList(),
        selectedOption = country,
        onOptionSelected = onCountryChange
    )

    DropdownField(
        label = stringResource(R.string.register_city),
        options = citiesMap[country] ?: emptyList(),
        selectedOption = city,
        onOptionSelected = onCityChange
    )
}

/**
 * Password section that includes current, new, and confirm fields.
 * Current password is required for any password change.
 */
@Composable
fun PasswordSection(
    currentPassword: String,
    newPassword: String,
    confirmPassword: String,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    passwordError: String?
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

    if (passwordError != null) FieldErrorText(passwordError)
}

/**
 * Displays a red error message below a field when validation fails.
 */
@Composable
fun FieldErrorText(error: String) {
    Text(
        text = error,
        color = Color.Red,
        fontSize = 13.sp,
        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
    )
}

/**
 * Row containing Clear and Save buttons for the profile form.
 */
@Composable
fun EditProfileActions(
    isUpdating: Boolean,
    onClear: () -> Unit,
    onSave: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = onClear,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.action_delete),
                tint = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = onSave,
            modifier = Modifier.weight(3f),
            colors = ButtonDefaults.buttonColors(
                containerColor = OrangePrimary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = if (isUpdating) "Saving..." else stringResource(R.string.edit_profile_button_save),
                fontSize = 13.sp,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
