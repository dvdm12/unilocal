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
 * ViewModel responsible for managing user profile updates and form validation.
 *
 * ✅ Features:
 * - Field-level validation and real-time error feedback
 * - Full control of form state (hydratable, resettable, protected against overwrites)
 * - Password validation in plain text (no hashing yet)
 * - Persistence via [UserRepository]
 * - Automatic hydration after update
 */
class UserUpdateViewModel(
    private val application: Application,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {

    // -------------------------------------------------------------------------
    // Form fields (observable states)
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

    /** Prevents overwriting user edits when hydrating the form */
    private val _isDirty = MutableStateFlow(false)

    // -------------------------------------------------------------------------
    // Country-city map (for dropdown UI)
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
            "Mar del Plata", "Salta", "Santa Fe", "San Miguel de Tucumán", "Corrientes"
        ),
        "Chile" to listOf(
            "Santiago", "Valparaíso", "Concepción", "La Serena", "Antofagasta"
        ),
        "Perú" to listOf(
            "Lima", "Arequipa", "Trujillo", "Chiclayo", "Piura"
        )
    )

    // -------------------------------------------------------------------------
    // Validation
    // -------------------------------------------------------------------------
    private fun isEmailValid(email: String) =
        Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(email)

    private fun isPasswordValid(password: String) =
        password.isBlank() || password.length >= 5

    private fun validateForm(): Boolean {
        var isValid = true

        if (_name.value.isBlank()) {
            _nameError.value = application.getString(R.string.msg_error_field_required)
            isValid = false
        } else _nameError.value = null

        if (_email.value.isBlank() || !isEmailValid(_email.value)) {
            _emailError.value = application.getString(R.string.login_email_error)
            isValid = false
        } else _emailError.value = null

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
    // Update helpers
    // -------------------------------------------------------------------------
    private fun markDirty() { _isDirty.value = true }

    fun updateName(v: String) { _name.tryEmit(v); markDirty() }
    fun updateLastname(v: String) { _lastname.tryEmit(v); markDirty() }
    fun updateUsername(v: String) { _username.tryEmit(v); markDirty() }
    fun updateEmail(v: String) { _email.tryEmit(v); markDirty() }
    fun updateCountry(v: String) { _country.tryEmit(v); markDirty() }
    fun updateCity(v: String) { _city.tryEmit(v); markDirty() }
    fun updateCurrentPassword(v: String) { _currentPassword.tryEmit(v); markDirty() }
    fun updateNewPassword(v: String) { _newPassword.tryEmit(v); markDirty() }
    fun updateConfirmPassword(v: String) { _confirmPassword.tryEmit(v); markDirty() }

    // -------------------------------------------------------------------------
    // Hydration (prefill)
    // -------------------------------------------------------------------------
    fun prefillFrom(user: User, force: Boolean = false) {
        if (!force && _isDirty.value) return

        _name.value = user.name
        _lastname.value = _lastname.value
        _username.value = user.username
        _email.value = user.email
        _country.value = user.country
        _city.value = user.city

        _currentPassword.value = ""
        _newPassword.value = ""
        _confirmPassword.value = ""

        _nameError.value = null
        _emailError.value = null
        _passwordError.value = null
        _isDirty.value = false
    }

    // -------------------------------------------------------------------------
    // Core update logic
    // -------------------------------------------------------------------------
    fun updateUser(currentUser: User, onSuccess: (User) -> Unit = {}) {
        viewModelScope.launch {
            if (_isUpdating.value) return@launch

            try {
                _isUpdating.value = true
                logStart(currentUser)

                if (!validateFormAndPassword(currentUser)) return@launch

                val updatedUser = buildUpdatedUser(currentUser)
                persistUserUpdate(currentUser, updatedUser, onSuccess)

            } catch (e: Exception) {
                handleException(e)
            } finally {
                finalizeUpdate(currentUser)
            }
        }
    }

    private fun validateFormAndPassword(currentUser: User): Boolean {
        if (!validateForm()) {
            _message.value = application.getString(R.string.msg_field_required)
            return false
        }

        if (_newPassword.value.isNotBlank() && _currentPassword.value.trim() != currentUser.password.trim()) {
            _passwordError.value = application.getString(R.string.msg_error_current_password_incorrect)
            _message.value = application.getString(R.string.msg_error_current_password_required)
            return false
        }

        return true
    }

    private fun buildUpdatedUser(currentUser: User): User = currentUser.copy(
        name = listOf(_name.value, _lastname.value)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .ifBlank { currentUser.name },
        username = _username.value.ifBlank { currentUser.username },
        password = _newPassword.value.ifBlank { currentUser.password },
        email = _email.value.ifBlank { currentUser.email },
        country = _country.value.ifBlank { currentUser.country },
        city = _city.value.ifBlank { currentUser.city },
        places = currentUser.places,
        role = currentUser.role,
        isActive = currentUser.isActive
    )

    private suspend fun persistUserUpdate(currentUser: User, updated: User, onSuccess: (User) -> Unit) {
        val success = userRepository.updateUser(updated)
        if (success) {
            val fresh = userRepository.getUserById(currentUser.id)
            if (fresh != null) {
                _message.value = application.getString(R.string.msg_user_updated)
                prefillFrom(fresh, force = true)
                onSuccess(fresh)
            } else {
                _message.value = application.getString(R.string.msg_error_generic)
            }
        } else {
            _message.value = application.getString(R.string.msg_error_generic)
        }
    }

    // -------------------------------------------------------------------------
    // Utility and logging
    // -------------------------------------------------------------------------
    fun clearMessage() { _message.value = null }

    fun onExitEditScreen() = resetForm()

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
        _isDirty.value = false
    }

    private fun logStart(user: User) =
        android.util.Log.d("UserUpdate", "Updating user ID=${user.id}, name=${user.name}")

    private fun handleException(e: Exception) {
        _message.value = e.message ?: application.getString(R.string.msg_error_generic)
        android.util.Log.e("UserUpdate", "Exception during user update", e)
    }

    private fun finalizeUpdate(user: User?) {
        _isUpdating.value = false
        android.util.Log.d("UserUpdate", ">>> Update process completed for user ID=${user?.id ?: "unknown"}")
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
