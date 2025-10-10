package com.example.unilocal.model

import kotlinx.serialization.Serializable
import java.time.LocalTime

/**
 * Represents a weekly schedule for a place or service.
 * @property dayStart The starting day of the schedule (1 = Monday, 7 = Sunday)
 * @property dayEnd The ending day of the schedule (1 = Monday, 7 = Sunday)
 * @property start The opening time (serialized as "HH:mm")
 * @property end The closing time (serialized as "HH:mm")
 */
@Serializable
data class Schedule(
    val dayStart: Int, // 1 (Monday) .. 7 (Sunday)
    val dayEnd: Int, // 1 (Monday) .. 7 (Sunday)
    @Serializable(with = LocalTimeSerializer::class)
    val start: LocalTime, // Opening time, serialized as "HH:mm"
    @Serializable(with = LocalTimeSerializer::class)
    val end: LocalTime // Closing time, serialized as "HH:mm"
)
