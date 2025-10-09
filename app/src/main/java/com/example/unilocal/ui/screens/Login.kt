package com.example.unilocal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import com.example.unilocal.ui.components.home.SocialButton
import com.example.unilocal.ui.components.home.UniPrimaryButton

/**
 * Validación simple: email y password no vacíos
 */
fun isLoginValid(email: String, password: String): Boolean {
    return email.isNotBlank() && password.isNotBlank()
}

/**
 * Botón alternativo para registro (estética diferente al de login principal).
 */
@Composable
fun RegisterButton(
    text: String = stringResource(R.string.login_register_link),
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

/**
 * Composable for the error dialog shown when login fields are invalid.
 */
@Composable
fun LoginErrorDialog(show: Boolean, onDismiss: () -> Unit) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.login_error_accept))
                }
            },
            title = { Text(stringResource(R.string.login_error_title)) },
            text = { Text(stringResource(R.string.login_error_message)) }
        )
    }
}

/**
 * Composable for the separator row with 'or continue with'.
 */
@Composable
fun OrContinueWithSeparator() {
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
            text = stringResource(R.string.login_or_continue),
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = Color.LightGray,
            thickness = 1.dp
        )
    }
}

/**
 * Pantalla de Login con TopBar, validación y alerta en caso de campos vacíos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    onBackClick: () -> Unit = {},
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
    var showErrorDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.login_title),
                        modifier = Modifier.fillMaxWidth(), // Center horizontally
                        textAlign = TextAlign.Left, // Center text
                        fontWeight = FontWeight.Bold, // Make font bold
                        fontSize = 22.sp // Larger font size
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_content_desc)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Mensaje de bienvenida
            Text(
                text = stringResource(R.string.login_welcome),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Campo Email
            AuthTextField(
                value = email,
                onValueChange = { email = it },
                label = stringResource(R.string.login_email_label),
                placeholder = stringResource(R.string.login_email_placeholder),
                leadingIcon = Icons.Default.Email,
                fieldType = "email",
                emailErrorText = stringResource(R.string.login_email_error)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Password
            AuthTextField(
                value = password,
                onValueChange = { password = it },
                label = stringResource(R.string.login_password_label),
                placeholder = stringResource(R.string.login_password_placeholder),
                leadingIcon = Icons.Default.Lock,
                isPassword = true,
                fieldType = "password",
                passwordErrorText = stringResource(R.string.login_password_error)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Olvidé mi contraseña
            Text(
                text = stringResource(R.string.login_forgot_password),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onForgotPassword() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Iniciar sesión
            UniPrimaryButton(
                text = stringResource(R.string.login_button),
                onClick = {
                    if (isLoginValid(email, password)) {
                        onLoginClick()
                    } else {
                        showErrorDialog = true
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Separador "o continuar con"
            OrContinueWithSeparator()

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Google
            SocialButton(
                icon = painterResource(id = R.drawable.gmail_svgrepo_com),
                text = stringResource(R.string.login_google),
                containerColor = Color(0xFFDB4437), // Rojo oficial de Google
                contentColor = Color.White,
                onClick = onGoogleLogin
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botón Facebook
            SocialButton(
                icon = painterResource(id = R.drawable.facebook_square_svgrepo_com),
                text = stringResource(R.string.login_facebook),
                containerColor = Color(0xFF1877F2), // Azul oficial de Facebook
                contentColor = Color.White,
                onClick = onFacebookLogin
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Crear cuenta
            RegisterButton(
                text = stringResource(R.string.login_register_link),
                onClick = onRegisterClick
            )
        }
    }

    // Alerta de error
    LoginErrorDialog(show = showErrorDialog, onDismiss = { showErrorDialog = false })
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Login(
        emailInitial = "correo",
        passwordInitial = "123"
    )
}
