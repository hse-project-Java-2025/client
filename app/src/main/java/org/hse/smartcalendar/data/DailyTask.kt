package org.hse.smartcalendar.data

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DailyTask (
    private var title : String,
    private var type: DailyTaskType = DailyTaskType.COMMON,
    private val creationTime : LocalDateTime =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    private var description : String,
    private var start : LocalTime,
    private var end: LocalTime,
    ) {

    fun getDailyTaskEndTime() : LocalTime {
        return end
    }

    fun getDailyTaskTitle() : String {
        return title
    }

    fun getDailyTaskType(): DailyTaskType {
        return type
    }

    fun getDailyTaskDescription() : String {
        return description
    }

    fun getDailyTaskStartTime() : LocalTime {
        return start
    }

    fun getDailyTaskArrangementString() : String {
        return getDailyTaskStartTime().toString() +
                " due to " + getDailyTaskEndTime().toString()
    }

    fun isNestedTasks(task : DailyTask) : Boolean {
        return !(this.getDailyTaskStartTime() >= task.getDailyTaskEndTime() ||
                task.getDailyTaskStartTime() >= this.getDailyTaskEndTime())
    }
}