package com.example.unilocal.model

data class Review(
    val id: String,
    val rating: Int, // 1..5
    val comment: String,
    val createdAt: String,
    val place: Place,
    val author: User
)
