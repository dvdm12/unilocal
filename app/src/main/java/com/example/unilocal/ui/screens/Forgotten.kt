package com.example.unilocal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.unilocal.ui.components.home.*
import kotlinx.coroutines.launch

/**
 * Main screen for password recovery.
 * Allows the user to enter their email and request a recovery code.
 * Includes social login options and navigation to return to the login screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onGoogleClick: () -> Unit = {},
    onFacebookClick: () -> Unit = {},
    onBackToLogin: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val emptyEmailMsg = stringResource(R.string.forgot_password_error_empty_email)
    val invalidEmailMsg = stringResource(R.string.forgot_password_error_invalid_email)
    val codeSentMsg = stringResource(R.string.forgot_password_code_sent, email)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.forgot_password_back_to_login))
                },
                navigationIcon = {
                    IconButton(onClick = onBackToLogin) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_content_desc)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                ForgotPasswordHeader()
                Spacer(modifier = Modifier.height(24.dp))
                ForgotPasswordForm(
                    email = email,
                    onValueChange = { email = it },
                    onSendCode = {
                        coroutineScope.launch {
                            when {
                                email.isBlank() -> {
                                    snackbarHostState.showSnackbar(
                                        message = emptyEmailMsg
                                    )
                                }
                                !email.contains("@") -> {
                                    snackbarHostState.showSnackbar(
                                        message = invalidEmailMsg
                                    )
                                }
                                else -> {
                                    snackbarHostState.showSnackbar(
                                        message = codeSentMsg
                                    )
                                }
                            }

                        }
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
                ForgotPasswordSocialButtons(
                    onGoogleClick = onGoogleClick,
                    onFacebookClick = onFacebookClick
                )
            }

            BackToLoginText(onClick = onBackToLogin)
        }
    }
}

/**
 * Displays the header section for the forgot password screen,
 * including title and subtitle instructions.
 */
@Composable
fun ForgotPasswordHeader() {
    Text(
        text = stringResource(R.string.forgot_password_title),
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = stringResource(R.string.forgot_password_subtitle),
        style = MaterialTheme.typography.bodyMedium
    )
}

/**
 * Contains the email input field and the primary action button
 * to send the recovery code.
 *
 * @param email Current email input value.
 * @param onValueChange Callback triggered when the input changes.
 * @param onSendCode Callback triggered when the user taps "Send code".
 */
@Composable
fun ForgotPasswordForm(
    email: String,
    onValueChange: (String) -> Unit,
    onSendCode: () -> Unit
) {
    AuthTextField(
        value = email,
        onValueChange = onValueChange,
        label = stringResource(R.string.forgot_password_email_label),
        placeholder = stringResource(R.string.forgot_password_email_placeholder),
        leadingIcon = Icons.Default.Email,
        isPassword = false,
        fieldType = "email",
        emailErrorText = stringResource(R.string.forgot_password_email_error)
    )
    Spacer(modifier = Modifier.height(20.dp))
    UniPrimaryButton(
        text = stringResource(R.string.forgot_password_button),
        onClick = onSendCode
    )
}

/**
 * Shows buttons for social login options (Google and Facebook),
 * with representative colors and branding.
 *
 * @param onGoogleClick Callback for Google button click.
 * @param onFacebookClick Callback for Facebook button click.
 */
@Composable
fun ForgotPasswordSocialButtons(
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SocialButton(
            icon = painterResource(id = R.drawable.ic_launcher_foreground),
            text = stringResource(R.string.forgot_password_google),
            containerColor = Color.Red, // Consider replacing with Color.White and using correct icon
            contentColor = Color.White,
            onClick = onGoogleClick
        )
        SocialButton(
            icon = painterResource(id = R.drawable.ic_launcher_foreground),
            text = stringResource(R.string.forgot_password_facebook),
            containerColor = Color(0xFF1877F2), // Facebook Blue
            contentColor = Color.White,
            onClick = onFacebookClick
        )
    }
}

/**
 * Displays a clickable text that navigates back to the login screen.
 *
 * @param onClick Callback when the user taps the text.
 */
@Composable
fun BackToLoginText(onClick: () -> Unit) {
    Text(
        text = stringResource(R.string.forgot_password_back_to_login),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.primary,
            fontSize = 13.sp
        )
    )
}

/**
 * Preview function to visualize the ForgotPasswordScreen
 * during design time in Android Studio.
 */
@Preview(showBackground = true, name = "ForgotPasswordScreen")
@Composable
fun ForgotPasswordScreenPreview() {
    MaterialTheme {
        Surface {
            ForgotPasswordScreen()
        }
    }
}
