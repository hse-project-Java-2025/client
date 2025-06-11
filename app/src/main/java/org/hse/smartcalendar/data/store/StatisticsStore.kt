package org.hse.smartcalendar.store

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.TotalTimeTaskTypes
import org.hse.smartcalendar.data.WorkManagerHolder
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.network.StatisticsDTO
import org.hse.smartcalendar.repository.StatisticsRepository
import org.hse.smartcalendar.view.model.state.AverageDayTimeVars
import org.hse.smartcalendar.view.model.state.TodayTimeVars
import org.hse.smartcalendar.view.model.state.WeekTime
import org.hse.smartcalendar.view.model.state.StatisticsCalculator
import org.hse.smartcalendar.work.StatisticsUploadWorker

object StatisticsStore {
    private val repository = StatisticsRepository(ApiClient.statisticsApiService)
    var totalTime by mutableStateOf(TotalTimeTaskTypes(0,0,0,0))
        private set
    var averageDayTime by mutableStateOf(AverageDayTimeVars(firstDay = LocalDate(1970,1,1), totalWorkMinutes = 0))
        private set
    var todayTime by mutableStateOf(TodayTimeVars(0,0))
        private set
    var weekTime by mutableStateOf(WeekTime(0))
        private set
    var calculator = StatisticsCalculator()
        private set
    fun clear() {
        totalTime = TotalTimeTaskTypes(0, 0, 0, 0)
        averageDayTime = AverageDayTimeVars(firstDay = LocalDate(1970, 1, 1), totalWorkMinutes = 0)
        todayTime = TodayTimeVars(0, 0)
        weekTime = WeekTime(0)
        calculator = StatisticsCalculator()
    }
    var uploader: () -> Unit = { uploadStatistics() }
    suspend fun init(): NetworkResponse<StatisticsDTO> = withContext(Dispatchers.IO) {
        when(val resp = repository.getStatistics()) {
            is NetworkResponse.Success -> {
                val d = resp.data
                totalTime = d.totalTime.toVMTotalTime()
                averageDayTime = AverageDayTimeVars.fromAverageDayDTO(d.averageDayTime)
                todayTime = TodayTimeVars.fromTodayTimeDTO(d.todayTime)
                weekTime = WeekTime(d.weekTime)
                calculator.init(d)
                resp
            }
            else -> resp
        }
    }
    private fun createOrDeleteTask(task: DailyTask, isCreate: Boolean){
        if (task.isComplete() && isCreate==false){
            changeTaskCompletion(task, false)
        }
        if (task.belongsCurrentDay()){
            todayTime.Planned.plusMinutes(task.getMinutesLength(), isCreate)
        }
    }

    fun createOrDeleteTask(task: DailyTask, dateTasks: List<DailyTask>, isAdd: Boolean) {
        createOrDeleteTask(task, isAdd)
        calculator.addOrDeleteTask(
            StatisticsCalculator.AddOrDeleteRequest
                (date = task.getTaskDate(), dateTasks = dateTasks))
        uploader()
    }

    fun changeTaskCompletion(task: DailyTask, isComplete: Boolean, isUploadStats: Boolean=true) {
        if (task.belongsCurrentDay()) todayTime = todayTime.apply { Completed.plusMinutes(task.getMinutesLength(), isComplete) }
        if (task.belongsCurrentWeek()) weekTime = weekTime.apply { All.addMinutes(task.getMinutesLength().toLong(), isComplete) }
        totalTime = totalTime.apply { completeTask(task, isComplete) }
        averageDayTime = averageDayTime.apply { update(totalTime.totalMinutes) }
        calculator.changeTaskCompletion(task)
        if (isUploadStats) {uploader()}
    }
    fun changeTask(task: DailyTask, newTask: DailyTask){
        if (task.isComplete()){
            changeTaskCompletion(task, false, isUploadStats = false)
            changeTaskCompletion(newTask, true, isUploadStats = false)
        }
        createOrDeleteTask(task, false)
        createOrDeleteTask(newTask, true)
        uploader()
    }
    private fun uploadStatistics() {
        val workManager = WorkManagerHolder.getInstance()
        val statsDTO = StatisticsDTO.fromStore()
        val json = Json.encodeToString(StatisticsDTO.serializer(), statsDTO)

        val workRequest = OneTimeWorkRequestBuilder<StatisticsUploadWorker>()
            .setInputData(workDataOf("statistics_json" to json))
            .build()

        workManager.enqueueUniqueWork(
            "upload_stats",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

}