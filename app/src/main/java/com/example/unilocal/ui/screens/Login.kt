package com.example.unilocal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unilocal.R
import com.example.unilocal.ui.components.home.AuthTextField
import com.example.unilocal.ui.components.home.SocialButton
import com.example.unilocal.ui.components.home.UniPrimaryButton

/**
 * Login composable displays the login screen UI, including email and password fields,
 * error validation, social login buttons, and navigation links. It uses AuthTextField
 * for input validation and supports previewing with initial values.
 *
 * @param onLoginClick Callback for login button click.
 * @param onGoogleLogin Callback for Google login button click.
 * @param onFacebookLogin Callback for Facebook login button click.
 * @param onRegisterClick Callback for register link click.
 * @param onForgotPassword Callback for forgot password link click.
 * @param emailInitial Initial value for the email field (useful for preview/testing).
 * @param passwordInitial Initial value for the password field (useful for preview/testing).
 */
@Composable
fun Login(
    onLoginSuccess: (username: String, isModerator: Boolean) -> Unit = { _, _ -> },
    onLoginClick: () -> Unit = {},
    onGoogleLogin: () -> Unit = {},
    onFacebookLogin: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    emailInitial: String = "",
    passwordInitial: String = ""
) {
    var email by remember { mutableStateOf(emailInitial) }
    var password by remember { mutableStateOf(passwordInitial) }

    val emailErrorText = stringResource(R.string.login_email_error)
    val passwordErrorText = stringResource(R.string.login_password_error)

    Column( // Main layout column for the login screen
        modifier = Modifier
            .fillMaxSize() // Fill the entire available space
            .padding(horizontal = 24.dp), // Add horizontal padding
        verticalArrangement = Arrangement.Center, // Center content vertically
        horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
    ) {
        AuthTextField( // Email input field with validation
            value = email,
            onValueChange = { email = it },
            label = stringResource(R.string.login_email_label), // Get label from strings.xml
            placeholder = stringResource(R.string.login_email_placeholder), // Get placeholder from strings.xml
            leadingIcon = Icons.Default.Email, // Show email icon
            fieldType = "email", // Specify field type for validation
            emailErrorText = emailErrorText // Error message for invalid email
        )

        Spacer(modifier = Modifier.height(16.dp)) // Space between fields

        AuthTextField( // Password input field with validation
            value = password,
            onValueChange = { password = it },
            label = stringResource(R.string.login_password_label), // Get label from strings.xml
            placeholder = stringResource(R.string.login_password_placeholder), // Get placeholder from strings.xml
            leadingIcon = Icons.Default.Lock, // Show lock icon
            isPassword = true, // Hide password input
            fieldType = "password", // Specify field type for validation
            passwordErrorText = passwordErrorText // Error message for invalid password
        )

        Spacer(modifier = Modifier.height(8.dp)) // Space before forgot password link

        Text( // Forgot password link
            text = stringResource(R.string.login_forgot_password), // Get text from strings.xml
            color = Color.Gray, // Set text color
            fontSize = 13.sp, // Set text size
            modifier = Modifier
                .align(Alignment.End) // Align to the end
                .clickable { onForgotPassword() } // Handle click event
        )

        Spacer(modifier = Modifier.height(24.dp)) // Space before login button

        UniPrimaryButton( // Main login button
            text = stringResource(R.string.login_button), // Get button text from strings.xml
            onClick =
                {
                    val isModerator = email == "moderador@gmail.com" // ejemplo simple
                    onLoginSuccess(email, isModerator)
                }

        )

        Spacer(modifier = Modifier.height(16.dp)) // Space before social buttons

        SocialButton( // Google login button
            icon = painterResource(id = R.drawable.ic_launcher_foreground), // Show Google icon
            text = stringResource(R.string.login_google), // Get button text from strings.xml
            containerColor = Color(0xFFF5F5F5), // Set button background color
            contentColor = Color.Black, // Set button text color
            onClick = onGoogleLogin // Handle Google login click
        )

        Spacer(modifier = Modifier.height(12.dp)) // Space before Facebook button

        SocialButton( // Facebook login button
            icon = painterResource(id = R.drawable.ic_launcher_foreground), // Show Facebook icon
            text = stringResource(R.string.login_facebook), // Get button text from strings.xml
            containerColor = Color(0xFF1877F2), // Set button background color
            contentColor = Color.White, // Set button text color
            onClick = onFacebookLogin // Handle Facebook login click
        )

        Spacer(modifier = Modifier.height(32.dp)) // Space before register link

        Text( // Register link
            text = stringResource(R.string.login_register_link), // Get text from strings.xml
            color = Color.Black, // Set text color
            fontWeight = FontWeight.Medium, // Set font weight
            modifier = Modifier.clickable { onRegisterClick() } // Handle click event
        )
    }
}

/**
 * LoginPreview composable provides a preview of the Login screen in Android Studio,
 * using sample initial values to demonstrate error messages and layout.
 */
@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Login(emailInitial = "correo", passwordInitial = "123")
}
