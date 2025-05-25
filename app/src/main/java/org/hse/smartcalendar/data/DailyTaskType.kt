package org.hse.smartcalendar.data

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import org.hse.smartcalendar.network.DailyTaskTypeSerializer

@Serializable(with = DailyTaskTypeSerializer::class)
enum class DailyTaskType(private val printName: String, val color: Color) {
    COMMON("Common", Color.LightGray),
    FITNESS("Fitness", Color.Red),
    WORK("Work", Color.DarkGray),
    STUDIES("Studies", Color.Green);

    @Override
    override fun toString(): String {
        return printName
    }
    companion object {
        fun fromString(type: String): DailyTaskType {
            return entries.firstOrNull { it.printName.equals(type, ignoreCase = true) }
                ?: COMMON
        }
    }
}
