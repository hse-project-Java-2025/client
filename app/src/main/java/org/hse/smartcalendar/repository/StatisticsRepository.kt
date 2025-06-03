package org.hse.smartcalendar.repository

import okhttp3.ResponseBody
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.network.StatisticsApiInterface
import org.hse.smartcalendar.network.StatisticsDTO
import org.hse.smartcalendar.network.TodayTime
import org.hse.smartcalendar.view.model.StatisticsViewModel

class StatisticsRepository(private val api: StatisticsApiInterface): BaseRepository() {
    suspend fun updateStatistics(viewModel: StatisticsViewModel): NetworkResponse<ResponseBody> {
        return withIdRequest { id ->
            api.updateUserStatistics(id, StatisticsDTO.fromViewModel(viewModel))
        }
    }
    suspend fun updateStatistics(statisticsRequest: StatisticsDTO): NetworkResponse<ResponseBody> {
        return withIdRequest { id ->
            api.updateUserStatistics(id, statisticsRequest)
        }
    }
    suspend fun getStatistics(): NetworkResponse<StatisticsDTO> {
        return when (val response = withIdRequest { id -> api.getUserStatistics(id) }) {
            is NetworkResponse.Success -> return response
            is NetworkResponse.Error -> NetworkResponse.Error(response.message)
            NetworkResponse.Loading -> NetworkResponse.Loading
            is NetworkResponse.NetworkError -> NetworkResponse.NetworkError(response.exceptionMessage)
        }
    }
    fun changeTaskCompletion(statisticsDTO: StatisticsDTO, task: DailyTask, isComplete: Boolean): StatisticsDTO{
        var newTodayCompletedTime = statisticsDTO.todayTime.completed
        if (task.belongsCurrentDay()){
            newTodayCompletedTime+= task.getMinutesLengthSigned(isComplete)
        }
        var newWeekCompletedTime = statisticsDTO.weekTime
        if (task.belongsCurrentWeek()) {
            newWeekCompletedTime+= task.getMinutesLengthSigned(isComplete)
        }
        val newTotalTime = statisticsDTO.totalTime.updated(task, isComplete)
        val averageDayTime = statisticsDTO.averageDayTime.updateTime(task, isComplete)
        return StatisticsDTO(
            totalTime = newTotalTime,
            weekTime = newWeekCompletedTime,
            todayTime = TodayTime(completed = newTodayCompletedTime, planned = statisticsDTO.todayTime.planned),
            continuesSuccessDays = statisticsDTO.continuesSuccessDays,
            averageDayTime = averageDayTime
        )
    }
    suspend fun createOrDeleteTask(
        oldDto: StatisticsDTO,
        task: DailyTask,
        isCreate: Boolean
    ): NetworkResponse<StatisticsDTO> {

    }