package com.example.unilocal.model

import java.time.LocalTime

data class Schedule(
    val dayStart: Int, // 1 (Lunes) .. 7 (Domingo)
    val dayEnd: Int,
    val start: LocalTime, // formato "HH:mm"
    val end: LocalTime
)
