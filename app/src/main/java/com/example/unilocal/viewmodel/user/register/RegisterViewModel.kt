package com.example.unilocal.viewmodel.user.register

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.unilocal.R
import com.example.unilocal.viewmodel.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel responsable del registro de usuarios.
 * Realiza validaciones en tiempo real y actualiza el estado de la UI.
 */
@SuppressLint("StaticFieldLeak")
class RegisterViewModel(
    application: Application,
    private val userRepository: UserRepository = UserRepository
) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    // --- CAMPOS: CAMBIOS Y VALIDACIONES ---
    fun onNameChange(value: String) {
        _uiState.update {
            it.copy(
                name = value,
                nameError = if (value.length < 3)
                    context.getString(R.string.snackbar_error_empty_fields)
                else null
            ).evaluateForm()
        }
    }

    fun onLastNameChange(value: String) {
        _uiState.update {
            it.copy(
                lastName = value,
                lastNameError = if (value.length < 3)
                    context.getString(R.string.snackbar_error_empty_fields)
                else null
            ).evaluateForm()
        }
    }

    fun onEmailChange(value: String) {
        _uiState.update {
            it.copy(
                email = value,
                emailError = if (!value.contains("@") || !value.contains(".")) {
                    context.getString(R.string.login_email_error)
                } else null
            ).evaluateForm()
        }
    }

    fun onPhoneChange(value: String) {
        _uiState.update {
            it.copy(
                phone = value,
                phoneError = if (value.length < 7)
                    context.getString(R.string.snackbar_error_empty_fields)
                else null
            ).evaluateForm()
        }
    }

    fun onCountryChange(value: String) {
        _uiState.update {
            it.copy(country = value).evaluateForm()
        }
    }

    fun onCityChange(value: String) {
        _uiState.update {
            it.copy(city = value).evaluateForm()
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                passwordError = if (value.length < 5)
                    context.getString(R.string.login_password_error)
                else null
            ).evaluateForm()
        }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                confirmPassword = value,
                confirmPasswordError = if (value != it.password)
                    context.getString(R.string.register_confirm_password)
                else null
            ).evaluateForm()
        }
    }

    // --- REGISTRO FINAL ---
    fun onRegisterClick() {
        val state = _uiState.value

        if (!state.isValid) {
            _uiState.update {
                it.copy(
                    errorMessage = context.getString(R.string.login_error_message),
                    isSuccess = false
                )
            }
            return
        }

        val success = userRepository.registerUser(
            name = "${state.name} ${state.lastName}",
            username = state.email.substringBefore("@"),
            password = state.password,
            email = state.email,
            country = state.country,
            city = state.city
        )

        if (success) {
            _uiState.update {
                it.copy(
                    isSuccess = true,
                    registeredUser = userRepository.findUser(state.email, state.password),
                    errorMessage = null
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isSuccess = false,
                    errorMessage = context.getString(R.string.login_error_message)
                )
            }
        }
    }

    // --- EVALUAR FORMULARIO COMPLETO ---
    private fun RegisterUiState.evaluateForm(): RegisterUiState {
        val valid = nameError == null &&
                lastNameError == null &&
                emailError == null &&
                passwordError == null &&
                confirmPasswordError == null &&
                name.isNotBlank() &&
                lastName.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                country.isNotBlank() &&
                city.isNotBlank()

        return copy(isValid = valid)
    }
}
