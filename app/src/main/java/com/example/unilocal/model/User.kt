package com.example.unilocal.model

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

enum class Role {
    USER,
    MODERATOR
}
