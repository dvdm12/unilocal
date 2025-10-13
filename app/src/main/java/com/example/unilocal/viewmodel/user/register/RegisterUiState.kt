package com.example.unilocal.viewmodel.user.register

import com.example.unilocal.model.User

/**
 * Representa el estado completo del formulario de registro.
 * Incluye campos de texto, errores de validación y banderas de estado global.
 */
data class RegisterUiState(
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val country: String = "",
    val city: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    // --- Errores específicos por campo ---
    val nameError: String? = null,
    val lastNameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,

    // --- Estado general del formulario ---
    val isValid: Boolean = false,

    // --- Estado de resultado del registro ---
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val registeredUser: User? = null
)
