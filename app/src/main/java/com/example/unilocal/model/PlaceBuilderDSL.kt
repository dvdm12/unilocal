package com.example.unilocal.model

import java.net.URL
import java.util.UUID

/**
 * DSL builder for creating [Place] objects fluently.
 */
class PlaceBuilderDSL {
    var id: String = UUID.randomUUID().toString()
    var name: String = ""
    var description: String = ""
    var category: PlaceCategory = PlaceCategory.RESTAURANT
    var address: String = ""
    var phone: String = ""
    var status: PlaceStatus = PlaceStatus.PENDING
    var avgRating: Double = 0.0
    var images: List<String> = emptyList()
    var owner: User? = null // 🔹 ahora opcional (coincide con Place.kt)
    var reviews: List<Review> = emptyList()
    var schedules: List<Schedule> = emptyList()
    var motive: String = ""

    /**
     * Allows adding image URLs directly as [URL] objects.
     */
    fun setImageUrls(urls: List<URL>) {
        images = urls.map { it.toString() }
    }

    fun build(): Place {
        return Place(
            id = id,
            name = name,
            description = description,
            category = category,
            address = address,
            phone = phone,
            status = status,
            avgRating = avgRating,
            images = images,
            owner = owner, // 🔹 puede ser null sin problema
            reviews = reviews,
            schedules = schedules,
            motive = motive
        )
    }
}

/**
 * Entry point for building a [Place] using Kotlin DSL style.
 */
fun buildPlace(init: PlaceBuilderDSL.() -> Unit): Place {
    val builder = PlaceBuilderDSL()
    builder.init()
    return builder.build()
}
