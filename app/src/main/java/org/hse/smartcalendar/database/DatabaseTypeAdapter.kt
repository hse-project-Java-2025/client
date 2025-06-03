package org.hse.smartcalendar.database

import org.hse.smartcalendar.data.DailyTask
import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object DatabaseTypeAdapter {

    @TypeConverter
    fun fromDailyTask(task: DailyTask): String {
        return Json.encodeToString(task)
    }

    @TypeConverter
    fun toDailyTask(json: String): DailyTask {
        return Json.decodeFromString(json)
    }
}