package com.example.unilocal.repository

import com.example.unilocal.model.*
import java.net.URL
import java.util.UUID

/**
 * In-memory repository responsible for managing all [Place] objects in the system.
 *
 * Responsibilities:
 * - Stores all created places, regardless of their owner.
 * - Provides CRUD operations for places.
 * - Supports creation using the [buildPlace] DSL to ensure valid initialization.
 * - Allows moderators or admins to list, update, or remove any place globally.
 *
 * Compatible with transient `Place.owner: User?` to avoid recursive serialization.
 */
object PlaceRepository {

    // -------------------------------------------------------------------------
    // INTERNAL STATE
    // -------------------------------------------------------------------------

    private val _places = mutableListOf<Place>()

    val places: List<Place>
        get() = _places.toList()

    // -------------------------------------------------------------------------
    // CREATION
    // -------------------------------------------------------------------------

    /**
     * Adds an existing [Place] to the repository.
     * If no owner is provided, assigns a dummy placeholder user.
     */
    fun addPlace(place: Place): Boolean {
        if (_places.any { it.id == place.id }) return false

        val resolvedPlace = if (place.owner != null) {
            place
        } else {
            place.copy(owner = dummyUser())
        }

        _places.add(resolvedPlace)
        return true
    }

    /**
     * Creates and registers a new [Place] using the [buildPlace] DSL.
     * If no owner is set, assigns a dummy placeholder user to maintain integrity.
     */
    fun createPlace(init: PlaceBuilderDSL.() -> Unit): Place {
        val newPlace = buildPlace(init)
        val resolvedPlace = if (newPlace.owner != null) {
            newPlace
        } else {
            newPlace.copy(owner = dummyUser())
        }

        _places.add(resolvedPlace)
        return resolvedPlace
    }

    /**
     * Creates a minimal placeholder [Place].
     */
    fun createEmptyPlace(): Place {
        val newPlace = buildPlace {
            id = UUID.randomUUID().toString()
        }
        val resolved = newPlace.copy(owner = dummyUser())
        _places.add(resolved)
        return resolved
    }

    // -------------------------------------------------------------------------
    // READ OPERATIONS
    // -------------------------------------------------------------------------

    fun getAllPlaces(): List<Place> = _places.toList()

    fun getPlaceById(placeId: String): Place? =
        _places.find { it.id == placeId }

    fun getPlacesByUser(userId: String): List<Place> =
        _places.filter { it.owner?.id == userId }

    fun getPlacesByStatus(status: PlaceStatus): List<Place> =
        _places.filter { it.status == status }

    fun searchPlacesByName(query: String): List<Place> {
        val normalized = query.trim().lowercase()
        if (normalized.isEmpty()) return getAllPlaces()
        return _places.filter { it.name.lowercase().contains(normalized) }
    }

    // -------------------------------------------------------------------------
    // UPDATE OPERATIONS
    // -------------------------------------------------------------------------

    fun updatePlace(updatedPlace: Place): Boolean {
        val index = _places.indexOfFirst { it.id == updatedPlace.id }
        return if (index != -1) {
            val resolved = if (updatedPlace.owner != null) {
                updatedPlace
            } else {
                updatedPlace.copy(owner = dummyUser())
            }
            _places[index] = resolved
            true
        } else false
    }

    fun updatePlaceStatus(placeId: String, newStatus: PlaceStatus): Boolean {
        val index = _places.indexOfFirst { it.id == placeId }
        if (index == -1) return false
        val existing = _places[index]
        val updated = existing.copy(status = newStatus, owner = existing.owner ?: dummyUser())
        _places[index] = updated
        return true
    }

    // -------------------------------------------------------------------------
    // DELETE OPERATIONS
    // -------------------------------------------------------------------------

    fun removePlace(placeId: String): Boolean {
        val index = _places.indexOfFirst { it.id == placeId }
        if (index == -1) return false
        _places.removeAt(index)
        return true
    }

    fun removePlacesByUser(userId: String): Int {
        val before = _places.size
        _places.removeAll { it.owner?.id == userId }
        return before - _places.size
    }

    // -------------------------------------------------------------------------
    // MAINTENANCE
    // -------------------------------------------------------------------------

    fun clear() {
        _places.clear()
    }

    // -------------------------------------------------------------------------
    // UTILITIES
    // -------------------------------------------------------------------------

    private fun dummyUser(): User = User(
        id = "user-default",
        name = "Anonymous",
        username = "anon",
        password = "password",
        email = "anon@example.com",
        country = "Unknown",
        city = "Nowhere",
        isActive = true,
        role = Role.USER
    )
}
