package org.hse.smartcalendar.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.network.StatisticsDTO
import org.hse.smartcalendar.store.StatisticsStore
import org.hse.smartcalendar.view.model.state.StatisticsUiState
import kotlin.math.roundToInt

open class StatisticsViewModel():ViewModel() {
    var _initResult = MutableLiveData<NetworkResponse<StatisticsDTO>>()
    val initResult:LiveData<NetworkResponse<StatisticsDTO>> = _initResult
    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()
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
    fun updateStatistics(){
        _uiState.value = StatisticsUiState(
            total       = StatisticsStore.totalTime,
            week        = StatisticsStore.weekTime,
            averageDay  = StatisticsStore.averageDayTime,
            today       = StatisticsStore.todayTime,
            calculable = StatisticsStore.calculator.stats.value
        )
    }
    fun init(){
        viewModelScope.launch {
            _initResult.value = NetworkResponse.Loading
            val response = StatisticsStore.init()
            if (response is NetworkResponse.Success){
                updateStatistics()
            }
            _initResult.value = response
        }
    }
    fun createOrDeleteTask(task: DailyTask, dailyTaskList: List<DailyTask>, isCreate: Boolean) {
        StatisticsStore.createOrDeleteTask(task, dailyTaskList, isCreate)
        updateStatistics()
    }

    fun changeTaskCompletion(task: DailyTask, isComplete: Boolean, isUploadStats: Boolean = true) {
        StatisticsStore.changeTaskCompletion(task, isComplete, isUploadStats)
        updateStatistics()
    }

    fun changeTask(task: DailyTask, newTask: DailyTask) {
        StatisticsStore.changeTask(task, newTask)
        updateStatistics()
    }
}