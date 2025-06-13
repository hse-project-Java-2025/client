package org.hse.smartcalendar.view.model

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import org.hse.smartcalendar.data.Invite

class InvitesViewModel : ViewModel() {
    val invites = mutableStateListOf<Invite>()

    fun init() {
        //TODO: в LoadingScreen отправлять запрос серверу
        loadInvites()
    }
    fun loadInvites() {
        invites.addAll(
            listOf(
            )
        )
    }
    fun addInvite(invite: Invite){//preview
        invites.add(invite)
    }
    fun tryAdd(invite: Invite): Boolean{
        return true
    }

    private fun accept(invite: Invite) {
        invites.remove(invite)
    }

    fun decline(invite: Invite) {
        invites.remove(invite)
    }
}