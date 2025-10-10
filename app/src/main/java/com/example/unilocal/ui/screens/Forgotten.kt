package com.example.unilocal.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.unilocal.viewmodel.login.LoginViewModelBase
import com.example.unilocal.viewmodel.login.fake.FakeLoginViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: LoginViewModelBase,
    onGoogleClick: () -> Unit = {},
    onFacebookClick: () -> Unit = {},
    onBackToLogin: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val emptyEmailMsg = stringResource(R.string.forgot_password_error_empty_email)
    val invalidEmailMsg = stringResource(R.string.forgot_password_error_invalid_email)
    val codeSentMsg = stringResource(R.string.forgot_password_code_sent, uiState.email)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.forgot_password_back_to_login))
                },
                navigationIcon = {
                    IconButton(onClick = onBackToLogin) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                    email = uiState.email,
                    onValueChange = viewModel::onEmailChange,
                    onSendCode = {
                        coroutineScope.launch {
                            when {
                                uiState.email.isBlank() -> {
                                    snackbarHostState.showSnackbar(emptyEmailMsg)
                                }
                                uiState.emailError != null -> {
                                    snackbarHostState.showSnackbar(invalidEmailMsg)
                                }
                                else -> {
                                    snackbarHostState.showSnackbar(codeSentMsg)
                                }
                            }
                        }
                    },
                    emailError = uiState.emailError
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

@Composable
fun ForgotPasswordForm(
    email: String,
    onValueChange: (String) -> Unit,
    onSendCode: () -> Unit,
    emailError: String?
) {
    AuthTextField(
        value = email,
        onValueChange = onValueChange,
        label = stringResource(R.string.forgot_password_email_label),
        placeholder = stringResource(R.string.forgot_password_email_placeholder),
        leadingIcon = Icons.Default.Email,
        fieldType = AuthFieldType.Email,
        errorMessage = emailError
    )
    Spacer(modifier = Modifier.height(20.dp))
    UniPrimaryButton(
        text = stringResource(R.string.forgot_password_button),
        onClick = onSendCode
    )
}

@Composable
fun ForgotPasswordSocialButtons(
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SocialButton(
            icon = painterResource(id = R.drawable.gmail_svgrepo_com),
            text = stringResource(R.string.forgot_password_google),
            containerColor = Color.Red,
            contentColor = Color.White,
            onClick = onGoogleClick
        )
        SocialButton(
            icon = painterResource(id = R.drawable.facebook_square_svgrepo_com),
            text = stringResource(R.string.forgot_password_facebook),
            containerColor = Color(0xFF1877F2),
            contentColor = Color.White,
            onClick = onFacebookClick
        )
    }
}

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

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    MaterialTheme {
        Surface {
            ForgotPasswordScreen(FakeLoginViewModel())
        }
    }
}
