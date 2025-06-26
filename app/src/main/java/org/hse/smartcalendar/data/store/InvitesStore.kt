package org.hse.smartcalendar.data.store

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hse.smartcalendar.data.Invite
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.network.InviteResponse
import org.hse.smartcalendar.network.toInvite
import org.hse.smartcalendar.repository.InviteRepository

object InvitesStore {
    private val _invites = mutableStateListOf<Invite>()
    val invites: List<Invite> get() = _invites
    private val repository = InviteRepository(ApiClient.inviteApiService)

    private var lastFetchedInvites: List<Invite> = emptyList()
    private var _hasNewInvites = mutableStateOf(false)
    val hasNewInvites: Boolean get() = _hasNewInvites.value

    suspend fun init(): NetworkResponse<List<Invite>> = withContext(Dispatchers.IO) {
        when (val resp = repository.getMyInvites()) {
            is NetworkResponse.Success -> {
                val invites = resp.data.map { it.toInvite() }
                updateDifference(fetched = invites)
                setInvites(invites)
                NetworkResponse.Success(invites)
            }
            is NetworkResponse.Error -> NetworkResponse.Error(resp.message)
            is NetworkResponse.NetworkError -> NetworkResponse.NetworkError(resp.exceptionMessage)
            is NetworkResponse.Loading -> NetworkResponse.Loading
        }
    }
    private fun updateDifference( fetched: List<Invite>){
        val isDifferent = fetched.size != lastFetchedInvites.size ||
                fetched.map { it.id } != lastFetchedInvites.map { it.id }
        if (isDifferent) {
            _hasNewInvites.value = true
        }
        lastFetchedInvites = fetched
    }

    private fun setInvites(newList: List<Invite>) {
        _invites.clear()
        _invites.addAll(newList)
    }

    fun removeInvite(invite: Invite) {
        _invites.remove(invite)
    }

    fun addInvite(invite: Invite) {
        _invites.add(invite)
    }
    fun markInvitesSeen() {
        _hasNewInvites.value = false
    }
}
