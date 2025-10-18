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
 * This repository is independent from [UserRepository].
 * Coordination between both should occur at the ViewModel level.
 */
object PlaceRepository {

    // -------------------------------------------------------------------------
    // 🔹 INTERNAL STATE
    // -------------------------------------------------------------------------

    /** Internal mutable list of all places currently in memory. */
    private val _places = mutableListOf<Place>()

    /** Public immutable view of all registered places. */
    val places: List<Place>
        get() = _places.toList()

    // -------------------------------------------------------------------------
    // 🔹 CREATION
    // -------------------------------------------------------------------------

    /**
     * Adds an existing [Place] instance to the repository.
     *
     * @param place The [Place] object to add.
     * @return `true` if added successfully, `false` if a place with the same ID already exists.
     */
    fun addPlace(place: Place): Boolean {
        if (_places.any { it.id == place.id }) return false
        _places.add(place)
        return true
    }

    /**
     * Creates and registers a new [Place] using the [buildPlace] DSL.
     *
     * Example:
     * ```
     * PlaceRepository.createPlace {
     *     name = "Café del Parque"
     *     description = "Cafetería artesanal con vista al parque principal"
     *     category = PlaceCategory.CAFE
     *     address = "Cra 14 # 9-30, Armenia"
     *     phone = "3205556789"
     *     owner = currentUser
     *     setImageUrls(listOf(URL("https://example.com/image1.jpg")))
     * }
     * ```
     *
     * @param init Lambda used to initialize the [PlaceBuilderDSL].
     * @return The newly created and registered [Place].
     */
    fun createPlace(init: PlaceBuilderDSL.() -> Unit): Place {
        val newPlace = buildPlace(init)
        _places.add(newPlace)
        return newPlace
    }

    /**
     * Creates and registers a minimal placeholder [Place] with generated defaults.
     * This method ensures all required parameters are initialized properly.
     */
    fun createEmptyPlace(): Place {
        val newPlace = buildPlace {
            id = UUID.randomUUID().toString()
        }
        _places.add(newPlace)
        return newPlace
    }

    // -------------------------------------------------------------------------
    // 🔹 READ OPERATIONS
    // -------------------------------------------------------------------------

    fun getAllPlaces(): List<Place> = _places.toList()

    fun getPlaceById(placeId: String): Place? =
        _places.find { it.id == placeId }

    fun getPlacesByUser(userId: String): List<Place> =
        _places.filter { it.owner.id == userId }

    fun getPlacesByStatus(status: PlaceStatus): List<Place> =
        _places.filter { it.status == status }

    fun searchPlacesByName(query: String): List<Place> {
        val normalized = query.trim().lowercase()
        if (normalized.isEmpty()) return getAllPlaces()
        return _places.filter { it.name.lowercase().contains(normalized) }
    }

    // -------------------------------------------------------------------------
    // 🔹 UPDATE OPERATIONS
    // -------------------------------------------------------------------------

    fun updatePlace(updatedPlace: Place): Boolean {
        val index = _places.indexOfFirst { it.id == updatedPlace.id }
        return if (index != -1) {
            _places[index] = updatedPlace
            true
        } else false
    }

    fun updatePlaceStatus(placeId: String, newStatus: PlaceStatus): Boolean {
        val index = _places.indexOfFirst { it.id == placeId }
        if (index == -1) return false
        val updated = _places[index].copy(status = newStatus)
        _places[index] = updated
        return true
    }

    // -------------------------------------------------------------------------
    // 🔹 DELETE OPERATIONS
    // -------------------------------------------------------------------------

    fun removePlace(placeId: String): Boolean {
        val index = _places.indexOfFirst { it.id == placeId }
        if (index == -1) return false
        _places.removeAt(index)
        return true
    }

    fun removePlacesByUser(userId: String): Int {
        val before = _places.size
        _places.removeAll { it.owner.id == userId }
        return before - _places.size
    }

    // -------------------------------------------------------------------------
    // 🔹 MAINTENANCE
    // -------------------------------------------------------------------------

    fun clear() {
        _places.clear()
    }

    // -------------------------------------------------------------------------
    // 🔹 UTILITIES
    // -------------------------------------------------------------------------

    /**
     * Generates a dummy user when a place requires a non-null owner.
     */
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
