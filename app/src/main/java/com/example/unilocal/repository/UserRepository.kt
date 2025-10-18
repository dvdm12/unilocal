package com.example.unilocal.repository

import com.example.unilocal.model.Role
import com.example.unilocal.model.User
import java.util.UUID

/**
 * In-memory repository that manages application users.
 *
 * Responsibilities:
 * - Stores simulated default users.
 * - Allows registration and updates of user data.
 * - Provides user search and validation utilities.
 *
 * ❌ This class no longer manages Place operations.
 * ✅ All place management is delegated to PlaceRepository or coordinated from UserViewModel.
 */
object UserRepository {

    // -------------------------------------------------------------------------
    // 🔹 INTERNAL STATE
    // -------------------------------------------------------------------------

    /** Internal list of users stored in memory. */
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

    /** Public immutable view of users. */
    val users: List<User>
        get() = _users.toList()

    // -------------------------------------------------------------------------
    // 🔹 READ OPERATIONS
    // -------------------------------------------------------------------------

    /** Returns all users (used mainly by moderators). */
    fun getAllUsers(): List<User> = _users.toList()

    /** Finds a user by credentials if active. */
    fun findUser(email: String, password: String): User? =
        _users.find { it.email == email && it.password == password && it.isActive }

    /** Checks if an email is already registered. */
    fun userExists(email: String): Boolean =
        _users.any { it.email.equals(email, ignoreCase = true) }

    /** Finds a user by ID. */
    fun getUserById(userId: String): User? =
        _users.find { it.id == userId }

    // -------------------------------------------------------------------------
    // 🔹 CREATION
    // -------------------------------------------------------------------------

    /**
     * Registers a new user.
     * @return true if successful, false if email already exists.
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

    // -------------------------------------------------------------------------
    // 🔹 UPDATE OPERATIONS
    // -------------------------------------------------------------------------

    /**
     * Updates an existing user by ID.
     * @return true if updated, false otherwise.
     */
    fun updateUser(updatedUser: User): Boolean {
        val index = _users.indexOfFirst { it.id == updatedUser.id }
        return if (index != -1) {
            _users[index] = updatedUser
            true
        } else false
    }

    // -------------------------------------------------------------------------
    // 🔹 DELETE OPERATIONS
    // -------------------------------------------------------------------------

    /**
     * Removes a user by ID.
     * @return true if removed, false otherwise.
     */
    fun removeUser(userId: String): Boolean {
        val index = _users.indexOfFirst { it.id == userId }
        if (index == -1) return false
        _users.removeAt(index)
        return true
    }

    /** Clears all users (useful for testing or resets). */
    fun clear() {
        _users.clear()
    }
}
