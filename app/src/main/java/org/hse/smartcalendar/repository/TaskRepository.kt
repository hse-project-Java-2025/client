package org.hse.smartcalendar.repository

import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hse.smartcalendar.data.DailySchedule
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.User
import org.hse.smartcalendar.network.AddTaskRequest
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.network.TaskApiInterface
import org.hse.smartcalendar.network.toTask

class TaskRepository(private val api: TaskApiInterface): BaseRepository() {
    suspend fun deleteTask(task: DailyTask): NetworkResponse<ResponseBody>{
        val response = withSupplierRequest()
    }
    suspend fun addTask(task: DailyTask): NetworkResponse<ResponseBody> {
        val response = withIdRequest { id ->
            api.addTask(id, AddTaskRequest.fromTask(task))}
        return when (response) {
            is NetworkResponse.Success -> {
                task.setId(response.data.id)
                NetworkResponse.Success("Task created".toResponseBody(null))
            }
            is NetworkResponse.Error -> response
            is NetworkResponse.NetworkError -> response
            is NetworkResponse.Loading -> response
        }
    }
    /**
     * В случае наложения тасок с сервера вылетит с NestedTaskException
     */
    suspend fun initUserTasks(): NetworkResponse<List<DailyTask>>{
        return when (val response =  withIdRequest { id -> api.getDailyTasks(id)}){
            is NetworkResponse.Success -> {
                val listTask:List<DailyTask> = response.data.map { toTask(it) }
                val map = listTask.groupBy { it.getTaskDate() }
                    .mapValues { (_, taskList) ->
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