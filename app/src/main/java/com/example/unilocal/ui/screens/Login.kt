package com.example.unilocal.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unilocal.R
import com.example.unilocal.ui.components.home.AuthTextField
import com.example.unilocal.ui.components.home.AuthFieldType
import com.example.unilocal.ui.components.home.SocialButton
import com.example.unilocal.ui.components.home.UniPrimaryButton
import com.example.unilocal.viewmodel.login.LoginResult
import com.example.unilocal.viewmodel.login.LoginViewModel
import com.example.unilocal.viewmodel.data.session.UserSessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    viewModel: LoginViewModel = viewModel(),
    userSessionViewModel: UserSessionViewModel,
    onBackClick: () -> Unit = {},
    onLoginSuccessUser: () -> Unit = {},
    onLoginSuccessModerator: () -> Unit = {},
    onGoogleLogin: () -> Unit = {},
    onFacebookLogin: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onForgotPassword: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // ⚡ Reacción ante el resultado del login
    LaunchedEffect(uiState.loginResult) {
        when (uiState.loginResult) {
            LoginResult.SUCCESS_USER, LoginResult.SUCCESS_MODERATOR -> {
                uiState.currentUser?.let { user ->
                    // Guardar en el SessionManager (a través del ViewModel global)
                    userSessionViewModel.setUser(user)
                }

                // Navegar dependiendo del rol
                when (uiState.loginResult) {
                    LoginResult.SUCCESS_USER -> onLoginSuccessUser()
                    LoginResult.SUCCESS_MODERATOR -> onLoginSuccessModerator()
                    else -> {}
                }

                viewModel.clearLoginResult()
            }

            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.login_title),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // 👋 Texto de bienvenida
            Text(
                text = stringResource(R.string.login_welcome),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // 📧 Campo email
            AuthTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = stringResource(R.string.login_email_label),
                placeholder = stringResource(R.string.login_email_placeholder),
                leadingIcon = Icons.Default.Email,
                fieldType = AuthFieldType.Email,
                errorMessage = uiState.emailError
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔒 Campo contraseña
            AuthTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                label = stringResource(R.string.login_password_label),
                placeholder = stringResource(R.string.login_password_placeholder),
                leadingIcon = Icons.Default.Lock,
                fieldType = AuthFieldType.Password,
                errorMessage = uiState.passwordError
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 🔑 Recuperar contraseña
            Text(
                text = stringResource(R.string.login_forgot_password),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onForgotPassword() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🚀 Botón iniciar sesión
            UniPrimaryButton(
                text = stringResource(R.string.login_button),
                onClick = { viewModel.onLoginClick() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ───────────── Separador ─────────────
            OrContinueWithSeparator()

            Spacer(modifier = Modifier.height(24.dp))

            // 🔴 Botón Google
            SocialButton(
                icon = painterResource(id = R.drawable.gmail_svgrepo_com),
                text = stringResource(R.string.login_google),
                containerColor = Color(0xFFDB4437),
                contentColor = Color.White,
                onClick = onGoogleLogin
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🔵 Botón Facebook
            SocialButton(
                icon = painterResource(id = R.drawable.facebook_square_svgrepo_com),
                text = stringResource(R.string.login_facebook),
                containerColor = Color(0xFF1877F2),
                contentColor = Color.White,
                onClick = onFacebookLogin
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 🆕 Crear cuenta
            RegisterButton(
                text = stringResource(R.string.login_register_link),
                onClick = onRegisterClick
            )
        }
    }

    // Diálogo de error para credenciales inválidas
    LoginErrorDialog(
        show = uiState.loginResult == LoginResult.INVALID_CREDENTIALS,
        onDismiss = { viewModel.clearLoginResult() }
    )
}

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

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "Login Screen")
@Composable
fun SimpleLoginPreview() {
    MaterialTheme {
        Surface {
            Login(
                userSessionViewModel = UserSessionViewModel(
                    context = LocalContext.current
                ),
                onBackClick = {},
                onLoginSuccessUser = {},
                onLoginSuccessModerator = {},
                onGoogleLogin = {},
                onFacebookLogin = {},
                onRegisterClick = {},
                onForgotPassword = {}
            )
        }
    }
}
