package com.example.unilocal.viewmodel.user

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.unilocal.R
import com.example.unilocal.model.Place
import com.example.unilocal.model.User
import com.example.unilocal.repository.IUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing user data and synchronizing it with [IUserRepository].
 *
 * ✅ Fully decoupled from Android Context.
 * ✅ Does NOT manage session persistence
 * ✅ Manages user operations such as updating, activation, and place management.
 */
class UserViewModel(
    private val userRepository: IUserRepository
) : ViewModel() {

    // -------------------------------------------------------------------------
    // 🔹 STATE
    // -------------------------------------------------------------------------

    /** Represents the current in-memory user retrieved from the repository. */
    private val _user = MutableStateFlow(userRepository.getSessionUser())
    val user: StateFlow<User?> = _user

    /** Emits message resource IDs for the UI layer to show feedback. */
    private val _messageRes = MutableStateFlow<Int?>(null)
    val messageRes: StateFlow<Int?> = _messageRes

    // -------------------------------------------------------------------------
    // 🔹 USER MANAGEMENT
    // -------------------------------------------------------------------------

    /** Retrieves all registered users. */
    fun getAllUsers(): List<User> = userRepository.getAllUsers()

    /** Deactivates a user (logical disable). */
    fun deactivateUser(userId: String) {
        viewModelScope.launch {
            val success = userRepository.deactivateUser(userId)
            _messageRes.value =
                if (success) R.string.msg_user_deactivated else R.string.msg_error_generic

            if (success && _user.value?.id == userId)
                _user.value = userRepository.getUserById(userId)
        }
    }

    /** Reactivates a previously disabled user. */
    fun activateUser(userId: String) {
        viewModelScope.launch {
            val success = userRepository.activateUser(userId)
            _messageRes.value =
                if (success) R.string.msg_user_activated else R.string.msg_error_generic

            if (success && _user.value?.id == userId)
                _user.value = userRepository.getUserById(userId)
        }
    }

    // -------------------------------------------------------------------------
    // 🔹 USER PLACES MANAGEMENT
    // -------------------------------------------------------------------------

    /**
     * Adds a new place to the current user and persists the change.
     * If no active user exists, this call has no effect.
     */
    fun addPlaceToUser(place: Place, onResult: (User?) -> Unit = {}) {
        val currentUser = _user.value ?: return
        viewModelScope.launch {
            val success = userRepository.addPlaceToUser(currentUser.id, place)
            _messageRes.value =
                if (success) R.string.msg_place_created else R.string.msg_error_generic

            if (success) {
                // Recupera el usuario actualizado desde el repositorio
                val updatedUser = userRepository.getUserById(currentUser.id)
                _user.value = updatedUser
                onResult(updatedUser)
            } else {
                onResult(null)
            }
        }
    }




    /**
     * Removes a place from the user's list by its ID.
     * Updates local and repository data if successful.
     */
    fun removePlaceFromUserList(placeId: String) {
        val currentUser = _user.value ?: return
        viewModelScope.launch {
            val success = userRepository.removePlaceFromUser(currentUser.id, placeId)
            _messageRes.value =
                if (success) R.string.msg_place_deleted else R.string.msg_place_not_found

            if (success)
                _user.value = userRepository.getUserById(currentUser.id)
        }
    }

    /**
     * Updates a specific place in the user's owned list.
     * Synchronizes both in-memory and persistent states.
     */
    fun updateUserPlace(updatedPlace: Place) {
        val currentUser = _user.value ?: return
        viewModelScope.launch {
            val success = userRepository.updateUserPlace(currentUser.id, updatedPlace)
            _messageRes.value =
                if (success) R.string.msg_place_updated else R.string.msg_error_generic

            if (success)
                _user.value = userRepository.getUserById(currentUser.id)
        }
    }

    /** Returns the list of places owned by the active user. */
    fun getUserPlaces(): List<Place> = _user.value?.places ?: emptyList()

    // -------------------------------------------------------------------------
    // 🔹 UTILITIES
    // -------------------------------------------------------------------------

    /** Clears the last emitted message resource ID. */
    fun clearMessage() {
        _messageRes.value = null
    }

    // -------------------------------------------------------------------------
    // 🔹 FACTORY
    // -------------------------------------------------------------------------

    companion object {
        /**
         * Factory for dependency-safe creation of [UserViewModel].
         * Allows injecting custom [IUserRepository] implementations.
         */
        fun provideFactory(
            userRepository: IUserRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                    return UserViewModel(userRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
