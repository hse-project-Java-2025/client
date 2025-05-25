package org.hse.smartcalendar.repository

import okhttp3.ResponseBody
import org.hse.smartcalendar.data.DailySchedule
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.User
import org.hse.smartcalendar.network.AddTaskRequest
import org.hse.smartcalendar.network.LoginResponse
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.network.TaskApiInterface
import retrofit2.Response

@Suppress("LiftReturnOrAssignment")
class TaskRepository(private val api: TaskApiInterface) {
    suspend fun<T> withIdRequest( supplier:suspend (Long)->Response<T>): NetworkResponse<T> {
        try {
            val id = User.id
            if (id==null){
                return NetworkResponse.errorId()
            }
            val response = supplier.invoke(id)
            if (response.isSuccessful){
                val responseBody = response.body()
                if (responseBody == null){
                    return NetworkResponse.errorNullResponse()
                } else {
                    return NetworkResponse.Success(responseBody)
                }
            } else {
                return NetworkResponse.fromResponse(response)
            }
        } catch (e: Exception) {
            return NetworkResponse<LoginResponse>.NetworkError(e.message.toString())
        }
    }
    suspend fun addTask(task: DailyTask): NetworkResponse<ResponseBody> {
        return withIdRequest() { id ->
            api.addTask(id, AddTaskRequest.fromTask(task))
        }
    }
    suspend fun initUserTasks(): NetworkResponse<List<DailyTask>>{
        return when (val response =  withIdRequest { id -> api.getDailyTasks(id)}){
            is NetworkResponse.Success -> {
                val listTask:List<DailyTask> = response.data.map { it.toTask() }
                val map = listTask.groupBy { it.getTaskDate() }
                    .mapValues { (date, taskList) ->
                        DailySchedule().apply {
                            taskList.forEach { addDailyTask(it) }
                        }
                    }.toMutableMap()
                User.getSchedule().initMap(map)
                return NetworkResponse.Success(listTask)}
            is NetworkResponse.NetworkError -> NetworkResponse.NetworkError(response.exceptionMessage)
            is NetworkResponse.Error -> NetworkResponse.Error(response.message)
            is NetworkResponse.Loading -> NetworkResponse.Loading
        }
    }
}