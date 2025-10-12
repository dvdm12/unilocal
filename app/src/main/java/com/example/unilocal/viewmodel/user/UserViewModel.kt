package com.example.unilocal.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unilocal.model.Place
import com.example.unilocal.model.PlaceStatus
import com.example.unilocal.model.User
import com.example.unilocal.viewmodel.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing user data and synchronizing it with the UserRepository.
 *
 * This class follows the single responsibility principle:
 * - Handles user session and persistence.
 * - Manages places associated with the active user.
 * - Allows moderators to update place statuses.
 *
 * Schedule-related logic has been fully delegated to ScheduleViewModel.
 */
class UserViewModel(
    private val userRepository: UserRepository = UserRepository
) : ViewModel() {

    // -------------------------------------------------------------------------
    // 🔹 USER STATE
    // -------------------------------------------------------------------------

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // -------------------------------------------------------------------------
    // 🔹 USER MANAGEMENT
    // -------------------------------------------------------------------------

    /**
     * Sets the currently active user.
     */
    fun setActiveUser(user: User) {
        _user.value = user
    }

    /**
     * Clears the current active user (logout operation).
     */
    fun clearUser() {
        _user.value = null
    }

    // -------------------------------------------------------------------------
    // 🔹 PLACES MANAGEMENT
    // -------------------------------------------------------------------------

    /**
     * Adds a new place to the active user and persists the update.
     */
    fun addPlace(place: Place) {
        val currentUser = _user.value ?: return

        val updatedPlaces = currentUser.places.toMutableList().apply { add(place.copy(owner = currentUser)) }
        val updatedUser = currentUser.copy(places = updatedPlaces)

        _user.value = updatedUser
        viewModelScope.launch { userRepository.updateUser(updatedUser) }

        _message.value = "Lugar agregado exitosamente."
    }

    /**
     * Removes a place from the active user based on its ID.
     */
    fun removePlace(placeId: String) {
        val currentUser = _user.value ?: return

        val updatedPlaces = currentUser.places.filterNot { it.id == placeId }.toMutableList()
        val updatedUser = currentUser.copy(places = updatedPlaces)

        _user.value = updatedUser
        viewModelScope.launch { userRepository.updateUser(updatedUser) }

        _message.value = "Lugar eliminado correctamente."
    }

    /**
     * Returns all places associated with the current user.
     */
    fun getUserPlaces(): List<Place> {
        return _user.value?.places ?: emptyList()
    }

    // -------------------------------------------------------------------------
    // 🔹 MODERATOR FEATURES
    // -------------------------------------------------------------------------

    /**
     * Returns all registered users from the repository.
     * Used by moderators to review all available places.
     */
    fun getAllUsers(): List<User> {
        return userRepository.getAllUsers()
    }

    /**
     * Updates the status of a given place (Approved, Rejected, Pending)
     * and persists the change in the corresponding user's record.
     */
    fun updatePlaceStatus(place: Place, newStatus: PlaceStatus) {
        val users = userRepository.getAllUsers()

        val user = users.find { it.id == place.owner.id } ?: return
        val updatedPlaces = user.places.map {
            if (it.id == place.id) it.copy(status = newStatus) else it
        }
        val updatedUser = user.copy(places = updatedPlaces as MutableList<Place>)

        viewModelScope.launch {
            userRepository.updateUser(updatedUser)
            _message.value = "El estado del lugar fue actualizado a $newStatus"
        }
    }


    // -------------------------------------------------------------------------
    // 🔹 UTILITIES
    // -------------------------------------------------------------------------

    /**
     * Clears the current message after displaying it on the UI.
     */
    fun clearMessage() {
        _message.value = null
    }
}
