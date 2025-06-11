package org.hse.smartcalendar.repository

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import org.hse.smartcalendar.data.User
import org.hse.smartcalendar.network.AudioApiInterface
import org.hse.smartcalendar.network.ChatTaskResponse
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.data.DailyTask
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody.Companion.asRequestBody
import org.hse.smartcalendar.store.StatisticsStore

class AudioRepository(private val api: AudioApiInterface) : BaseRepository() {

    suspend fun sendAudioAndAddTasks(audioFile: File): NetworkResponse<List<DailyTask>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val requestFile = audioFile.asRequestBody("audio/*".toMediaType())
            val part = MultipartBody.Part.createFormData("file", audioFile.name, requestFile)

            when (val resp = sendAudio(part)) {
                is NetworkResponse.Success -> {
                    val newTasks: List<DailyTask> = resp.data.map { it.toDailyTask() }
                    val addedTasks: ArrayList<DailyTask> = ArrayList<DailyTask>()
                    val schedule = User.getSchedule()
                    newTasks.forEach { task ->{
                        val dailySchedule = schedule
                            .getOrCreateDailySchedule(task.getTaskDate())
                        if (dailySchedule
                            .addDailyTask(task)){
                            addedTasks.add(task)
                        }
                    }
                    }
                    NetworkResponse.Success(addedTasks)
                }
                is NetworkResponse.Error -> NetworkResponse.Error(resp.message)
                is NetworkResponse.NetworkError -> NetworkResponse.NetworkError(resp.exceptionMessage)
                is NetworkResponse.Loading -> NetworkResponse.Loading
            }
        } catch (e: Exception) {
            NetworkResponse.NetworkError(e.localizedMessage ?: "Unknown error")
        }
    }
    suspend fun sendAudioGetResponse(audioFile: File): NetworkResponse<ChatTaskResponse> = withContext(Dispatchers.IO) {
        return@withContext try {
            val requestFile = audioFile.asRequestBody("audio/*".toMediaType())
            val part = MultipartBody.Part.createFormData("file", audioFile.name, requestFile)
            when (val resp = sendAudio(part)) {
                is NetworkResponse.Success -> {
                    if (resp.data.isEmpty()){
                        NetworkResponse.Error("response empty")
                    }
                    NetworkResponse.Success(resp.data[0])
                }
                is NetworkResponse.Error -> NetworkResponse.Error(resp.message)
                is NetworkResponse.NetworkError -> NetworkResponse.NetworkError(resp.exceptionMessage)
                is NetworkResponse.Loading -> NetworkResponse.Loading
            }
        } catch (e: Exception) {
            NetworkResponse.NetworkError(e.localizedMessage ?: "Unknown error")
        }
    }

    suspend fun sendAudio(filePart: MultipartBody.Part): NetworkResponse<List<ChatTaskResponse>> {
        return withSupplierRequest { api.processAudio(filePart) }
    }
}