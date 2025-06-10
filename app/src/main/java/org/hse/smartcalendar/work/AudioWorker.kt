package org.hse.smartcalendar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.hse.smartcalendar.repository.AudioRepository
import java.io.File
import org.hse.smartcalendar.data.User
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.NetworkResponse

class AudioWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    private val repo = AudioRepository(ApiClient.audioApiService)

    override suspend fun doWork(): Result {
        val path = inputData.getString("audio_path") ?: return Result.failure()
        val descJson = inputData.getString("audio_desc") ?: return Result.failure()
        val file = File(path)
        val requestFile = RequestBody.create("audio/*".toMediaType(), file)
        val part = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val response = repo.sendAudio(part)
        return if (response is NetworkResponse.Success) {
            val newTasks = response.data.map{it.toDailyTask()};
            for (task in newTasks) {
                User.getSchedule().getOrCreateDailySchedule(task.getTaskDate()).addDailyTask(task)
            }
            Result.success()
        } else {
            Result.retry()
        }
    }
}