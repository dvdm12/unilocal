package com.example.unilocal.repository

import com.example.unilocal.model.Place
import com.example.unilocal.model.User

/**
 * Interface that defines the contract for user data repositories.
 *
 * Provides a consistent abstraction for multiple data sources such as:
 * - In-memory (local simulation)
 * - Remote APIs (REST)
 * - Cloud databases (e.g., Firebase)
 *
 * Ensures unified behavior for session management, CRUD operations,
 * logical activation/deactivation, and user–place relationships.
 */
interface IUserRepository {

    // -------------------------------------------------------------------------
    // SESSION MANAGEMENT
    // -------------------------------------------------------------------------

    /**
     * Sets the user currently associated with the in-memory session.
     * This does not modify the logical activation state of the account.
     */
    fun setSessionUser(user: User)

    /** Returns the current in-memory session user, if any. */
    fun getSessionUser(): User?

    /** Clears the session user reference. */
    fun clearSessionUser()

    // -------------------------------------------------------------------------
    // READ OPERATIONS
    // -------------------------------------------------------------------------

    /** Returns all users in the repository. */
    fun getAllUsers(): List<User>

    /**
     * Finds an active user by email and password.
     * Only users with isActive = true are considered.
     */
    fun findUser(email: String, password: String): User?

    /** Checks whether a user with the given email already exists. */
    fun userExists(email: String): Boolean

    /** Retrieves a user by their unique ID. */
    fun getUserById(userId: String): User?

    // -------------------------------------------------------------------------
    // CREATION
    // -------------------------------------------------------------------------

    /**
     * Registers a new user with the given parameters.
     * @return true if registration was successful, false if the email already exists.
     */
    fun registerUser(
        name: String,
        username: String,
        password: String,
        email: String,
        country: String,
        city: String
    ): Boolean

    /**
     * Registers a new user with the given parameters.
     * @return true if registration was successful, false if the email already exists.
     */
    fun registerUser(
        id:String,
        name: String,
        username: String,
        password: String,
        email: String,
        country: String,
        city: String
    ): Boolean

    // -------------------------------------------------------------------------
    // UPDATE OPERATIONS
    // -------------------------------------------------------------------------

    /**
     * Updates the user's editable information (name, username, password, email, etc.)
     * while preserving immutable fields such as `role`, `isActive`, and `places`.
     * @return true if the update was successful, false otherwise.
     */
    fun updateUser(updatedUser: User): Boolean

    // -------------------------------------------------------------------------
    // ACCOUNT STATE MANAGEMENT
    // -------------------------------------------------------------------------

    /**
     * Deactivates a user account (logical disable).
     * The user will remain in the repository but cannot log in until reactivated.
     * @return true if successfully deactivated, false otherwise.
     */
    fun deactivateUser(userId: String): Boolean

    /**
     * Reactivates a previously disabled user account.
     * @return true if successfully reactivated, false otherwise.
     */
    fun activateUser(userId: String): Boolean

    // -------------------------------------------------------------------------
    // PLACE RELATIONSHIP MANAGEMENT
    // -------------------------------------------------------------------------

    /**
     * Adds a new [Place] to the user's list of places.
     * Prevents duplicates and synchronizes with the current session.
     * @return true if added successfully, false otherwise.
     */
    fun addPlaceToUser(userId: String, place: Place): Boolean

    /**
     * Removes a [Place] from the user's list by its ID.
     * @return true if removed successfully, false otherwise.
     */
    fun removePlaceFromUser(userId: String, placeId: String): Boolean

    /**
     * Updates an existing [Place] inside the user's list.
     * @return true if updated successfully, false otherwise.
     */
    fun updateUserPlace(userId: String, updatedPlace: Place): Boolean

    // -------------------------------------------------------------------------
    // DELETE OPERATIONS
    // -------------------------------------------------------------------------

    /**
     * Permanently removes a user from the repository.
     * If the removed user is the one in session, the session must be cleared.
     * @return true if removed successfully, false otherwise.
     */
    fun removeUser(userId: String): Boolean

    /** Clears all users and resets the session (used for testing or resets). */
    fun clear()
}
