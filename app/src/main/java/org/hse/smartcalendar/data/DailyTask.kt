package org.hse.smartcalendar.data

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

// TODO Написать нексолько утилитарных функций для работы с минутами.
fun LocalTime.Companion.fromMinutesOfDay(minutes: Int): LocalTime {
    return fromSecondOfDay(minutes * 60)
}

fun LocalTime.Companion.toMinutesOfDay(time : LocalTime): Int {
    return time.hour * 60 + time.minute
}

class DailyTask (
    private var title : String,
    private val creationTime : LocalDateTime =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    private var description : String,
    private var start : LocalTime,
    private var duration : LocalTime,
    ) {

    fun getDailyTaskEndTime() : LocalTime {
        return LocalTime.fromSecondOfDay(
            start.toSecondOfDay() + duration.toSecondOfDay()
        )
    }

    fun getDailyTaskTitle() : String {
        return title
    }

    fun getDailyTaskDescription() : String {
        return description
    }

    fun getDailyTaskStartTime() : LocalTime {
        return start
    }

    fun getDailyTaskSchedule() : String {
        return getDailyTaskStartTime().toString() +
                " due to " + getDailyTaskEndTime().toString()
    }

    fun isNestedTasks(task : DailyTask) : Boolean {
        return !(this.getDailyTaskStartTime() >= task.getDailyTaskEndTime() ||
                task.getDailyTaskStartTime() >= this.getDailyTaskEndTime())
    }
}