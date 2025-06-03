package org.hse.smartcalendar.view.model

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import org.hse.smartcalendar.data.DailySchedule
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskAction
import org.hse.smartcalendar.data.User
import org.hse.smartcalendar.data.WorkManagerHolder
import org.hse.smartcalendar.database.PendingTaskActionRepository
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.notification.TaskApiWorker
import org.hse.smartcalendar.repository.TaskRepository
import java.io.File
import java.util.concurrent.TimeUnit
open class AbstractListViewModel  constructor(
    val statisticsManager: StatisticsManager) : ViewModel() {
    var _actionResult = MutableLiveData<NetworkResponse<Any>>()
    val actionResult:LiveData<NetworkResponse<Any>> = _actionResult
    fun getScreenDate(): LocalDate{
        return dailyTaskSchedule.date
    }
    private var dailyTaskSchedule: DailySchedule
    private var dailyScheduleDate = mutableStateOf(
        Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
    )
    var dailyTaskList: SnapshotStateList<DailyTask>
    private val user: User = User
    init {
        val date: LocalDate =
            Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
        dailyTaskSchedule = user.getSchedule().getOrCreateDailySchedule(date)
        dailyTaskList = SnapshotStateList(dailyTaskSchedule.getDailyTaskList())
    }
    open fun scheduleTaskRequest(task: DailyTask, action: DailyTaskAction.Type) {
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
        statisticsManager.addDailyTask(newTask)
        scheduleTaskRequest(newTask, DailyTaskAction.Type.ADD)
    }

    fun removeDailyTask(task : DailyTask) {
        if (!dailyTaskSchedule.removeDailyTask(task)) {
            // TODO
        } else {
            dailyTaskList.remove(task)
            statisticsManager.removeDailyTask(task)
            scheduleTaskRequest(task, DailyTaskAction.Type.DELETE)
        }
    }

    fun changeTaskCompletion(task: DailyTask, status: Boolean) {
        if (dailyTaskSchedule.setCompletionById(task.getId(), status)) {
            statisticsManager.changeTaskCompletion(task, status)
            task.setCompletion(status)
            scheduleTaskRequest(task, DailyTaskAction.Type.CHANGE_COMPLETION)
        }
    }

    fun changeDailyTaskSchedule(date: LocalDate) {
        dailyTaskSchedule = user.getSchedule().getOrCreateDailySchedule(date)
        dailyScheduleDate.value = dailyTaskSchedule.date
        dailyTaskList.clear()
        dailyTaskSchedule.getDailyTaskList().forEach { task ->
            dailyTaskList.add(task)
        }
    }

    fun moveToNextDailySchedule() {
        val date: LocalDate = dailyTaskSchedule.date
        val nextDate: LocalDate = date.plus(
            DatePeriod(
                days = 1
            )
        )
        changeDailyTaskSchedule(nextDate)
    }

    fun moveToPreviousDailySchedule() {
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

    fun isUpdatable(oldTask: DailyTask, newTask: DailyTask): Boolean {
        if (!dailyTaskSchedule.getDailyTaskList().contains(oldTask)) {
            return false
        }
        dailyTaskSchedule.getDailyTaskList().forEach { task ->
            if (task != oldTask) {
                if (task.isNestedTasks(newTask)) {
                    return false
                }
            }
        }
        return true
    }

    private fun <T> SnapshotStateList(dailyTaskList: List<T>): SnapshotStateList<T> {
        val result: SnapshotStateList<T> = SnapshotStateList()
        dailyTaskList.forEach { task ->
            result.add(task)
        }
        return result
    }
}
@HiltViewModel
class ListViewModel @Inject constructor(statisticsManager: StatisticsManager) : AbstractListViewModel(statisticsManager) {
    private val workManager = WorkManagerHolder.getInstance()

    override fun scheduleTaskRequest(task: DailyTask, action: DailyTaskAction.Type) {
        val taskJson = Json.encodeToString(DailyTaskAction.serializer(), DailyTaskAction(task = task, type = action))

        val workRequest = OneTimeWorkRequestBuilder<TaskApiWorker>()
            .setInputData(workDataOf(DailyTaskAction.jsonName to taskJson))
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniqueWork(
            "task_${action}_${task.getId()}",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
    fun sendAudio(
        audioFile: MutableState<File?>,
        description: AudioDescription,
    ): DailyTask? {
        // TODO Надо написать отправку файла и обработку ответа.
        Thread.sleep(1000)
        val task: DailyTask = DailyTask(
            title = "TODO",
            description = "TODO",
            start = LocalTime(0, 0),
            end = LocalTime(0, 0),
            date = DailyTask.defaultDate
        )
        return task
    }

    class NestedTask(val nestedTask: DailyTask) :
        Exception("Collision in list in case of updating")

    enum class AudioDescription(private val toString: String) {
        CONVERT_AUDIO("TODO Convert audio"),
        SUGGESTIONS("TODO Suggestions");

        @Override
        override fun toString(): String {
            return toString
        }
    }
}