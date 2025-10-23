package com.example.unilocal.repository

import com.example.unilocal.model.Place
import com.example.unilocal.model.Role
import com.example.unilocal.model.User
import java.util.UUID

object UserRepository : IUserRepository {

    // -------------------------------------------------------------------------
    // INTERNAL STATE
    // -------------------------------------------------------------------------

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

    private var _sessionUser: User? = null

    val users: List<User>
        get() = _users.toList()

    // -------------------------------------------------------------------------
    // SESSION MANAGEMENT
    // -------------------------------------------------------------------------

    override fun setSessionUser(user: User) {
        _sessionUser = _users.find { it.id == user.id } ?: user
    }

    override fun getSessionUser(): User? = _sessionUser

    override fun clearSessionUser() {
        _sessionUser = null
    }

    // -------------------------------------------------------------------------
    // READ OPERATIONS
    // -------------------------------------------------------------------------

    override fun getAllUsers(): List<User> = _users.toList()

    override fun findUser(email: String, password: String): User? =
        _users.find { it.email == email && it.password == password && it.isActive }

    override fun userExists(email: String): Boolean =
        _users.any { it.email.equals(email, ignoreCase = true) }

    override fun getUserById(userId: String): User? =
        _users.find { it.id == userId }

    // -------------------------------------------------------------------------
    // CREATION
    // -------------------------------------------------------------------------

    override fun registerUser(
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

    override fun registerUser(
        id: String,
        name: String,
        username: String,
        password: String,
        email: String,
        country: String,
        city: String
    ): Boolean {
        if (userExists(email)) return false
        val newUser = User(
            id = id,
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
    // UPDATE OPERATIONS
    // -------------------------------------------------------------------------

    override fun updateUser(updatedUser: User): Boolean {
        val index = _users.indexOfFirst { it.id == updatedUser.id }
        if (index == -1) return false
        val existingUser = _users[index]
        if (!existingUser.isActive) return false

        val mergedUser = existingUser.copy(
            name = updatedUser.name.ifBlank { existingUser.name },
            username = updatedUser.username.ifBlank { existingUser.username },
            password = if (updatedUser.password.isNotBlank() &&
                updatedUser.password != existingUser.password
            ) updatedUser.password else existingUser.password,
            email = updatedUser.email.ifBlank { existingUser.email },
            country = updatedUser.country.ifBlank { existingUser.country },
            city = updatedUser.city.ifBlank { existingUser.city },
            role = existingUser.role,
            isActive = true,
            places = existingUser.places // preserva la referencia
        )

        _users[index] = mergedUser
        if (_sessionUser?.id == mergedUser.id) _sessionUser = mergedUser
        return true
    }

    // -------------------------------------------------------------------------
    // ACCOUNT STATE MANAGEMENT
    // -------------------------------------------------------------------------

    override fun deactivateUser(userId: String): Boolean {
        val index = _users.indexOfFirst { it.id == userId }
        if (index == -1) return false
        val existingUser = _users[index]
        if (!existingUser.isActive) return false

        val updated = existingUser.copy(isActive = false, places = existingUser.places)
        _users[index] = updated
        if (_sessionUser?.id == userId) _sessionUser = updated
        return true
    }

    override fun activateUser(userId: String): Boolean {
        val index = _users.indexOfFirst { it.id == userId }
        if (index == -1) return false
        val existingUser = _users[index]
        if (existingUser.isActive) return false

        val updated = existingUser.copy(isActive = true, places = existingUser.places)
        _users[index] = updated
        if (_sessionUser?.id == userId) _sessionUser = updated
        return true
    }

    // -------------------------------------------------------------------------
    // PLACE RELATIONSHIP MANAGEMENT
    // -------------------------------------------------------------------------

    override fun addPlaceToUser(userId: String, place: Place): Boolean {
        val index = _users.indexOfFirst { it.id == userId }
        if (index == -1) return false

        val user = _users[index]
        if (!user.isActive) return false
        if (user.places.any { it.id == place.id }) return false

        // 🔹 Rompe la referencia circular eliminando el owner al almacenar
        val consistentPlace = place.copy(owner = null)

        // 🔹 Agrega directamente sobre la lista mutable real
        user.places.add(consistentPlace)

        // 🔹 Actualiza el usuario sin recrear la lista
        _users[index] = user.copy(places = user.places)

        // 🔹 Sincroniza sesión si corresponde
        if (_sessionUser?.id == userId) _sessionUser = _users[index]

        return true
    }


    override fun removePlaceFromUser(userId: String, placeId: String): Boolean {
        val index = _users.indexOfFirst { it.id == userId }
        if (index == -1) return false
        val user = _users[index]
        if (!user.isActive) return false

        val removed = user.places.removeIf { it.id == placeId }
        if (!removed) return false

        _users[index] = user.copy(places = user.places)
        if (_sessionUser?.id == userId) _sessionUser = _users[index]
        return true
    }

    override fun updateUserPlace(userId: String, updatedPlace: Place): Boolean {
        val index = _users.indexOfFirst { it.id == userId }
        if (index == -1) return false
        val user = _users[index]
        if (!user.isActive) return false

        val placeIndex = user.places.indexOfFirst { it.id == updatedPlace.id }
        if (placeIndex == -1) return false

        user.places[placeIndex] =
            if (updatedPlace.owner?.id == userId)
                updatedPlace
            else
                updatedPlace.copy(owner = user)

        _users[index] = user.copy(places = user.places)
        if (_sessionUser?.id == userId) _sessionUser = _users[index]
        return true
    }

    // -------------------------------------------------------------------------
    // DELETE OPERATIONS
    // -------------------------------------------------------------------------

    override fun removeUser(userId: String): Boolean {
        val index = _users.indexOfFirst { it.id == userId }
        if (index == -1) return false

        _users[index].places.clear()
        _users.removeAt(index)
        if (_sessionUser?.id == userId) _sessionUser = null
        return true
    }

    override fun clear() {
        _users.clear()
        _sessionUser = null
    }
}
