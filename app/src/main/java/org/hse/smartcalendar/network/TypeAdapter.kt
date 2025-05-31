import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTaskType

class LocalTimeAdapter : TypeAdapter<LocalTime>() {
    override fun write(out: JsonWriter, value: LocalTime) {
        out.value(value.toString()) // сохранит "08:30:00"
    }

    override fun read(reader: JsonReader): LocalTime {
        return LocalTime.parse(reader.nextString()) // парсит ISO-время
    }
}
class LocalDateAdapter : TypeAdapter<LocalDate>() {
    override fun write(out: JsonWriter, value: LocalDate) {
        out.value(value.toString()) // "2025-05-24"
    }

    override fun read(reader: JsonReader): LocalDate {
        return LocalDate.parse(reader.nextString())
    }
}
class LocalDateTimeAdapter : TypeAdapter<LocalDateTime>() {
    override fun write(out: JsonWriter, value: LocalDateTime) {
        out.value(value.toString()) // "2025-05-24T08:30"
    }

    override fun read(reader: JsonReader): LocalDateTime {
        return LocalDateTime.parse(reader.nextString())
    }
}

class DailyTaskTypeAdapter : TypeAdapter<DailyTaskType>() {
    override fun write(out: JsonWriter, value: DailyTaskType) {
        out.value(value.toString()) // сериализуем как printName
    }

    override fun read(reader: JsonReader): DailyTaskType {
        return DailyTaskType.fromString(reader.nextString()) // десериализация по printName
    }
}