package com.example.unilocal.viewmodel.data.session

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.unilocal.model.User
import com.example.unilocal.repository.IUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing the user session lifecycle.
 *
 * Uses [AndroidViewModel] to safely access Application Context (avoids memory leaks).
 * Synchronizes persistent cache (SessionManager) with in-memory or remote repositories.
 * Ensures referential integrity between cached users and in-memory repository.
 */
class UserSessionViewModel(
    application: Application,
    private val userRepository: IUserRepository
) : AndroidViewModel(application) {

    /** Manages persistent session storage (e.g., SharedPreferences or DataStore). */
    private val sessionManager = SessionManager(application.applicationContext)

    // -------------------------------------------------------------------------
    // STATE
    // -------------------------------------------------------------------------

    /** Holds the current user session loaded from persistent cache (if any). */
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    /** Indicates whether the session initialization process has completed. */
    private val _isSessionLoaded = MutableStateFlow(false)
    val isSessionLoaded: StateFlow<Boolean> = _isSessionLoaded

    // -------------------------------------------------------------------------
    // INITIALIZATION
    // -------------------------------------------------------------------------

    init {
        viewModelScope.launch {
            val cachedUser = sessionManager.getUser()
            if (cachedUser != null) {
                val existingUser = userRepository.getUserById(cachedUser.id)

                // Register the user if not yet in memory
                if (existingUser == null && !userRepository.userExists(cachedUser.email)) {
                    userRepository.registerUser(
                        id = cachedUser.id,
                        name = cachedUser.name,
                        username = cachedUser.username,
                        password = cachedUser.password,
                        email = cachedUser.email,
                        country = cachedUser.country,
                        city = cachedUser.city
                    )
                }

                // 🔹 If the user exists, merge its places with cached version
                val userWithPlaces = existingUser?.copy(
                    places = cachedUser.places.toMutableList()
                ) ?: cachedUser

                // 🔹 Synchronize repository and session
                userRepository.setSessionUser(userWithPlaces)
                _currentUser.value = userWithPlaces
            }

            _isSessionLoaded.value = true
        }
    }

    // -------------------------------------------------------------------------
    // SESSION CONTROL
    // -------------------------------------------------------------------------

    /**
     * Persists and activates a user session.
     * Synchronizes both persistent storage and in-memory repository state.
     */
    fun setUser(user: User) {
        viewModelScope.launch {
            sessionManager.saveUser(user)

            val existingUser = userRepository.getUserById(user.id)
            if (existingUser == null && !userRepository.userExists(user.email)) {
                userRepository.registerUser(
                    id = user.id,
                    name = user.name,
                    username = user.username,
                    password = user.password,
                    email = user.email,
                    country = user.country,
                    city = user.city
                )
            }

            // 🔹 Update user in repository with full places list
            val userWithPlaces = existingUser?.copy(
                places = user.places.toMutableList()
            ) ?: user

            userRepository.setSessionUser(userWithPlaces)
            _currentUser.value = userWithPlaces
        }
    }

    /**
     * Clears the current user session from both cache and repository.
     * Used on logout or when credentials become invalid.
     */
    fun clearSession() {
        viewModelScope.launch {
            sessionManager.clearUser()
            userRepository.clearSessionUser()
            _currentUser.value = null
        }
    }

    /**
     * Returns the currently active user from session memory (not suspended).
     * If the session has not been initialized yet, this may return null.
     */
    fun getCurrentUser(): User? = _currentUser.value

    // -------------------------------------------------------------------------
    // UTILITIES
    // -------------------------------------------------------------------------

    /** Returns true if there is a valid user currently in session. */
    fun hasActiveSession(): Boolean = _currentUser.value != null

    // -------------------------------------------------------------------------
    // FACTORY
    // -------------------------------------------------------------------------

    companion object {
        /**
         * Factory for dependency-safe creation of [UserSessionViewModel].
         * Prevents memory leaks and allows injection of different repository implementations.
         */
        fun provideFactory(
            application: Application,
            userRepository: IUserRepository
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(UserSessionViewModel::class.java)) {
                        return UserSessionViewModel(application, userRepository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }
        }
    }
}
