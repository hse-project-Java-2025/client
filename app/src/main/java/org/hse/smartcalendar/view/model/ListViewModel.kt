package org.hse.smartcalendar.view.model

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.hse.smartcalendar.data.DailySchedule
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.User

class ListViewModel(id: Long) : ViewModel() {
    private var dailyTaskSchedule: DailySchedule
    private var dailyScheduleDate = mutableStateOf(
        Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
    )
    var dailyTaskList: SnapshotStateList<DailyTask>
    private val user: User = User(id)

    init {
        val date: LocalDate =
            Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
        dailyTaskSchedule = user.getSchedule().getOrCreateDailySchedule(date)
        dailyTaskList = SnapshotStateList(dailyTaskSchedule.getDailyTaskList())

    }

    fun addDailyTask(newTask : DailyTask) {
        try {
            dailyTaskSchedule.addDailyTask(newTask)
        } catch (exception: DailySchedule.NestedTaskException) {
            throw exception
        }
        dailyTaskList.add(newTask)
        dailyTaskList.sortBy { task ->
            task.getDailyTaskStartTime()
        }
    }

    fun removeDailyTask(task : DailyTask) {
        if (!dailyTaskSchedule.removeDailyTask(task)) {
            // TODO
        } else {
            dailyTaskList.remove(task)
        }
    }

    fun changeTaskCompletion(task: DailyTask, status: Boolean) {
        if (dailyTaskSchedule.setCompletionById(task.getId(), status)) {
            // TODO Может быть придется обновлять список.
            task.setCompletion(status)
        }
    }

    fun changeDailyTaskSchedule(date: LocalDate): Unit {
        dailyTaskSchedule = user.getSchedule().getOrCreateDailySchedule(date)
        dailyScheduleDate.value = dailyTaskSchedule.date
        dailyTaskList.clear()
        dailyTaskSchedule.getDailyTaskList().forEach { task ->
            dailyTaskList.add(task)
        }
    }

    fun moveToNextDailySchedule(): Unit {
        val date: LocalDate = dailyTaskSchedule.date
        val nextDate: LocalDate = date.plus(
            DatePeriod(
                days = 1
            )
        )
        changeDailyTaskSchedule(nextDate)
    }

    fun moveToPreviousDailySchedule(): Unit {
        val date: LocalDate = dailyTaskSchedule.date
        val nextDate: LocalDate = date.minus(
            DatePeriod(
                days = 1
            )
        )
        changeDailyTaskSchedule(nextDate)
    }

    fun getScheduleDate(): LocalDate {
        return dailyScheduleDate.value
    }

    private fun <T> SnapshotStateList(dailyTaskList: List<T>): SnapshotStateList<T> {
        val result: SnapshotStateList<T> = SnapshotStateList()
        dailyTaskList.forEach { task ->
            result.add(task)
        }
        return result
    }
}