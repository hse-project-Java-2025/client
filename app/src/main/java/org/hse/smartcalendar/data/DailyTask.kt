package org.hse.smartcalendar.data

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import org.hse.smartcalendar.utility.TimeUtils
import org.hse.smartcalendar.utility.UUIDSerializer
import java.util.UUID

@Serializable
data class DailyTask (
    @Serializable(with = UUIDSerializer::class)
    private var id: UUID = UUID.randomUUID(),
    private var title : String,
    private var isComplete: Boolean = false,
    private var type: DailyTaskType = DailyTaskType.COMMON,//Int
    private val creationTime : LocalDateTime =//пока не нужен
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    private var description : String,
    private var start : LocalTime,//hour, minute
    private var end: LocalTime,//hour, minute
    private var date: LocalDate,//year, month, day
    ) {
    companion object {//затычка, можешь убрать
        val defaultDate = TimeUtils.getCurrentDateTime().date
        fun fromTime(start: LocalTime, end: LocalTime, date: LocalDate): DailyTask{
            return fromTimeAndType(start,end,date, DailyTaskType.COMMON)
        }
        fun fromTimeAndType(start: LocalTime, end: LocalTime, date: LocalDate, type: DailyTaskType): DailyTask{
            return DailyTask(
                title = "Task",
                description = "Description",
                end = end,
                start = start,
                date = date,
                type = type
            )
        }
    }
    init {
        if (title.isEmpty()) {
            throw EmptyTitleException()
        }
        if (start > end) {
            throw TimeConflictException(start, end)
        }
    }
    fun setId(newId: UUID){
        id = newId
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
    fun belongsCurrentDay(): Boolean{
        return date== TimeUtils.getCurrentDateTime().date
    }
    fun belongsCurrentWeek(): Boolean{
        val diff =TimeUtils.getCurrentDateTime().date.toEpochDays() - date.toEpochDays();
        return diff < 7 && diff>=0
    }
    fun getMinutesLength(): Int{
        return (end.toSecondOfDay()-start.toSecondOfDay())/60
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