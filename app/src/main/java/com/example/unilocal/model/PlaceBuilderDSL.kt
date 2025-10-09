package com.example.unilocal.model

import java.util.UUID

class PlaceBuilderDSL {
    var id: String = UUID.randomUUID().toString()
    var name: String = ""
    var description: String = ""
    var category: PlaceCategory = PlaceCategory.RESTAURANT
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var phone: String = ""
    var status: PlaceStatus = PlaceStatus.PENDING
    var avgRating: Double = 0.0
    var images: List<String> = emptyList()
    var owner: User = dummyUser()
    var reviews: List<Review> = emptyList()
    var schedules: List<Schedule> = emptyList()
    var motive: String = ""

    fun build(): Place {
        return Place(
            id = id,
            name = name,
            description = description,
            category = category,
            latitude = latitude,
            longitude = longitude,
            phone = phone,
            status = status,
            avgRating = avgRating,
            images = images,
            owner = owner,
            reviews = reviews,
            schedules = schedules,
            motive = motive
        )
    }

    private fun dummyUser(): User = User(
        id = "user-default",
        name = "Anonymous",
        username = "anon",
        password = "password",
        email = "anon@example.com",
        country = "Unknown",
        city = "Nowhere",
        isActive = true,
        role = Role.USER
    )
}

fun buildPlace(init: PlaceBuilderDSL.() -> Unit): Place {
    val builder = PlaceBuilderDSL()
    builder.init()
    return builder.build()
}