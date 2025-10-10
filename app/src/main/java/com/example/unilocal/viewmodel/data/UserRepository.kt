package com.example.unilocal.viewmodel.data

import com.example.unilocal.model.Role
import com.example.unilocal.model.User
import java.util.UUID

/**
 * Repositorio en memoria que gestiona los usuarios de la aplicación.
 * - Guarda usuarios simulados por defecto.
 * - Permite registrar nuevos usuarios.
 * - Ofrece métodos de búsqueda y validación.
 */
object UserRepository {

    /** Lista interna mutable de usuarios (en memoria). */
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

    /** Devuelve todos los usuarios actuales. */
    val users: List<User>
        get() = _users.toList() // inmutable para afuera

    /** Busca un usuario por email y contraseña. */
    fun findUser(email: String, password: String): User? =
        _users.find { it.email == email && it.password == password && it.isActive }

    /** Verifica si ya existe un usuario con el email dado. */
    fun userExists(email: String): Boolean =
        _users.any { it.email.equals(email, ignoreCase = true) }

    /**
     * Registra un nuevo usuario y lo agrega al repositorio.
     * Retorna true si fue agregado exitosamente, false si el email ya estaba en uso.
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

    /** Limpia todos los usuarios (solo para pruebas o depuración). */
    fun clear() {
        _users.clear()
    }
}
