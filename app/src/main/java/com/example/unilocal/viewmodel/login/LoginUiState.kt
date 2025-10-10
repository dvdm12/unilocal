package com.example.unilocal.viewmodel.login

import com.example.unilocal.model.User


data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val loginResult: LoginResult? = null,
    val currentUser: User?=null
)