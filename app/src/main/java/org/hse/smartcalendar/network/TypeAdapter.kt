import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTaskType
import java.time.OffsetDateTime

class LocalTimeAdapter : TypeAdapter<LocalTime>() {
    override fun write(out: JsonWriter, value: LocalTime) {
        out.value(value.toString())
    }

    override fun read(reader: JsonReader): LocalTime {
        return LocalTime.parse(reader.nextString())
    }
}
class LocalDateAdapter : TypeAdapter<LocalDate>() {
    override fun write(out: JsonWriter, value: LocalDate) {
        out.value(value.toString())
    }

    override fun read(reader: JsonReader): LocalDate {
        return LocalDate.parse(reader.nextString())
    }
}
class LocalDateTimeAdapter : TypeAdapter<LocalDateTime>() {
    override fun write(out: JsonWriter, value: LocalDateTime) {
        out.value(value.toString())
    }

    override fun read(reader: JsonReader): LocalDateTime {
        return LocalDateTime.parse(reader.nextString())
    }
}

class DailyTaskTypeAdapter : TypeAdapter<DailyTaskType>() {
    override fun write(out: JsonWriter, value: DailyTaskType) {
        out.value(value.name)
    }

    override fun read(reader: JsonReader): DailyTaskType {
        return DailyTaskType.fromString(reader.nextString().uppercase())
    }
}
class OffsetDateTimeAdapter:  TypeAdapter<OffsetDateTime>() {
    override fun write(out: JsonWriter, value: OffsetDateTime) {
        out.value(value.toString())
    }
    override fun read(reader: JsonReader): OffsetDateTime {
        return OffsetDateTime.parse(reader.nextString())
    }
}
class InstantTimeAdapter: TypeAdapter<Instant>() {
    override fun write(out: JsonWriter, value: Instant) {
        out.value(value.toString())  // ISO-8601 with Z
    }
    override fun read(reader: JsonReader): Instant {
        return Instant.parse(reader.nextString())
    }
}