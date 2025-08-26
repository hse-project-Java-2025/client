package org.hse.smartcalendar.repository

import org.hse.smartcalendar.data.DailySchedule
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.User
import org.hse.smartcalendar.network.ChatGptApiInterface
import org.hse.smartcalendar.network.ChatGptRequest
import org.hse.smartcalendar.network.NetworkResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import org.hse.smartcalendar.network.AudioApiInterface
import org.hse.smartcalendar.network.ChatTaskResponse
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody.Companion.asRequestBody
import org.hse.smartcalendar.network.ChatGptResponse
import org.hse.smartcalendar.store.StatisticsStore


class ChatGptRepository(
    private val api: ChatGptApiInterface
) : BaseRepository() {

    val mainSchedule = User.getSchedule()
    private val id = User.id

    suspend fun generateSuggestions(query: String): NetworkResponse<List<DailyTask>> {
        return withIdRequest { userId ->
            api.generateSuggestions(userId, ChatGptRequest(query))
        }.mapSuccess { response ->
            response.events.map { it.toDailyTask() }
        }
    }
    suspend fun generateSuggestionsAudio(audioFile: File): NetworkResponse<List<DailyTask>> {
        return withIdRequest { userId ->
            val requestFile = audioFile.asRequestBody("audio/*".toMediaType())
            val part = MultipartBody.Part.createFormData("file", audioFile.name, requestFile)
            api.generateSuggestionsFromAudio(userId, part)
        }.mapSuccess { response ->
            response.events.map { it.toDailyTask() }
        }
    }


    fun tryAdd(task: DailyTask): Boolean {
        return mainSchedule.getOrCreateDailySchedule(task.getTaskDate()).tryAddTask(task)
    }

    fun declineTask(task: DailyTask): Boolean {
        return true
    }
}