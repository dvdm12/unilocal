package com.example.unilocal.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val username: String,
    val password: String,
    val email: String,
    val country: String,
    val city: String,
    val isActive: Boolean,
    val role: Role
)

@Serializable
enum class Role {
    USER,
    MODERATOR
}
