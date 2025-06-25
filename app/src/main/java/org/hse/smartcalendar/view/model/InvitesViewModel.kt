package org.hse.smartcalendar.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.hse.smartcalendar.data.DailySchedule
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.Invite
import org.hse.smartcalendar.data.MainSchedule
import org.hse.smartcalendar.data.User
import org.hse.smartcalendar.data.store.InvitesStore
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.repository.InviteRepository

class InvitesViewModel : ViewModel() {
    var _initResult = MutableLiveData<NetworkResponse<List<Invite>>>()
    val initResult:LiveData<NetworkResponse<List<Invite>>> = _initResult
    private val invitesRepository: InviteRepository = InviteRepository(ApiClient.inviteApiService)
    val invites = mutableListOf<Invite>()
    private val mainSchedule = User.getSchedule()

    fun startPollingInvites(){
        viewModelScope.launch {
            while (true) {
                _initResult.value = NetworkResponse.Loading
                val response = InvitesStore.init()
                if (response is NetworkResponse.Success) {
                    updateUI()
                }
                _initResult.value = response
                kotlinx.coroutines.delay(5000)
            }
        }
    }
    fun updateUI(){
        invites.clear()
        invites.addAll(InvitesStore.invites)
    }
    fun tryAdd(invite: Invite): Boolean{
        val task = invite.task
        try {
            mainSchedule.getOrCreateDailySchedule(task.getTaskDate()).addDailyTask(task)
        } catch (e: DailySchedule.NestedTaskException){
            return false
        }
        accept(invite)
        return true
    }

    private fun accept(invite: Invite) {
        viewModelScope.launch {
            while (invitesRepository.acceptInvite(invite.id) !is NetworkResponse.Success<*>){}
        }
        invites.remove(invite)
    }

    fun decline(invite: Invite) {
        viewModelScope.launch {
            while (invitesRepository.removeInvite(invite.id, User.name) !is NetworkResponse.Success<*>){}
        }
        invites.remove(invite)
    }
    fun addInvite(invite: Invite){//preview
        invites.add(invite)
    }
}