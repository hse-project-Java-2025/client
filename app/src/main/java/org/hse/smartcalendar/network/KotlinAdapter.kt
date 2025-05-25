package org.hse.smartcalendar.network

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.hse.smartcalendar.data.DailyTaskType


object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString())
    }
}
object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.toString()) // default ISO-8601 format: yyyy-MM-dd
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }
}
object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeString(value.toString()) // default ISO-8601 format: HH:mm:ss[.nnnnnnnnn]
    }

    override fun deserialize(decoder: Decoder): LocalTime {
        return LocalTime.parse(decoder.decodeString())
    }
}
object DailyTaskTypeSerializer : KSerializer<DailyTaskType> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("DailyTaskType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: DailyTaskType) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): DailyTaskType {
        val name = decoder.decodeString()
        return try {
            DailyTaskType.valueOf(name.uppercase())
        } catch (e: IllegalArgumentException) {
            DailyTaskType.COMMON // fallback
        }
    }
}