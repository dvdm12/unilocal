package com.example.unilocal.viewmodel.data

import com.example.unilocal.model.Place
import com.example.unilocal.model.Role
import com.example.unilocal.model.User
import java.util.UUID

/**
 * In-memory repository that manages application users.
 *
 * Responsibilities:
 * - Stores default simulated users.
 * - Allows registration of new users.
 * - Provides search and validation methods.
 * - Supports place status updates for moderators.
 */
object UserRepository {

    // Internal mutable list of users (in memory)
    private val _users = mutableListOf(
        User(
            id = "1",
            name = "Juan Pérez",
            username = "juanp",
            password = "123456",
            email = "juan@correo.com",
            country = "Colombia",
            city = "Bogotá",
            isActive = true,
            role = Role.USER
        ),
        User(
            id = "2",
            name = "Laura Admin",
            username = "lauraadmin",
            password = "adminpass",
            email = "laura@admin.com",
            country = "Colombia",
            city = "Medellín",
            isActive = true,
            role = Role.MODERATOR
        )
    )

    // Returns all current users as an immutable list
    val users: List<User>
        get() = _users.toList()

    /**
     * Returns all current users.
     * Used by moderators to list all user data.
     */
    fun getAllUsers(): List<User> = _users.toList()

    // Finds a user by email and password, only if active
    fun findUser(email: String, password: String): User? =
        _users.find { it.email == email && it.password == password && it.isActive }

    // Checks if a user with the given email already exists
    fun userExists(email: String): Boolean =
        _users.any { it.email.equals(email, ignoreCase = true) }

    /**
     * Registers a new user and adds it to the repository.
     * @return true if added successfully, false if email is already in use.
     */
    fun registerUser(
        name: String,
        username: String,
        password: String,
        email: String,
        country: String,
        city: String
    ): Boolean {
        if (userExists(email)) return false

        val newUser = User(
            id = UUID.randomUUID().toString(),
            name = name,
            username = username,
            password = password,
            email = email,
            country = country,
            city = city,
            isActive = true,
            role = Role.USER
        )
        _users.add(newUser)
        return true
    }

    /**
     * Updates an existing user by matching its ID.
     * Replaces the old instance with the new one, preserving the list order.
     * @return true if the user was found and updated, false otherwise.
     */
    fun updateUser(updatedUser: User): Boolean {
        val index = _users.indexOfFirst { it.id == updatedUser.id }
        return if (index != -1) {
            _users[index] = updatedUser
            true
        } else {
            false
        }
    }

    /**
     * Updates the status of a specific place for a given user.
     * @return true if the place was found and updated, false otherwise.
     */
    fun updatePlaceStatus(userId: String, placeId: String, updatedPlace: Place): Boolean {
        val userIndex = _users.indexOfFirst { it.id == userId }
        if (userIndex == -1) return false

        val user = _users[userIndex]
        val updatedPlaces = user.places.map { place ->
            if (place.id == placeId) updatedPlace else place
        }

        _users[userIndex] = user.copy(places = updatedPlaces as MutableList<Place>)
        return true
    }

    /**
     * Removes all users from the repository.
     * (Useful for testing or resetting data.)
     */
    fun clear() {
        _users.clear()
    }
}
