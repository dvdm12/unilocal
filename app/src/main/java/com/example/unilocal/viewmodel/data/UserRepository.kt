package com.example.unilocal.viewmodel.data

import com.example.unilocal.model.Role // Import Role enum for user roles
import com.example.unilocal.model.User // Import User data class
import java.util.UUID // Import UUID for unique user IDs

/**
 * In-memory repository that manages application users.
 *
 * Responsibilities:
 * - Stores default simulated users.
 * - Allows registration of new users.
 * - Provides search and validation methods.
 */
object UserRepository {

    // Internal mutable list of users (in memory)
    private val _users = mutableListOf(
        User(
            id = "1", // Unique user ID
            name = "Juan Pérez", // User's full name
            username = "juanp", // Username for login
            password = "123456", // User's password
            email = "juan@correo.com", // User's email address
            country = "Colombia", // User's country
            city = "Bogotá", // User's city
            isActive = true, // Whether the user is active
            role = Role.USER // User's role
        ),
        User(
            id = "2", // Unique user ID
            name = "Laura Admin", // User's full name
            username = "lauraadmin", // Username for login
            password = "adminpass", // User's password
            email = "laura@admin.com", // User's email address
            country = "Colombia", // User's country
            city = "Medellín", // User's city
            isActive = true, // Whether the user is active
            role = Role.MODERATOR // User's role
        )
    )

    // Returns all current users as an immutable list
    val users: List<User>
        get() = _users.toList() // Expose as immutable

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
        name: String, // New user's name
        username: String, // New user's username
        password: String, // New user's password
        email: String, // New user's email
        country: String, // New user's country
        city: String // New user's city
    ): Boolean {
        if (userExists(email)) return false // Prevent duplicate email

        val newUser = User(
            id = UUID.randomUUID().toString(), // Generate unique ID
            name = name, // Assign name
            username = username, // Assign username
            password = password, // Assign password
            email = email, // Assign email
            country = country, // Assign country
            city = city, // Assign city
            isActive = true, // Set user as active
            role = Role.USER // Default role is USER
        )
        _users.add(newUser) // Add new user to list
        return true // Indicate success
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

    // Clears all users (for testing or debugging purposes)
    fun clear() {
        _users.clear() // Remove all users from list
    }
}
