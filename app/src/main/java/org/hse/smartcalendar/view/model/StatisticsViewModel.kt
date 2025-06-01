package org.hse.smartcalendar.view.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.TotalTimeTaskTypes
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.AverageDayTime
import org.hse.smartcalendar.network.ContinuesSuccessDays
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.network.StatisticsDTO
import org.hse.smartcalendar.network.TodayTime
import org.hse.smartcalendar.notification.StatisticsUploadWorker
import org.hse.smartcalendar.repository.StatisticsRepository
import org.hse.smartcalendar.utility.DayPeriod
import org.hse.smartcalendar.utility.DaysAmount
import org.hse.smartcalendar.utility.TimePeriod
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.time.DurationUnit

class StatisticsViewModel():ViewModel() {
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
    private class WeekTime(all: Long){
        val All: TimePeriod = TimePeriod(all)
    }
    private class TodayTimeVars(planned: Long, completed: Long){
        val Planned: DayPeriod = DayPeriod(planned)
        var Completed: DayPeriod = DayPeriod(completed)
        companion object{
            fun fromTodayTimeDTO(todayTimeDTO: TodayTime): TodayTimeVars{
                return TodayTimeVars(planned = todayTimeDTO.planned,
                    completed = todayTimeDTO.completed)
            }
        }
    }
    private class ContinuesSuccessDaysVars(record: Int, now: Int){
        val Record: DaysAmount = DaysAmount(record)
        val Now: DaysAmount = DaysAmount(now)
        companion object{
            fun fromContinuesSuccessDTO(continuesSuccessDTO: ContinuesSuccessDays): ContinuesSuccessDaysVars{
                return ContinuesSuccessDaysVars(record = continuesSuccessDTO.record.toInt(),
                    now = continuesSuccessDTO.now.toInt())
            }
        }
    }
    private class AverageDayTimeVars(totalWorkMinutes: Long, val totalDays: Long){
        var All: DayPeriod = DayPeriod(totalWorkMinutes/totalDays)
        fun update(totalTimeMinutes: Long){
            All = DayPeriod(totalTimeMinutes/totalDays)
        }
        companion object{
            fun fromAverageDayDTO(averageDayTimeDTO: AverageDayTime): AverageDayTimeVars{
                return AverageDayTimeVars(totalWorkMinutes = averageDayTimeDTO.totalWorkMinutes,
                    totalDays = max(averageDayTimeDTO.totalDays, 1)
                )
            }
        }
    }
    private var ContiniusSuccessDays: ContinuesSuccessDaysVars = ContinuesSuccessDaysVars(0, 0)
    private var TotalTime: TotalTimeTaskTypes = TotalTimeTaskTypes(0, 0, 0, 0)
    private var weekTime = WeekTime(0)
    private var AverageDayTime: AverageDayTimeVars = AverageDayTimeVars(totalDays = 1, totalWorkMinutes =  0)
    private var TodayTime: TodayTimeVars = TodayTimeVars(0, 0)
    fun init(){
        viewModelScope.launch {
            _initResult.value = NetworkResponse.Loading
            val response = statisticsRepo.getStatistics()
            if (response is NetworkResponse.Success){
                val data = response.data
                TotalTime = data.totalTime.toVMTotalTime()
                AverageDayTime = AverageDayTimeVars.fromAverageDayDTO(data.averageDayTime)
                weekTime = WeekTime(data.weekTime)
                ContiniusSuccessDays = ContinuesSuccessDaysVars.fromContinuesSuccessDTO(data.continuesSuccessDays)
                TodayTime = TodayTimeVars.fromTodayTimeDTO(data.todayTime)
            }
            _initResult.value = response
        }
    }
    private fun uploadStatistics(){
        val statsDTO = StatisticsDTO.fromViewModel(this)
        val json =  Json.encodeToString(StatisticsDTO.serializer(), statsDTO)

        val workRequest = OneTimeWorkRequestBuilder<StatisticsUploadWorker>()
            .setInputData(workDataOf("statistics_json" to json))
            .setInitialDelay(10, TimeUnit.SECONDS)//в итоге должно быть минут
            .build()

        WorkManager.getInstance().enqueueUniqueWork(
            "upload_stats",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
    fun createOrDeleteTask(task: DailyTask, isCreate: Boolean, isUploadStats: Boolean =true){
        if (task.isComplete() && isCreate==false){
            changeTaskCompletion(task, false)
        }
        if (task.belongsCurrentDay()){
            TodayTime.Planned.plusMinutes(task.getMinutesLength(), isCreate)
        }
        if (isUploadStats) {uploadStatistics()}
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
        if (isUploadStats) {uploadStatistics()}
    }

    fun changeTask(task: DailyTask, newTask: DailyTask){
        if (task.isComplete()){
            changeTaskCompletion(task, false, isUploadStats = false)
            changeTaskCompletion(newTask, true, isUploadStats = false)
        }
        createOrDeleteTask(task, false, isUploadStats = false)
        createOrDeleteTask(newTask, true, isUploadStats = false)
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

    fun getRecordContiniusSuccessDays():DaysAmount{
        return ContiniusSuccessDays.Record
    }
    fun getTodayContinusSuccessDays():DaysAmount{
        return ContiniusSuccessDays.Now
    }
    fun getTotalTimeActivityTypes():TotalTimeTaskTypes{
        return TotalTimeTaskTypes(TotalTime.Common.time.inWholeMinutes, TotalTime.Work.time.inWholeMinutes, TotalTime.Study.time.inWholeMinutes, TotalTime.Fitness.time.inWholeMinutes)
    }
    fun getWeekWorkTime(): TimePeriod{
        return weekTime.All
    }
    fun getTypesInDay(): Long{
        return 2
    }
}