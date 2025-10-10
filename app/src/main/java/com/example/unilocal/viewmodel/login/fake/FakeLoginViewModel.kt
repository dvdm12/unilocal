package com.example.unilocal.viewmodel.login.fake

import com.example.unilocal.model.Role
import com.example.unilocal.model.User
import com.example.unilocal.viewmodel.login.LoginResult
import com.example.unilocal.viewmodel.login.LoginUiState
import com.example.unilocal.viewmodel.login.LoginViewModelBase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class FakeLoginViewModel : LoginViewModelBase {

    private val _uiState = MutableStateFlow(
        LoginUiState(
            email = "test@correo.com",
            password = "123456",
            emailError = null,
            passwordError = null,
            loginResult = null,
            currentUser = null
        )
    )
    override val uiState: StateFlow<LoginUiState> = _uiState

    override fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }

    override fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    override fun onLoginClick() {
        val email = _uiState.value.email
        val password = _uiState.value.password

        if (email == "test@correo.com" && password == "123456") {
            val fakeUser = User(
                id = "999",
                name = "Test User",
                username = "testuser",
                password = "123456",
                email = "test@correo.com",
                country = "Testland",
                city = "Testville",
                isActive = true,
                role = Role.USER
            )

            _uiState.update {
                it.copy(
                    currentUser = fakeUser,
                    loginResult = LoginResult.SUCCESS_USER
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    loginResult = LoginResult.INVALID_CREDENTIALS
                )
            }
        }
    }

    override fun clearLoginResult() {
        _uiState.update { it.copy(loginResult = null) }
    }
}
