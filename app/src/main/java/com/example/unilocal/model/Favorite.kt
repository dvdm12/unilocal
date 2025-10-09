package com.example.unilocal.model

import java.time.LocalTime

data class Favorite(
    val id: String,
    val createdAt: LocalTime,
    val user: User,
    val place: Place
)
