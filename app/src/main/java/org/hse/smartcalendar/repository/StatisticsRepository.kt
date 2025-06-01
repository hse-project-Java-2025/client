package org.hse.smartcalendar.repository

import okhttp3.ResponseBody
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.network.StatisticsApiInterface
import org.hse.smartcalendar.network.StatisticsDTO
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
}