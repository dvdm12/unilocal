package com.example.unilocal.viewmodel

import androidx.lifecycle.ViewModel
import com.example.unilocal.model.Role
import com.example.unilocal.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    // Usuarios de prueba (simulación de base de datos)
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

    // Actualiza el email y valida
    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = validateEmail(email)
            )
        }
    }

    // Actualiza la contraseña y valida
    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = validatePassword(password)
            )
        }
    }

    // Intenta iniciar sesión
    fun onLoginClick() {
        val currentState = _uiState.value
        val emailError = validateEmail(currentState.email)
        val passwordError = validatePassword(currentState.password)

        // Validación fallida
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

        // Buscar usuario que coincida
        val matchedUser = users.find {
            it.email == currentState.email &&
                    it.password == currentState.password &&
                    it.isActive
        }

        // Actualizar estado según el rol
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

    // Limpia el resultado del login
    fun clearLoginResult() {
        _uiState.update {
            it.copy(loginResult = null)
        }
    }

    // Validación básica de email
    private fun validateEmail(email: String): String? {
        return if (!email.contains("@") || !email.contains(".com")) {
            "Correo inválido"
        } else null
    }

    // Validación básica de contraseña
    private fun validatePassword(password: String): String? {
        return if (password.length < 6) {
            "Contraseña demasiado corta"
        } else null
    }
}
