package org.hse.smartcalendar.repository

import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hse.smartcalendar.data.DailySchedule
import org.hse.smartcalendar.data.DailySchedule.NestedTaskException
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.User
import org.hse.smartcalendar.network.CompleteStatusRequest
import org.hse.smartcalendar.network.EditTaskRequest
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.network.TaskApiInterface
import org.hse.smartcalendar.network.TaskRequest

class TaskRepository(private val api: TaskApiInterface): BaseRepository() {
    suspend fun deleteTask(task: DailyTask): NetworkResponse<ResponseBody>{
        val response = withSupplierRequest<ResponseBody>{
            ->api.deleteTask(task.getId())
        }
        return response
    }
    suspend fun changeTaskCompletion(task: DailyTask): NetworkResponse<ResponseBody>{
        val response = withSupplierRequest<ResponseBody>{
            ->api.changeTaskCompletion(task.getId(), CompleteStatusRequest.fromTask(task))
        }
        return response
    }
    suspend fun editTask(task: DailyTask): NetworkResponse<ResponseBody>{
        val response = withSupplierRequest<ResponseBody>{
            ->api.editTask(task.getId(), EditTaskRequest.fromTask(task))
        }
        return response
    }
    suspend fun addTask(task: DailyTask): NetworkResponse<ResponseBody> {
        val response = withIdRequest { id ->
            api.addTask(id, TaskRequest.fromTask(task))}
        return when (response) {
            is NetworkResponse.Success -> {
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
        try {
            return when (val response = withIdRequest { id -> api.getDailyTasks(id) }) {
                is NetworkResponse.Success -> {
                    User.clearSchedule()
                    val listTask: List<DailyTask> = response.data.map { it.toTask() }
                    val map = listTask.groupBy { it.getTaskDate() }
                        .mapValues { (_, taskList) ->
                            DailySchedule().apply {
                                taskList.forEach { addDailyTask(it) }
                            }
                        }.toMutableMap()
                    User.getSchedule().initMap(map)
                    return NetworkResponse.Success(listTask)
                }

                is NetworkResponse.NetworkError -> NetworkResponse.NetworkError(response.exceptionMessage)
                is NetworkResponse.Error -> NetworkResponse.Error(response.message)
                is NetworkResponse.Loading -> NetworkResponse.Loading
            }
        } catch (e : NestedTaskException){
            return NetworkResponse.Error("Server data error\n"+e.message.toString())
        }
    }
}