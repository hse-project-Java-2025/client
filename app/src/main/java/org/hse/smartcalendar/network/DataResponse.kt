package org.hse.smartcalendar.network

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.utility.TimeUtils
import java.util.UUID
import androidx.compose.runtime.MutableState

data class RegisterResponse (
    val id: Long? = null,
    val username: String? = null,
    val email: String?  = null,
    val password: String? = null,
    val tasks: List<Any> = emptyList(),
    val enabled: Boolean? = true,
    val authorities: List<Any>? = emptyList(),
    val accountNonExpired: Boolean? = true,
    val credentialsNonExpired: Boolean? = true
)
data class LoginResponse(
    val token: String
)
data class CredentialsResponse(
    val message: String
)
data class AddTaskResponse(
    val id: UUID
)
data class UserInfoResponse(
    val email: String,
    val username: String,
    val id: Long
)
@Serializable
data class TaskResponse(
    val id: String,
    val title: String,
    val description: String,
    val start: LocalTime,
    val end: LocalTime,
    val date: String,
    val type: DailyTaskType,
    val creationTime: LocalDateTime,
    val complete: Boolean
){
    fun toTask(): DailyTask {
        return DailyTask(
            id = UUID.fromString(id),
            title = title,
            description = description,
            start = start,
            end = end,
            date = LocalDate.parse(date),
            type = type,
            creationTime = creationTime,
            isComplete = complete
        )
    }
}
@Serializable
data class ChatTaskResponse(
    val id: String,
    val title: String,
    val description: String,
    val start: LocalDateTime?,
    val end: LocalDateTime?,
    val date: String,
    val type: String?,
    val creationTime: LocalDateTime?,
    val complete: Boolean?
) {
    fun toDailyTask(): DailyTask {
        val task = DailyTask(
            id = UUID.fromString(id),
            title = title,
            description = description,
            start = start?.time ?: LocalTime(0, 0),
            end = end?.time ?: LocalTime(0, 0),
            date = (start ?: end?: creationTime?: TimeUtils.getCurrentDateTime()).date ,
            type = DailyTaskType.valueOf(type?.uppercase() ?: "COMMON"),
            creationTime = creationTime?: TimeUtils.getCurrentDateTime(),
            isComplete = complete == true,
        )
        return task
    }
    private fun <T> applyToState(
        change: T?,
        state: MutableState<T>
    ){
        if (change !=null){
            state.value = change
        }
    }
    fun applyToUiStates(
        taskTitle: MutableState<String>,
        taskDescription: MutableState<String>,
        taskType: MutableState<DailyTaskType>,
        startTime: MutableState<Int>,
        endTime: MutableState<Int>,
        isErrorInRecorder: MutableState<Boolean>
    ) {
        isErrorInRecorder.value = false

        taskTitle.value = this.title
        taskDescription.value = this.description
        val newType =  type?.let { DailyTaskType.valueOf(it.uppercase()) }
        applyToState(newType, taskType)
        val newStart = start?.time?.toSecondOfDay()?.div(60)
        applyToState(newStart, startTime)
        val newEnd = start?.time?.toSecondOfDay()?.div(60)
        applyToState(newEnd, endTime)
    }
}
