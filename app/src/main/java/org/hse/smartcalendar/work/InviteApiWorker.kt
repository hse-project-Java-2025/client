package org.hse.smartcalendar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.serialization.json.Json
import org.hse.smartcalendar.data.InviteAction
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.repository.InviteRepository

class InviteApiWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val repo = InviteRepository(ApiClient.inviteApiService)

    override suspend fun doWork(): Result {
        val json = inputData.getString(InviteAction.jsonName)
            ?: return Result.failure()
        val action = Json.decodeFromString(InviteAction.serializer(), json)

        val success = when (action.type) {
                InviteAction.Type.ACCEPT -> repo.acceptInvite(action.eventId)
                InviteAction.Type.INVITE -> repo.inviteUser(action.eventId, action.loginOrEmail)
                InviteAction.Type.REMOVE_INVITE -> repo.removeInvite(
                    action.eventId,
                    action.loginOrEmail
                )
            }.let {
            it is NetworkResponse.Success
        }
        return if (success) Result.success() else Result.retry()
    }
}