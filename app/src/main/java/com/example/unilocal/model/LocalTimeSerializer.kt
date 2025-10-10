package com.example.unilocal.model

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalTime

/**
 * Custom serializer for LocalTime to support kotlinx.serialization.
 * Serializes LocalTime as a string in "HH:mm" format.
 */
object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeString(value.toString()) // Serialize as "HH:mm:ss" or "HH:mm"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(decoder: Decoder): LocalTime {
        return LocalTime.parse(decoder.decodeString()) // Parse from string
    }
}

