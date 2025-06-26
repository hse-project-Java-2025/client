package org.hse.smartcalendar.repository

import okhttp3.ResponseBody
import org.hse.smartcalendar.data.store.InvitesStore
import org.hse.smartcalendar.network.InviteApiInterface
import org.hse.smartcalendar.network.InviteRequest
import org.hse.smartcalendar.network.InviteResponse
import org.hse.smartcalendar.network.NetworkResponse
import java.util.UUID

class InviteRepository(private val api: InviteApiInterface) : BaseRepository() {

    suspend fun getMyInvites(): NetworkResponse<List<InviteResponse>> {
        return withSupplierRequest { api.getMyInvites() }
    }

    suspend fun acceptInvite(eventId: UUID): NetworkResponse<ResponseBody> {
        return withSupplierRequest { api.acceptInvite(eventId) }
    }

    suspend fun inviteUser(eventId: UUID, loginOrEmail: String): NetworkResponse<ResponseBody> {
        return withSupplierRequest {
            api.inviteUser(eventId, InviteRequest(loginOrEmail))
        }
    }

    suspend fun removeInvite(eventId: UUID, loginOrEmail: String): NetworkResponse<ResponseBody> {
        return withSupplierRequest {
            api.removeInvite(eventId, InviteRequest(loginOrEmail))
        }
    }

    suspend fun removeParticipant(eventId: UUID, loginOrEmail: String): NetworkResponse<ResponseBody> {
        return withSupplierRequest {
            api.removeParticipant(eventId, InviteRequest(loginOrEmail))
        }
    }
}