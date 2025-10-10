package com.example.unilocal.model

import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class Favorite(
    val id: String,
    @Serializable(with = LocalTimeSerializer::class)
    val createdAt: LocalTime,
    val user: User,
    val place: Place
)
