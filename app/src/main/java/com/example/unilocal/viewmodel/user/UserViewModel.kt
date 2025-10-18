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
 * ViewModel responsible for managing user data and synchronizing it
 * with [UserRepository] and [PlaceRepository].
 *
 * Responsibilities:
 * - Handles user session and persistence.
 * - Coordinates the relationship between users and places.
 * - Allows moderators to update place statuses.
 */
class UserViewModel(
    private val userRepository: UserRepository = UserRepository,
    private val placeRepository: PlaceRepository = PlaceRepository
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

    /** Sets the currently active user. */
    fun setActiveUser(user: User) {
        _user.value = user
    }

    /** Clears the current active user (logout). */
    fun clearUser() {
        _user.value = null
    }

    // -------------------------------------------------------------------------
    // 🔹 PLACES MANAGEMENT
    // -------------------------------------------------------------------------

    /**
     * Adds a new place to the active user and persists the update.
     * The place is also registered globally in [PlaceRepository].
     */
    fun addPlace(place: Place) {
        val currentUser = _user.value ?: return

        viewModelScope.launch {
            // 1️⃣ Registrar el lugar en el repositorio global
            placeRepository.addPlace(place.copy(owner = currentUser))

            // 2️⃣ Actualizar la lista local del usuario
            val updatedPlaces = currentUser.places.toMutableList().apply {
                add(place.copy(owner = currentUser))
            }
            val updatedUser = currentUser.copy(places = updatedPlaces)

            // 3️⃣ Persistir en UserRepository
            _user.value = updatedUser
            userRepository.updateUser(updatedUser)

            _message.value = "Lugar agregado exitosamente."
        }
    }

    /**
     * Removes a place from the active user and from the system globally.
     */
    fun removePlace(placeId: String) {
        val currentUser = _user.value ?: return

        viewModelScope.launch {
            // 1️⃣ Eliminar del sistema global
            val removedFromSystem = placeRepository.removePlace(placeId)

            // 2️⃣ Eliminar de la lista del usuario
            val updatedPlaces = currentUser.places.filterNot { it.id == placeId }.toMutableList()
            val updatedUser = currentUser.copy(places = updatedPlaces)

            _user.value = updatedUser
            userRepository.updateUser(updatedUser)

            _message.value = if (removedFromSystem)
                "Lugar eliminado correctamente."
            else
                "Lugar no encontrado en el sistema global."
        }
    }

    /** Returns all places associated with the current user. */
    fun getUserPlaces(): List<Place> {
        return _user.value?.places ?: emptyList()
    }

    // -------------------------------------------------------------------------
    // 🔹 MODERATOR FEATURES
    // -------------------------------------------------------------------------

    /** Returns all registered users (used by moderators). */
    fun getAllUsers(): List<User> {
        return userRepository.getAllUsers()
    }

    /**
     * Updates the status of a given place (Approved, Rejected, Pending)
     * and persists the change globally and for the place’s owner.
     */
    fun updatePlaceStatus(place: Place, newStatus: PlaceStatus) {
        viewModelScope.launch {
            // 1️⃣ Actualizar estado global
            val updatedPlace = place.copy(status = newStatus)
            val updatedInRepo = placeRepository.updatePlace(updatedPlace)

            if (!updatedInRepo) {
                _message.value = "No se encontró el lugar para actualizar."
                return@launch
            }

            // 2️⃣ Actualizar la lista del usuario propietario
            val owner = userRepository.getUserById(place.owner.id) ?: return@launch
            val updatedPlaces = owner.places.map {
                if (it.id == place.id) updatedPlace else it
            }
            val updatedUser = owner.copy(places = updatedPlaces.toMutableList())

            userRepository.updateUser(updatedUser)

            // 3️⃣ Refrescar el usuario activo si corresponde
            if (_user.value?.id == owner.id) {
                _user.value = updatedUser
            }

            _message.value = "El estado del lugar fue actualizado a $newStatus"
        }
    }

    // -------------------------------------------------------------------------
    // 🔹 UTILITIES
    // -------------------------------------------------------------------------

    /** Clears the current message after displaying it on the UI. */
    fun clearMessage() {
        _message.value = null
    }
}
