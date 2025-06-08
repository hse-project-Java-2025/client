package org.hse.smartcalendar.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.TotalTimeTaskTypes
import org.hse.smartcalendar.data.WorkManagerHolder
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.network.StatisticsDTO
import org.hse.smartcalendar.work.StatisticsUploadWorker
import org.hse.smartcalendar.repository.StatisticsRepository
import org.hse.smartcalendar.view.model.state.DayPeriod
import org.hse.smartcalendar.view.model.state.DaysAmount
import org.hse.smartcalendar.view.model.state.StatisticsCalculator
import org.hse.smartcalendar.view.model.state.TimePeriod
import org.hse.smartcalendar.view.model.state.AverageDayTimeVars
import org.hse.smartcalendar.view.model.state.TodayTimeVars
import org.hse.smartcalendar.view.model.state.WeekTime
import kotlin.math.roundToInt

open class AbstractStatisticsViewModel():ViewModel() {
    private val statisticsRepo: StatisticsRepository = StatisticsRepository(ApiClient.statisticsApiService)
    var _initResult = MutableLiveData<NetworkResponse<StatisticsDTO>>()
    val initResult:LiveData<NetworkResponse<StatisticsDTO>> = _initResult
    companion object {
        fun getPercent(part: Long, all: Long): Float {
            if (all == 0L) return 25.0f
            return toPercent(part.toFloat() / all)
        }
        fun toPercent(part: Float): Float {
            if (part.isNaN() || part.isInfinite()) {
                return 0.0f
            }
            return (part * 1000).roundToInt().toFloat() / 10
        }
    }

    var TotalTime: TotalTimeTaskTypes = TotalTimeTaskTypes(0, 0, 0, 0)
        private set
    var weekTime = WeekTime(0)
        private set
    var AverageDayTime: AverageDayTimeVars = AverageDayTimeVars(totalDays = 1, totalWorkMinutes =  0)
        private set
    var TodayTime: TodayTimeVars = TodayTimeVars(0, 0)
        private set
    val statisticsCalculator: StatisticsCalculator = StatisticsCalculator()
    fun init(){
        viewModelScope.launch {
            _initResult.value = NetworkResponse.Loading
            val response = statisticsRepo.getStatistics()
            if (response is NetworkResponse.Success){
                val data = response.data
                TotalTime = data.totalTime.toVMTotalTime()
                AverageDayTime = AverageDayTimeVars.fromAverageDayDTO(data.averageDayTime)
                weekTime = WeekTime(data.weekTime)
                TodayTime = TodayTimeVars.fromTodayTimeDTO(data.todayTime)
                statisticsCalculator.init(data)
            }
            _initResult.value = response
        }
    }
    open fun uploadStatistics(){
    }
    private fun createOrDeleteTask(task: DailyTask, isCreate: Boolean){
        if (task.isComplete() && isCreate==false){
            changeTaskCompletion(task, false)
        }
        if (task.belongsCurrentDay()){
            TodayTime.Planned.plusMinutes(task.getMinutesLength(), isCreate)
        }
    }
    fun createOrDeleteTask(task: DailyTask, isCreate: Boolean, dailyTaskList: List<DailyTask>){
        createOrDeleteTask(task, isCreate)
        statisticsCalculator.addOrDeleteTask(
            StatisticsCalculator.AddOrDeleteRequest
                (date = task.getTaskDate(), dateTasks = dailyTaskList))
        uploadStatistics()
    }

    fun changeTaskCompletion(task: DailyTask, isComplete: Boolean, isUploadStats: Boolean =true){//когда таска запатчена
        val taskMinutesLength = task.getMinutesLength()
        if (task.belongsCurrentDay()) {
            TodayTime.Completed.plusMinutes(taskMinutesLength, isComplete)
        }
        if (task.belongsCurrentWeek()){
            weekTime.All.addMinutes(taskMinutesLength.toLong(), isComplete)
        }
        TotalTime.completeTask(task, isComplete)
        AverageDayTime.update(TotalTime.totalMinutes)
        statisticsCalculator.changeTaskCompletion(task)
        if (isUploadStats) {uploadStatistics()}
    }

    fun changeTask(task: DailyTask, newTask: DailyTask){
        if (task.isComplete()){
            changeTaskCompletion(task, false, isUploadStats = false)
            changeTaskCompletion(newTask, true, isUploadStats = false)
        }
        createOrDeleteTask(task, false)
        createOrDeleteTask(newTask, true)
        uploadStatistics()
    }

    fun getTotalWorkTime():TimePeriod{
        return TotalTime.All
    }

    fun getTodayPlannedTime(): DayPeriod{
        return TodayTime.Planned
    }
    fun getAverageDailyTime():DayPeriod{
        return AverageDayTime.All
    }

    fun getTodayCompletedTime():DayPeriod{
        return TodayTime.Completed
    }

    fun getRecordContinuesSuccessDays():DaysAmount{
        return DaysAmount(statisticsCalculator.getRecordContinuesSuccessDays())
    }
    fun getTodayContinuesSuccessDays():DaysAmount{
        return DaysAmount(statisticsCalculator.getTodayContinuesSuccessDays())
    }
    fun getTotalTimeActivityTypes():TotalTimeTaskTypes{
        return TotalTimeTaskTypes(TotalTime.Common.time.inWholeMinutes, TotalTime.Work.time.inWholeMinutes, TotalTime.Study.time.inWholeMinutes, TotalTime.Fitness.time.inWholeMinutes)
    }
    fun getWeekWorkTime(): TimePeriod{
        return weekTime.All
    }
    fun getTypesInCurrentDay(): Int{
        return statisticsCalculator.getCurrentDayTypes()
    }


}
class StatisticsViewModel(): AbstractStatisticsViewModel(){
    override fun uploadStatistics() {
        val workManager = WorkManagerHolder.getInstance()
        val statsDTO = StatisticsDTO.fromViewModel(this)
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