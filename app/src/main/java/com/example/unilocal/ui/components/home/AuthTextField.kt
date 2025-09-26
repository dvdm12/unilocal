package com.example.unilocal.ui.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A reusable text field for authentication forms.
 * Supports password visibility toggle and input validation.
 */
@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    value: String, // Current input value
    onValueChange: (String) -> Unit, // Callback for input changes
    label: String, // Label shown above the text field
    placeholder: String, // Placeholder text shown inside the field
    leadingIcon: ImageVector, // Icon shown on the left side
    isPassword: Boolean = false, // Whether the field is for a password
    fieldType: String = "text", // Type of field: "text", "email", or "password"
    emailErrorText: String = "", // Error message shown for invalid email
    passwordErrorText: String = "" // Error message shown for invalid password
) {
    var isPasswordVisible by remember { mutableStateOf(false) } // Tracks whether password is visible
    var errorMessage by remember(value) { mutableStateOf("") } // Holds validation error messages

    // Validation logic based on fieldType
    errorMessage = when (fieldType) {
        "email" -> if (value.isNotEmpty() && !value.matches(
                Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
        ) emailErrorText else ""

        "password" -> if (value.isNotEmpty() && value.length < 5) passwordErrorText else ""

        else -> ""
    }

    // Layout column containing the text field and optional error message
    Column(modifier = modifier.fillMaxWidth()) {
        // Main input field
        AuthOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            isPassword = isPassword,
            isPasswordVisible = isPasswordVisible,
            onVisibilityToggle = { isPasswordVisible = !isPasswordVisible },
            isError = errorMessage.isNotEmpty()
        )

        // Error message shown below the field
        AuthErrorMessage(errorMessage)
    }
}

/**
 * A modular composable for rendering an OutlinedTextField with optional password visibility toggle.
 */
@Composable
private fun AuthOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    isPassword: Boolean,
    isPasswordVisible: Boolean,
    onVisibilityToggle: () -> Unit,
    isError: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = TextStyle(fontSize = 15.sp)) }, // 👈 font size reducido
        placeholder = { Text(placeholder, style = TextStyle(fontSize = 15.sp)) }, // 👈 font size reducido
        leadingIcon = {
            Icon(imageVector = leadingIcon, contentDescription = null)
        },
        trailingIcon = {
            if (isPassword) {
                val visibilityIcon =
                    if (isPasswordVisible) Icons.Default.Check else Icons.Default.Close
                val description =
                    if (isPasswordVisible) "Hide password" else "Show password"
                Icon(
                    imageVector = visibilityIcon,
                    contentDescription = description,
                    modifier = Modifier
                        .clickable { onVisibilityToggle() }
                        .padding(4.dp)
                )
            }
        },
        visualTransformation = if (isPassword && !isPasswordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        isError = isError,
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(fontSize = 14.sp)
    )
}


/**
 * Displays the validation error message under the input field.
 */
@Composable
private fun AuthErrorMessage(errorMessage: String) {
    if (errorMessage.isNotEmpty()) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
        )
    }
}

@Preview(showBackground = true, name = "AuthTextField Preview")
@Composable
fun AuthTextFieldPreview() {
    MaterialTheme {
        AuthTextField(
            value = "",
            onValueChange = {},
            label = "Correo electrónico",
            placeholder = "Ingresa tu correo",
            leadingIcon = Icons.Default.FavoriteBorder,
            isPassword = false,
            fieldType = "email",
            emailErrorText = "Correo inválido",
            passwordErrorText = "Contraseña muy corta"
        )
    }
}

