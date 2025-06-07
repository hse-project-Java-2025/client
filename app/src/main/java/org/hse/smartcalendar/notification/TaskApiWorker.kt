package org.hse.smartcalendar.notification
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.serialization.json.Json
import org.hse.smartcalendar.data.DailyTaskAction
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.repository.TaskRepository

class TaskApiWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val repo = TaskRepository(ApiClient.taskApiService)

    override suspend fun doWork(): Result {
        val taskActionJson = inputData.getString(DailyTaskAction.jsonName) ?: return Result.failure()
        val taskAction = Json.decodeFromString<DailyTaskAction>(taskActionJson)

        val success = when (taskAction.type) {
                DailyTaskAction.Type.ADD -> repo.addTask(taskAction.task)
                DailyTaskAction.Type.EDIT-> repo.editTask(taskAction.task)
                DailyTaskAction.Type.DELETE -> repo.deleteTask(taskAction.task)
                DailyTaskAction.Type.CHANGE_COMPLETION -> repo.changeTaskCompletion(taskAction.task)
            }.let {
                it is org.hse.smartcalendar.network.NetworkResponse.Success
            }

        return if (success) Result.success() else Result.retry()
    }
}