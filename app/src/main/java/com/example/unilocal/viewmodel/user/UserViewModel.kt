package com.example.unilocal.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unilocal.model.Place
import com.example.unilocal.model.PlaceStatus
import com.example.unilocal.model.User
import com.example.unilocal.repository.PlaceRepository
import com.example.unilocal.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing user data and coordinating
 * persistence between [UserRepository] and [PlaceRepository].
 *
 * Main responsibilities:
 * - Manage user sessions and persistence.
 * - Coordinate the relationship between users and their places.
 * - Provide moderation utilities for updating place statuses.
 *
 * This ViewModel does not perform data storage directly; it delegates
 * that responsibility to the respective repositories, ensuring separation
 * of concerns and cleaner architecture.
 */
class UserViewModel(
    private val userRepository: UserRepository = UserRepository,
    private val placeRepository: PlaceRepository = PlaceRepository
) : ViewModel() {

    // -------------------------------------------------------------------------
    // USER STATE
    // -------------------------------------------------------------------------

    /** Holds the currently active user in memory. */
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    /** Holds the latest message or feedback for UI display. */
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // -------------------------------------------------------------------------
    // USER MANAGEMENT
    // -------------------------------------------------------------------------

    /** Sets the currently active user for this session. */
    fun setActiveUser(user: User) {
        _user.value = user
    }

    /** Clears the current active user (used during logout). */
    fun clearUser() {
        _user.value = null
    }

    // -------------------------------------------------------------------------
    // PLACE MANAGEMENT
    // -------------------------------------------------------------------------

    /**
     * Adds a new place to the active user and persists the update.
     *
     * This method performs the following steps:
     * 1. Registers the place globally in [PlaceRepository].
     * 2. Adds the same place to the current user’s local list.
     * 3. Persists the updated user in [UserRepository].
     */
    fun addPlace(place: Place) {
        val currentUser = _user.value ?: return

        viewModelScope.launch {
            // Register globally
            placeRepository.addPlace(place.copy(owner = currentUser))

            // Update user's local list
            val updatedPlaces = currentUser.places.toMutableList().apply {
                add(place.copy(owner = currentUser))
            }
            val updatedUser = currentUser.copy(places = updatedPlaces)

            // Persist in UserRepository
            _user.value = updatedUser
            userRepository.updateUser(updatedUser)

            _message.value = "Place added successfully."
        }
    }

    /**
     * Removes a place from both the active user's list and
     * the global [PlaceRepository].
     *
     * If the place does not exist globally, the user data is
     * still updated to ensure local consistency.
     */
    fun removePlace(placeId: String) {
        val currentUser = _user.value ?: return

        viewModelScope.launch {
            // Remove globally
            val removedFromSystem = placeRepository.removePlace(placeId)

            // Remove from user
            val updatedPlaces = currentUser.places.filterNot { it.id == placeId }.toMutableList()
            val updatedUser = currentUser.copy(places = updatedPlaces)

            _user.value = updatedUser
            userRepository.updateUser(updatedUser)

            _message.value = if (removedFromSystem)
                "Place removed successfully."
            else
                "Place not found in the global system."
        }
    }

    /** Returns all places associated with the currently active user. */
    fun getUserPlaces(): List<Place> {
        return _user.value?.places ?: emptyList()
    }

    // -------------------------------------------------------------------------
    // MODERATOR FEATURES
    // -------------------------------------------------------------------------

    /** Returns all registered users from [UserRepository]. */
    fun getAllUsers(): List<User> {
        return userRepository.getAllUsers()
    }

    /**
     * Updates the status of a given [Place] (Approved, Rejected, Pending)
     * and persists the change both globally and for the place’s owner.
     *
     * This method ensures data consistency between the global repository
     * and the user who owns the place.
     */
    fun updatePlaceStatus(place: Place, newStatus: PlaceStatus) {
        viewModelScope.launch {
            // Update global status
            val updatedPlace = place.copy(status = newStatus)
            val updatedInRepo = placeRepository.updatePlace(updatedPlace)

            if (!updatedInRepo) {
                _message.value = "The place could not be found for update."
                return@launch
            }

            // Update owner's local list
            val owner = userRepository.getUserById(place.owner.id) ?: return@launch
            val updatedPlaces = owner.places.map {
                if (it.id == place.id) updatedPlace else it
            }
            val updatedUser = owner.copy(places = updatedPlaces.toMutableList())

            userRepository.updateUser(updatedUser)

            // Refresh if the current active user is the owner
            if (_user.value?.id == owner.id) {
                _user.value = updatedUser
            }

            _message.value = "The place status was updated to $newStatus."
        }
    }

    // -------------------------------------------------------------------------
    // UTILITIES
    // -------------------------------------------------------------------------

    /** Clears the latest message after it has been shown in the UI. */
    fun clearMessage() {
        _message.value = null
    }
}
