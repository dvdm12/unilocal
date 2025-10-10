package com.example.unilocal.viewmodel.user

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unilocal.model.Place
import com.example.unilocal.model.PlaceStatus
import com.example.unilocal.model.Schedule
import com.example.unilocal.model.User
import com.example.unilocal.viewmodel.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing user-related data and validation logic.
 * Includes schedule validation and synchronization with UserRepository.
 */
class UserViewModel(
    private val userRepository: UserRepository = UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    /**
     * Sets the current active user.
     */
    fun setActiveUser(user: User) {
        _user.value = user
    }

    /**
     * Adds a place to the active user and persists it.
     */
    fun addPlace(place: Place) {
        val currentUser = _user.value ?: return
        val newPlace = place.copy(owner = currentUser)

        val updatedPlaces = currentUser.places.toMutableList().apply { add(newPlace) }
        val updatedUser = currentUser.copy(places = updatedPlaces)

        _user.value = updatedUser
        viewModelScope.launch { userRepository.updateUser(updatedUser) }

        _message.value = "Place successfully added."
    }

    /**
     * Validates and adds a schedule to a specific place.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun addScheduleToPlace(placeId: String, schedule: Schedule) {
        val currentUser = _user.value ?: return

        // --- Validation ---
        if (schedule.dayStart !in 1..7 || schedule.dayEnd !in 1..7) {
            _message.value = "Days must be between 1 (Monday) and 7 (Sunday)."
            return
        }
        if (schedule.dayStart > schedule.dayEnd) {
            _message.value = "Start day cannot be after end day."
            return
        }
        if (schedule.start >= schedule.end) {
            _message.value = "Opening time must be earlier than closing time."
            return
        }

        // --- Update logic ---
        val updatedPlaces = currentUser.places.map { place ->
            if (place.id == placeId) {
                val overlap = place.schedules.any { existing ->
                    !(schedule.end <= existing.start || schedule.start >= existing.end)
                }
                if (overlap) {
                    _message.value = "Schedule overlaps with an existing one."
                    return
                }
                place.copy(schedules = place.schedules + schedule)
            } else place
        }.toMutableList() // ✅ convert to MutableList<Place>

        val updatedUser = currentUser.copy(places = updatedPlaces)
        _user.value = updatedUser

        viewModelScope.launch { userRepository.updateUser(updatedUser) }
        _message.value = "Schedule successfully added."
    }

    /**
     * Removes a schedule by matching day range.
     */
    fun removeSchedule(placeId: String, dayStart: Int, dayEnd: Int) {
        val currentUser = _user.value ?: return

        val updatedPlaces = currentUser.places.map { place ->
            if (place.id == placeId)
                place.copy(schedules = place.schedules.filterNot {
                    it.dayStart == dayStart && it.dayEnd == dayEnd
                })
            else place
        }.toMutableList() // ✅ convert to MutableList<Place>

        val updatedUser = currentUser.copy(places = updatedPlaces)
        _user.value = updatedUser

        viewModelScope.launch { userRepository.updateUser(updatedUser) }
        _message.value = "Schedule removed successfully."
    }

    /**
     * Returns all schedules from all user's places.
     */
    fun getAllSchedules(): List<Schedule> {
        val currentUser = _user.value ?: return emptyList()
        return currentUser.places.flatMap { it.schedules }
    }

    // -------------------------------------------------------------------------
    // 🔹 NUEVAS FUNCIONES PARA EL MODERADOR
    // -------------------------------------------------------------------------

    /**
     * Returns all users from the repository.
     * Used by moderators to review all places.
     */
    fun getAllUsers(): List<User> {
        return userRepository.getAllUsers()
    }

    /**
     * Updates the status of a given place (Approved, Rejected, Pending)
     * and persists the change in the corresponding user.
     */
    fun updatePlaceStatus(place: Place, newStatus: PlaceStatus) {
        val users = userRepository.getAllUsers().toMutableList()
        var updatedUser: User? = null

        // Buscar el usuario propietario y actualizar su lugar
        val updatedUsers = users.map { user ->
            if (user.id == place.owner.id) {
                val updatedPlaces = user.places.map {
                    if (it.id == place.id) it.copy(status = newStatus) else it
                }
                updatedUser = user.copy(places = updatedPlaces as MutableList<Place>)
                updatedUser!!
            } else user
        }

        // Persistir cambios si se encontró el usuario
        updatedUser?.let {
            viewModelScope.launch {
                userRepository.updateUser(it)
                _message.value = "Place status updated to $newStatus"
            }
        }
    }

    /**
     * Clears the current user data.
     */
    fun clearUser() {
        _user.value = null
    }

    /**
     * Clears current message (after displaying it).
     */
    fun clearMessage() {
        _message.value = null
    }
}
