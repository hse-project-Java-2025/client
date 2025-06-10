package org.hse.smartcalendar.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.TotalTimeTaskTypes
import org.hse.smartcalendar.data.WorkManagerHolder
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.network.StatisticsDTO
import org.hse.smartcalendar.work.StatisticsUploadWorker
import org.hse.smartcalendar.repository.StatisticsRepository
import org.hse.smartcalendar.store.StatisticsStore
import org.hse.smartcalendar.view.model.state.DayPeriod
import org.hse.smartcalendar.view.model.state.DaysAmount
import org.hse.smartcalendar.view.model.state.StatisticsCalculator
import org.hse.smartcalendar.view.model.state.TimePeriod
import org.hse.smartcalendar.view.model.state.AverageDayTimeVars
import org.hse.smartcalendar.view.model.state.TodayTimeVars
import org.hse.smartcalendar.view.model.state.WeekTime
import kotlin.math.roundToInt

open class AbstractStatisticsViewModel():ViewModel() {
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
    fun init(){
        viewModelScope.launch {
            _initResult.value = NetworkResponse.Loading
            _initResult.value = StatisticsStore.init()
        }
    }
    open fun uploadStatistics(){
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