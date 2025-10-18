package com.example.unilocal.viewmodel.user.update

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.unilocal.R
import com.example.unilocal.model.User
import com.example.unilocal.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing and validating the user profile update form.
 *
 * ✅ Features:
 * - Field-level validation with explicit feedback for the user.
 * - Ignores combo-box validation (country/city keep default values).
 * - Requires current password for secure password changes.
 * - Resets state after successful update.
 */
class UserUpdateViewModel(
    private val application: Application,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {

    // -------------------------------------------------------------------------
    // 🔹 Form fields
    // -------------------------------------------------------------------------

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _lastname = MutableStateFlow("")
    val lastname: StateFlow<String> = _lastname

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _country = MutableStateFlow("Colombia")
    val country: StateFlow<String> = _country

    private val _city = MutableStateFlow("Armenia")
    val city: StateFlow<String> = _city

    private val _currentPassword = MutableStateFlow("")
    val currentPassword: StateFlow<String> = _currentPassword

    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    // -------------------------------------------------------------------------
    // 🔹 Field errors for user feedback
    // -------------------------------------------------------------------------

    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError

    private val _phoneError = MutableStateFlow<String?>(null)
    val phoneError: StateFlow<String?> = _phoneError

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError

    // -------------------------------------------------------------------------
    // 🔹 UI state
    // -------------------------------------------------------------------------

    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // -------------------------------------------------------------------------
    // 🔹 Validation rules
    // -------------------------------------------------------------------------

    private fun isPhoneValid(phone: String): Boolean =
        phone.matches(Regex("^\\d{7,}$"))

    private fun isEmailValid(email: String): Boolean =
        email.contains("@") && email.contains(".com")

    private fun isPasswordValid(password: String): Boolean =
        password.isBlank() || password.length >= 5

    private fun isPasswordChangeValid(currentPass: String, newPass: String): Boolean =
        if (newPass.isNotEmpty()) currentPass.isNotEmpty() else true

    // -------------------------------------------------------------------------
    // 🔹 Validation logic with detailed feedback
    // -------------------------------------------------------------------------

    private fun validateForm(): Boolean {
        var valid = true

        // --- Name ---
        if (_name.value.isBlank()) {
            _nameError.value = application.getString(R.string.msg_validation_name)
            valid = false
        } else _nameError.value = null

        // --- Phone ---
        if (!isPhoneValid(_phone.value)) {
            _phoneError.value = application.getString(R.string.msg_error_phone_invalid)
            valid = false
        } else _phoneError.value = null

        // --- Email ---
        if (!isEmailValid(_email.value)) {
            _emailError.value = application.getString(R.string.login_email_error)
            valid = false
        } else _emailError.value = null

        // --- Passwords ---
        if (_newPassword.value.isNotBlank()) {
            when {
                !isPasswordValid(_newPassword.value) -> {
                    _passwordError.value = application.getString(R.string.login_password_error)
                    valid = false
                }
                _newPassword.value != _confirmPassword.value -> {
                    _passwordError.value = application.getString(R.string.edit_profile_confirm_password_placeholder)
                    valid = false
                }
                _currentPassword.value.isBlank() -> {
                    _passwordError.value = application.getString(R.string.msg_error_current_password_required)
                    valid = false
                }
                else -> _passwordError.value = null
            }
        } else _passwordError.value = null

        return valid
    }

    // -------------------------------------------------------------------------
    // 🔹 Field updates
    // -------------------------------------------------------------------------

    fun updateName(value: String) = _name.tryEmit(value)
    fun updateLastname(value: String) = _lastname.tryEmit(value)
    fun updateUsername(value: String) = _username.tryEmit(value)
    fun updatePhone(value: String) = _phone.tryEmit(value)
    fun updateEmail(value: String) = _email.tryEmit(value)
    fun updateCountry(value: String) = _country.tryEmit(value)
    fun updateCity(value: String) = _city.tryEmit(value)
    fun updateCurrentPassword(value: String) = _currentPassword.tryEmit(value)
    fun updateNewPassword(value: String) = _newPassword.tryEmit(value)
    fun updateConfirmPassword(value: String) = _confirmPassword.tryEmit(value)

    // -------------------------------------------------------------------------
    // 🔹 Country-city list (no validation required)
    // -------------------------------------------------------------------------

    val americanCitiesMap: Map<String, List<String>> = mapOf(
        "Colombia" to listOf(
            "Bogotá", "Medellín", "Cali", "Barranquilla", "Cartagena",
            "Cúcuta", "Bucaramanga", "Pereira", "Santa Marta", "Ibagué",
            "Manizales", "Villavicencio", "Neiva", "Pasto", "Montería",
            "Armenia", "Sincelejo", "Popayán", "Valledupar", "Tunja"
        ),
        "México" to listOf(
            "Ciudad de México", "Guadalajara", "Monterrey", "Puebla", "Tijuana",
            "León", "Juárez", "Zapopan", "Mérida", "San Luis Potosí",
            "Querétaro", "Toluca", "Cancún", "Chihuahua", "Culiacán",
            "Morelia", "Hermosillo", "Saltillo", "Veracruz", "Aguascalientes"
        ),
        "Argentina" to listOf(
            "Buenos Aires", "Córdoba", "Rosario", "Mendoza", "La Plata",
            "Mar del Plata", "Salta", "Santa Fe", "San Miguel de Tucumán", "Corrientes",
            "Bahía Blanca", "Paraná", "Neuquén", "Formosa", "Santiago del Estero",
            "Posadas", "San Salvador de Jujuy", "Resistencia", "Comodoro Rivadavia", "Río Cuarto"
        ),
        "Chile" to listOf(
            "Santiago", "Valparaíso", "Concepción", "La Serena", "Antofagasta",
            "Temuco", "Rancagua", "Iquique", "Puerto Montt", "Talca",
            "Arica", "Copiapó", "Chillán", "Osorno", "Punta Arenas",
            "Los Ángeles", "Curicó", "Coyhaique", "Valdivia", "Calama"
        ),
        "Perú" to listOf(
            "Lima", "Arequipa", "Trujillo", "Chiclayo", "Piura",
            "Cusco", "Iquitos", "Huancayo", "Tacna", "Puno",
            "Juliaca", "Chimbote", "Ica", "Huaraz", "Cajamarca",
            "Ayacucho", "Tumbes", "Tarapoto", "Moquegua", "Pucallpa"
        )
    )


    // -------------------------------------------------------------------------
    // 🔹 Core logic
    // -------------------------------------------------------------------------

    fun updateUser(user: User, onSuccess: (User) -> Unit = {}) {
        viewModelScope.launch {
            try {
                _isUpdating.value = true

                // Validate fields before update
                if (!validateForm()) {
                    _message.value = application.getString(R.string.msg_field_required)
                    return@launch
                }

                // Simulate persistence
                userRepository.updateUser(user)
                _message.value = application.getString(R.string.msg_user_updated)

                resetForm()
                onSuccess(user)

            } catch (e: Exception) {
                _message.value = e.message ?: application.getString(R.string.msg_error_generic)
            } finally {
                _isUpdating.value = false
            }
        }
    }

    /** Reset fields to initial state after successful update. */
    private fun resetForm() {
        _name.value = ""
        _lastname.value = ""
        _username.value = ""
        _phone.value = ""
        _email.value = ""
        _country.value = "Colombia"
        _city.value = "Armenia"
        _currentPassword.value = ""
        _newPassword.value = ""
        _confirmPassword.value = ""
        _nameError.value = null
        _phoneError.value = null
        _emailError.value = null
        _passwordError.value = null
    }

    /** Clears temporary messages (for Snackbar). */
    fun clearMessage() {
        _message.value = null
    }

    // -------------------------------------------------------------------------
    // 🔹 Factory for dependency injection
    // -------------------------------------------------------------------------

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserUpdateViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserUpdateViewModel(application, UserRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

