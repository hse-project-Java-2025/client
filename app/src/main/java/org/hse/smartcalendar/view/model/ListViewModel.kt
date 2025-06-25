package org.hse.smartcalendar.view.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
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
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.ChatTaskResponse
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.repository.AudioRepository
import org.hse.smartcalendar.repository.InviteRepository
import org.hse.smartcalendar.work.TaskApiWorker
import java.io.File

open class AbstractListViewModel(val statisticsManager: StatisticsManager) : ViewModel() {
    var _actionResult = MutableLiveData<NetworkResponse<Any>>()
    val actionResult:LiveData<NetworkResponse<Any>> = _actionResult
    fun getScreenDate(): LocalDate{
        return dailyTaskSchedule.date
    }
    private lateinit var dailyTaskSchedule: DailySchedule
    private var dailyScheduleDate = mutableStateOf(
        Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
    )
    val dailyTaskList: SnapshotStateList<DailyTask>  = mutableStateListOf()
    protected val user: User = User
    fun loadDailyTasks(){
        dailyTaskSchedule = user.getSchedule().getOrCreateDailySchedule(dailyScheduleDate.value)
        dailyTaskList.addAll(dailyTaskSchedule.getDailyTaskList())
    }
    open fun scheduleTaskRequest(task: DailyTask, action: DailyTaskAction.Type) {
    }
    open fun addDailyTask(newTask : DailyTask) {
        try {
            dailyTaskSchedule.addDailyTask(newTask)
        } catch (exception: DailySchedule.NestedTaskException) {
            throw exception
        }
        dailyTaskList.add(newTask)
        dailyTaskList.sortBy { task ->
            task.getDailyTaskStartTime()
        }
        statisticsManager.addDailyTask(newTask, dailyTaskList)
        scheduleTaskRequest(newTask, DailyTaskAction.Type.ADD)
    }

    fun removeDailyTask(task : DailyTask) {
        if (!dailyTaskSchedule.removeDailyTask(task)) {
            // TODO
        } else {
            dailyTaskList.remove(task)
            statisticsManager.removeDailyTask(task, dailyTaskList)
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
class ListViewModel(statisticsManager: StatisticsManager) : AbstractListViewModel(statisticsManager) {
    private val workManager = WorkManagerHolder.getInstance()
    private val audioRepo = AudioRepository(ApiClient.audioApiService)
    private val invitesRepository = InviteRepository(ApiClient.inviteApiService)
    override fun addDailyTask(newTask: DailyTask) {
        super.addDailyTask(newTask)
        viewModelScope.launch {//on meeting agree on sync invitees
            val id = newTask.getId()
            val invitees = newTask.getSharedInfo().invitees
            for (user in invitees) {
                while (invitesRepository.inviteUser(eventId = id, loginOrEmail = user) !is NetworkResponse.Success<*>){
                }
            }
        }
    }
    override fun scheduleTaskRequest(task: DailyTask, action: DailyTaskAction.Type) {
        val taskJson = Json.encodeToString(DailyTaskAction.serializer(), DailyTaskAction(task = task, type = action))

        val workRequest = OneTimeWorkRequestBuilder<TaskApiWorker>()
            .setInputData(workDataOf(DailyTaskAction.jsonName to taskJson))
            .build()
        workManager.enqueueUniqueWork(
            "task_${task.getId()}",
            ExistingWorkPolicy.APPEND,
            workRequest
        )
    }
    fun sendAudio(
        audioFile: MutableState<File?>,
        description: AudioDescription,
    ): ChatTaskResponse? {
        val file = audioFile.value
        var response: ChatTaskResponse? = null
        if (file!=null) {
            viewModelScope.launch {
                _actionResult.value = NetworkResponse.Loading
                val result  = audioRepo.sendAudioGetResponse(file)
                if (result is NetworkResponse.Success){
                     response = result.data
                }
                _actionResult.value = result
            }
        }
        return response
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