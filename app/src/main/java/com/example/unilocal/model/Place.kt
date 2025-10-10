package com.example.unilocal.model

import kotlinx.serialization.Serializable
import java.net.URL

@Serializable
/**
 * Represents a place with all its details, including owner, reviews, schedules, and images.
 *
 * @property id Unique identifier for the place
 * @property name Name of the place
 * @property description Description of the place
 * @property category Category of the place (e.g., RESTAURANT, CAFE)
 * @property address Address of the place
 * @property phone Contact phone number
 * @property status Approval status of the place
 * @property avgRating Average rating of the place
 * @property images List of image URLs as strings
 * @property owner User who owns the place
 * @property reviews List of reviews for the place
 * @property schedules List of schedules for the place
 * @property motive Motive for approval/rejection
 */
data class Place(
    val id: String,
    val name: String,
    val description: String,
    val category: PlaceCategory,
    val address: String,
    val phone: String,
    val status: PlaceStatus,
    val avgRating: Double,
    val images: List<String>,
    val owner: User,
    val reviews: List<Review> = emptyList(),
    val schedules: List<Schedule> = emptyList(),
    val motive: String
) {
    /**
     * Returns a list of valid image URLs parsed from the images property.
     * This property is not serialized and is always computed from images.
     */
    val imageUrls: List<URL>
        get() = images.mapNotNull { runCatching { URL(it) }.getOrNull() }
}

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
