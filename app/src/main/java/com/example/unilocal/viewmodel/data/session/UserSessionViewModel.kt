package com.example.unilocal.viewmodel.data.session

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unilocal.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserSessionViewModel(context: Context) : ViewModel() {

    private val sessionManager = SessionManager(context)

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _isSessionLoaded = MutableStateFlow(false)
    val isSessionLoaded: StateFlow<Boolean> = _isSessionLoaded

    init {
        // Cargar usuario guardado al iniciar
        viewModelScope.launch {
            _currentUser.value = sessionManager.getUser()
            _isSessionLoaded.value = true
        }
    }

    fun setUser(user: User) {
        viewModelScope.launch {
            sessionManager.saveUser(user)
            _currentUser.value = user
        }
    }

    fun clearSession() {
        viewModelScope.launch {
            sessionManager.clearUser()
            _currentUser.value = null
        }
    }
}
