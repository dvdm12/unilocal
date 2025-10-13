package com.example.unilocal.viewmodel.login

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.unilocal.R
import com.example.unilocal.model.Role
import com.example.unilocal.viewmodel.data.session.SessionManager
import com.example.unilocal.viewmodel.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel encargado de manejar la lógica de inicio de sesión.
 * Se apoya en el UserRepository para la autenticación y en SessionManager
 * para persistir la sesión del usuario.
 */
class LoginViewModel(
    application: Application,
    private val userRepository: UserRepository = UserRepository
) : AndroidViewModel(application), LoginViewModelBase {

    private val _uiState = MutableStateFlow(LoginUiState())
    override val uiState: StateFlow<LoginUiState> = _uiState

    private val sessionManager = SessionManager(application.applicationContext)

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    // --- ACTUALIZACIÓN DE CAMPOS ---

    override fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = validateEmail(email)) }
    }

    override fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = validatePassword(password)) }
    }

    // --- ACCIÓN DE LOGIN ---
    override fun onLoginClick() {
        val state = _uiState.value
        val emailError = validateEmail(state.email)
        val passwordError = validatePassword(state.password)

        // Si hay errores, actualiza el estado
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

        // Buscar usuario en el repositorio
        val matchedUser = userRepository.findUser(state.email, state.password)

        if (matchedUser != null) {
            sessionManager.saveUser(matchedUser)
        }

        // Determinar el resultado
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

    // --- LIMPIAR RESULTADO ---
    override fun clearLoginResult() {
        _uiState.update { it.copy(loginResult = null) }
    }

    // --- VALIDADORES ---
    private fun validateEmail(email: String): String? {
        return if (!email.contains("@") || !email.contains(".com")) {
            context.getString(R.string.login_email_error)
        } else null
    }

    private fun validatePassword(password: String): String? {
        return if (password.length < 5) {
            context.getString(R.string.login_password_error)
        } else null
    }
}
