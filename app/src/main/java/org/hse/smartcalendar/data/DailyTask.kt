package org.hse.smartcalendar.data

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

class DailyTask (
    private val id: UUID = UUID.randomUUID(),
    private var title : String,
    private var isComplete: Boolean = false,
    private var type: DailyTaskType = DailyTaskType.COMMON,
    private val creationTime : LocalDateTime =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    private var description : String,
    private var start : LocalTime,
    private var end: LocalTime,
    //private var date: LocalDate,
    ) {
    constructor(
        title: String,
        isComplete: Boolean,
        type: String,
        description: String,
        start: Pair<Int, Int>,
        end: Pair<Int, Int>,
        //day: Int, month: Int, year: Int
    ) :
            this(
                title = title,
                isComplete = isComplete,
                type = DailyTaskType.fromString(type),
                description = description,
                start = LocalTime(hour = start.first, minute = start.second),
                end = LocalTime(hour = end.first, minute = end.second),
                //date = LocalDate(day, month, year)
            ) {
    }
    init {
        if (title.isEmpty()) {
            throw EmptyTitleException()
        }
        if (start > end) {
            throw TimeConflictException(start, end)
        }
    }

    fun getId(): UUID {
        return id
    }

    fun isComplete(): Boolean {
        return isComplete
    }

    fun setCompletion(status: Boolean) {
        isComplete = status
    }

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

    class TimeConflictException(start: LocalTime, end: LocalTime) : IllegalArgumentException(
        "Illegal start and end time: start = " +
                start.toString() +
                " less then end = " +
                end.toString()
    )

    class EmptyTitleException : IllegalArgumentException(
        "Illegal task title: title is empty"
    )
}