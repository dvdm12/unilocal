package com.example.unilocal.model

data class Place(
    val id: String,
    val name: String,
    val description: String,
    val category: PlaceCategory,
    val latitude: Double,
    val longitude: Double,
    val phone: String,
    val status: PlaceStatus,
    val avgRating: Double,
    val images: List<String>,
    val owner: User,
    val reviews: List<Review> = emptyList(),
    val schedules: List<Schedule> = emptyList(),
    val motive: String
)

enum class PlaceCategory {
    RESTAURANT,
    CAFE,
    MUSEUM,
    STORE,
    THEATER
}

enum class PlaceStatus {
    PENDING,
    APPROVED,
    REJECTED
}
