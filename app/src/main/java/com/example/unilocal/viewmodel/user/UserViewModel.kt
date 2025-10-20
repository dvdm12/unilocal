package com.example.unilocal.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unilocal.model.Place
import com.example.unilocal.model.User
import com.example.unilocal.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing user data and synchronizing it
 * with [UserRepository].
 *
 * Responsibilities:
 * - Manage user session and persistence.
 * - Maintain and update user-specific information.
 * - Update the local list of places owned by the user (without direct global access).
 * - Provide user-related functionalities for the UI layer.
 */
class UserViewModel(
    private val userRepository: UserRepository = UserRepository
) : ViewModel() {

    // -------------------------------------------------------------------------
    // USER STATE
    // -------------------------------------------------------------------------

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // -------------------------------------------------------------------------
    // USER MANAGEMENT
    // -------------------------------------------------------------------------

    /** Sets the currently active user. */
    fun setActiveUser(user: User) {
        _user.value = user
    }

    /** Clears the current active user (logout). */
    fun clearUser() {
        _user.value = null
    }

    /** Returns all registered users (used by moderators). */
    fun getAllUsers(): List<User> = userRepository.getAllUsers()

    // -------------------------------------------------------------------------
    // USER'S PLACES MANAGEMENT (LOCAL ONLY)
    // -------------------------------------------------------------------------

    /**
     * Adds a place to the current user's local list.
     * Does not persist it globally — that is handled by PlaceViewModel.
     */
    fun addPlaceToUser(place: Place) {
        val currentUser = _user.value ?: return

        val updatedPlaces = currentUser.places.toMutableList().apply { add(place.copy(owner = currentUser)) }
        val updatedUser = currentUser.copy(places = updatedPlaces)

        _user.value = updatedUser
        viewModelScope.launch { userRepository.updateUser(updatedUser) }

        _message.value = "Lugar agregado al usuario."
    }

    /**
     * Removes a place from the current user's local list.
     * This does not delete the place globally.
     */
    fun removePlaceFromUserList(placeId: String) {
        val currentUser = _user.value ?: return

        val updatedPlaces = currentUser.places.filterNot { it.id == placeId }.toMutableList()
        val updatedUser = currentUser.copy(places = updatedPlaces)

        _user.value = updatedUser
        viewModelScope.launch { userRepository.updateUser(updatedUser) }

        _message.value = "Lugar eliminado del usuario."
    }

    /** Returns all places associated with the current user. */
    fun getUserPlaces(): List<Place> = _user.value?.places ?: emptyList()

    // -------------------------------------------------------------------------
    // UTILITIES
    // -------------------------------------------------------------------------

    /** Clears the current message after displaying it on the UI. */
    fun clearMessage() {
        _message.value = null
    }
}
