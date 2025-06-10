package org.hse.smartcalendar.repository

import okhttp3.MultipartBody
import org.hse.smartcalendar.network.AudioApiInterface
import org.hse.smartcalendar.network.ChatTaskResponse
import org.hse.smartcalendar.network.NetworkResponse

class AudioRepository(private val api: AudioApiInterface) : BaseRepository() {

    suspend fun sendAudio(filePart: MultipartBody.Part): NetworkResponse<List<ChatTaskResponse>> {
        return withSupplierRequest { api.processAudio(filePart) }
    }
}