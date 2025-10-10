package com.example.unilocal.viewmodel.login


import kotlinx.coroutines.flow.StateFlow

interface LoginViewModelBase {
    val uiState: StateFlow<LoginUiState>
    fun onEmailChange(email: String)
    fun onPasswordChange(password: String)
    fun onLoginClick()
    fun clearLoginResult()
}
