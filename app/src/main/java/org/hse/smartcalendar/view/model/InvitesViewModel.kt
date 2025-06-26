package org.hse.smartcalendar.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import kotlinx.coroutines.launch
import org.hse.smartcalendar.data.DailySchedule
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.Invite
import org.hse.smartcalendar.data.InviteAction
import org.hse.smartcalendar.data.MainSchedule
import org.hse.smartcalendar.data.User
import org.hse.smartcalendar.data.store.InvitesStore
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.repository.InviteRepository
import org.hse.smartcalendar.work.InviteApiWorker
import kotlinx.serialization.json.Json
import org.hse.smartcalendar.data.WorkManagerHolder
import androidx.work.ExistingWorkPolicy

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
        scheduleInviteWork(invite, InviteAction.Type.ACCEPT)
        invites.remove(invite)
    }

    fun decline(invite: Invite) {
        scheduleInviteWork(invite, InviteAction.Type.REMOVE_INVITE)
        invites.remove(invite)
    }
    fun addInvite(invite: Invite){//preview
        invites.add(invite)
    }
    private fun scheduleInviteWork(invite: Invite, actionType: InviteAction.Type) {
        val action = InviteAction(
            type = actionType,
            eventId = invite.id,
            loginOrEmail = User.name
        )
        val actionJson = Json.encodeToString(InviteAction.serializer(), action)

        val workRequest = OneTimeWorkRequestBuilder<InviteApiWorker>()
            .setInputData(workDataOf(InviteAction.jsonName to actionJson))
            .build()
        WorkManagerHolder.getInstance()
            .enqueueUniqueWork(
                "invite_${action.eventId}_${action.loginOrEmail}_${action.type}",
                ExistingWorkPolicy.APPEND,
                workRequest
            )
    }

}