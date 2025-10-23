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
 * ViewModel responsible for managing the user profile update process.
 *
 * Responsibilities:
 * - Provides field-level validation and error messages.
 * - Preserves immutable properties (places, role, and active status) during updates.
 * - Delegates the merging and persistence logic entirely to the UserRepository.
 */
class UserUpdateViewModel(
    private val application: Application,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {

    // -------------------------------------------------------------------------
    // Form fields (observed state)
    // -------------------------------------------------------------------------
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _lastname = MutableStateFlow("")
    val lastname: StateFlow<String> = _lastname

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

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
    // Error states
    // -------------------------------------------------------------------------
    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError

    // -------------------------------------------------------------------------
    // UI state
    // -------------------------------------------------------------------------
    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // -------------------------------------------------------------------------
    // Country-city map (no validation)
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
    // Validation helpers
    // -------------------------------------------------------------------------
    private fun isEmailValid(email: String): Boolean =
        email.contains("@") && email.contains(".")

    private fun isPasswordValid(password: String): Boolean =
        password.isBlank() || password.length >= 5

    /**
     * Validates form input and updates error states.
     * Country and city fields are excluded from validation.
     */
    private fun validateForm(): Boolean {
        var isValid = true

        // Validate name
        if (_name.value.isBlank()) {
            _nameError.value = application.getString(R.string.msg_error_field_required)
            isValid = false
        } else _nameError.value = null

        // Validate email
        if (_email.value.isBlank() || !isEmailValid(_email.value)) {
            _emailError.value = application.getString(R.string.login_email_error)
            isValid = false
        } else _emailError.value = null

        // Validate password logic
        if (_newPassword.value.isNotBlank()) {
            when {
                !isPasswordValid(_newPassword.value) -> {
                    _passwordError.value = application.getString(R.string.login_password_error)
                    isValid = false
                }
                _confirmPassword.value != _newPassword.value -> {
                    _passwordError.value = application.getString(R.string.msg_error_password_mismatch)
                    isValid = false
                }
                _currentPassword.value.isBlank() -> {
                    _passwordError.value = application.getString(R.string.msg_error_current_password_required)
                    isValid = false
                }
                else -> _passwordError.value = null
            }
        } else _passwordError.value = null

        return isValid
    }

    // -------------------------------------------------------------------------
    // Public field updates
    // -------------------------------------------------------------------------
    fun updateName(value: String) = _name.tryEmit(value)
    fun updateLastname(value: String) = _lastname.tryEmit(value)
    fun updateUsername(value: String) = _username.tryEmit(value)
    fun updateEmail(value: String) = _email.tryEmit(value)
    fun updateCountry(value: String) = _country.tryEmit(value)
    fun updateCity(value: String) = _city.tryEmit(value)
    fun updateCurrentPassword(value: String) = _currentPassword.tryEmit(value)
    fun updateNewPassword(value: String) = _newPassword.tryEmit(value)
    fun updateConfirmPassword(value: String) = _confirmPassword.tryEmit(value)

    // -------------------------------------------------------------------------
    // Core logic
    // -------------------------------------------------------------------------
    fun updateUser(currentUser: User, onSuccess: (User) -> Unit = {}) {
        viewModelScope.launch {
            try {
                _isUpdating.value = true

                // --- Log: Inicio de la actualización ---
                android.util.Log.d("UserUpdate", ">>> Starting update for user ID=${currentUser.id}, name=${currentUser.name}")
                android.util.Log.d("UserUpdate", "Current places (${currentUser.places.size}): ${currentUser.places.joinToString { it.name }}")

                // --- Validar campos del formulario ---
                if (!validateForm()) {
                    _message.value = application.getString(R.string.msg_field_required)
                    android.util.Log.w("UserUpdate", "Validation failed: Missing or invalid fields")
                    return@launch
                }

                // --- Validar cambio de contraseña ---
                if (_newPassword.value.isNotBlank() && _currentPassword.value != currentUser.password) {
                    _passwordError.value = application.getString(R.string.msg_error_current_password_incorrect)
                    _message.value = application.getString(R.string.msg_error_current_password_required)
                    android.util.Log.w("UserUpdate", "Password update blocked: current password mismatch")
                    return@launch
                }

                // --- Construir copia parcial editable ---
                val partialUser = currentUser.copy(
                    name = listOf(_name.value, _lastname.value)
                        .filter { it.isNotBlank() }
                        .joinToString(" "),
                    username = _username.value.ifBlank { currentUser.username },
                    password = _newPassword.value.ifBlank { currentUser.password },
                    email = _email.value.ifBlank { currentUser.email },
                    country = _country.value.ifBlank { currentUser.country },
                    city = _city.value.ifBlank { currentUser.city }
                )

                android.util.Log.d("UserUpdate", "Prepared partialUser for merge: ${partialUser.name}")

                // --- Persistencia mediante el repositorio ---
                val success = userRepository.updateUser(partialUser)
                android.util.Log.d("UserUpdate", "Repository update result: $success")

                if (success) {
                    val updated = userRepository.getUserById(currentUser.id)!!

                    android.util.Log.d("UserUpdate", "User updated successfully")
                    android.util.Log.d("UserUpdate", "Updated user ID=${updated.id}, name=${updated.name}")
                    android.util.Log.d("UserUpdate", "Updated places (${updated.places.size}): ${updated.places.joinToString { it.name }}")

                    _message.value = application.getString(R.string.msg_user_updated)
                    resetForm()
                    onSuccess(updated)
                } else {
                    _message.value = application.getString(R.string.msg_error_generic)
                    android.util.Log.e("UserUpdate", "User not found in repository during update")
                }

            } catch (e: Exception) {
                _message.value = e.message ?: application.getString(R.string.msg_error_generic)
                android.util.Log.e("UserUpdate", "Exception during user update", e)
            } finally {
                _isUpdating.value = false
                android.util.Log.d("UserUpdate", ">>> Update process completed for user ID=${currentUser.id}")
            }
        }
    }


    // -------------------------------------------------------------------------
    // Utility methods
    // -------------------------------------------------------------------------
    private fun resetForm() {
        _name.value = ""
        _lastname.value = ""
        _username.value = ""
        _email.value = ""
        _country.value = "Colombia"
        _city.value = "Armenia"
        _currentPassword.value = ""
        _newPassword.value = ""
        _confirmPassword.value = ""
        _nameError.value = null
        _emailError.value = null
        _passwordError.value = null
    }

    fun clearMessage() {
        _message.value = null
    }

    // -------------------------------------------------------------------------
    // Factory for dependency injection
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
