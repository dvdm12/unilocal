package com.example.unilocal.ui.components // Package declaration

import androidx.compose.foundation.layout.* // Import for layout components
import androidx.compose.material3.* // Import for Material3 components
import androidx.compose.runtime.Composable // Import for Composable annotation
import androidx.compose.runtime.getValue // Import for property delegate 'getValue'
import androidx.compose.runtime.mutableStateOf // Import for mutable state
import androidx.compose.runtime.remember // Import for remembering state
import androidx.compose.runtime.setValue // Import for property delegate 'setValue'
import androidx.compose.ui.Modifier // Import for Modifier
import androidx.compose.ui.graphics.vector.ImageVector // Import for vector icons
import androidx.compose.ui.text.input.PasswordVisualTransformation // Import for password transformation
import androidx.compose.ui.text.input.VisualTransformation // Import for visual transformation
import androidx.compose.ui.unit.dp // Import for dp units

/**
 * AuthTextField is a reusable composable for authentication forms.
 * It supports validation for email and password fields, showing error messages from string resources.
 */
@Composable // Marks this function as a composable
fun AuthTextField( // Function declaration for AuthTextField
    value: String, // Current field value
    onValueChange: (String) -> Unit, // Callback for value change
    label: String, // Field label
    placeholder: String, // Placeholder text
    leadingIcon: ImageVector, // Icon at the start of the field
    isPassword: Boolean = false, // If true, hides input for password
    modifier: Modifier = Modifier, // Modifier for layout/styling
    fieldType: String = "text", // Type of field: "email", "password", or "text"
    emailErrorText: String = "", // Error message for invalid email
    passwordErrorText: String = "" // Error message for invalid password
) {
    var errorMessage by remember(value) { mutableStateOf("") } // Holds the error message and updates when value changes
    // Validate input based on field type
    errorMessage = when (fieldType) { // Select error message based on field type
        "email" -> if (value.isNotEmpty() && !value.matches(Regex("^[A-Za-z0-9+_.-]+@gmail\\.com$"))) emailErrorText else "" // Email validation
        "password" -> if (value.isNotEmpty() && value.length < 5) passwordErrorText else "" // Password validation
        else -> "" // No validation for other types
    }
    Column(modifier = modifier.fillMaxWidth()) { // Layout column, fills max width
        OutlinedTextField( // Material3 outlined text field
            value = value, // Field value
            onValueChange = onValueChange, // Value change callback
            label = { Text(label) }, // Field label
            placeholder = { Text(placeholder) }, // Field placeholder
            leadingIcon = { // Leading icon composable
                Icon(imageVector = leadingIcon, contentDescription = null) // Show leading icon
            },
            modifier = Modifier.fillMaxWidth(), // Fill max width
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None, // Hide password if needed
            singleLine = true, // Single line input
            isError = errorMessage.isNotEmpty() // Highlight field if error
        )
        if (errorMessage.isNotEmpty()) { // If there is an error message
            Text( // Show error message below field
                text = errorMessage, // Error message text
                color = MaterialTheme.colorScheme.error, // Error color
                style = MaterialTheme.typography.labelSmall, // Error text style
                modifier = Modifier.padding(start = 16.dp, top = 2.dp) // Padding for error text
            )
        }
    }
}
