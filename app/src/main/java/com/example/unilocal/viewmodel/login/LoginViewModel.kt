package com.example.unilocal.viewmodel.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.unilocal.model.Role
import com.example.unilocal.model.User
import com.example.unilocal.viewmodel.data.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(application: Application) : AndroidViewModel(application), LoginViewModelBase {

    private val _uiState = MutableStateFlow(LoginUiState())
    override val uiState: StateFlow<LoginUiState> = _uiState

    private val sessionManager = SessionManager(application.applicationContext)

    private val users = listOf(
        User(
            id = "1",
            name = "Juan Pérez",
            username = "juanp",
            password = "123456",
            email = "juan@correo.com",
            country = "Colombia",
            city = "Bogotá",
            isActive = true,
            role = Role.USER
        ),
        User(
            id = "2",
            name = "Laura Admin",
            username = "lauraadmin",
            password = "adminpass",
            email = "laura@admin.com",
            country = "Colombia",
            city = "Medellín",
            isActive = true,
            role = Role.MODERATOR
        )
    )

    override fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = validateEmail(email)
            )
        }
    }

    override fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = validatePassword(password)
            )
        }
    }

    override fun onLoginClick() {
        val currentState = _uiState.value
        val emailError = validateEmail(currentState.email)
        val passwordError = validatePassword(currentState.password)

        if (emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError,
                    loginResult = LoginResult.INVALID_INPUT
                )
            }
            return
        }

        val matchedUser = users.find {
            it.email == currentState.email &&
                    it.password == currentState.password &&
                    it.isActive
        }

        // Guardar sesión si el login fue exitoso
        matchedUser?.let {
            sessionManager.saveUser(it)
        }

        _uiState.update {
            it.copy(
                currentUser = matchedUser,
                loginResult = when (matchedUser?.role) {
                    Role.USER -> LoginResult.SUCCESS_USER
                    Role.MODERATOR -> LoginResult.SUCCESS_MODERATOR
                    else -> LoginResult.INVALID_CREDENTIALS
                }
            )
        }
    }

    override fun clearLoginResult() {
        _uiState.update {
            it.copy(loginResult = null)
        }
    }

    private fun validateEmail(email: String): String? {
        return if (!email.contains("@") || !email.contains(".com")) {
            "Correo inválido"
        } else null
    }

    private fun validatePassword(password: String): String? {
        return if (password.length < 6) {
            "Contraseña demasiado corta"
        } else null
    }
}
