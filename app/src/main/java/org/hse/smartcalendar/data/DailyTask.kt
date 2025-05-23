package org.hse.smartcalendar.data

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
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
    private var date: LocalDate,
    ) {
    companion object DefaultDate{//затычка, можешь убрать
        val date = LocalDate.fromEpochDays((Clock.System.now().toEpochMilliseconds()/1000/60/60/24).toInt())
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

    fun setDailyTaskTitle(newTitle: String) {
        title = newTitle
    }

    fun setDailyTaskStartTime(newStart: LocalTime) {
        start = newStart
    }

    fun setDailyTaskEndTime(newEnd: LocalTime) {
        end = newEnd
    }

    fun getDailyTaskType(): DailyTaskType {
        return type
    }

    fun getDailyTaskDescription() : String {
        return description
    }
    fun getDailyTaskCreationTime() : LocalDateTime {
        return creationTime
    }
    fun getTaskDate() : LocalDate {
        return date
    }

    fun setDailyTaskType(newType: DailyTaskType) {
        type = newType
    }

    fun setDailyTaskDescription(newDescription: String) {
        description = newDescription
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

    fun updateDailyTask(task: DailyTask) {
        title = task.title
        type = task.type
        description = task.description
        start = task.start
        end = task.end
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