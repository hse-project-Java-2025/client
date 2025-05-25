package org.hse.smartcalendar.network

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.LocalDate
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import java.util.UUID

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
    val message: String
)
data class UserInfoResponse(
    val email: String,
    val username: String,
    val id: Long
)
data class TaskResponse(
    val id: UUID,
    val title: String,
    val description: String,
    val start: LocalTime,
    val end: LocalTime,
    val date: LocalDate,
    val type: DailyTaskType,
    val creationTime: LocalDateTime,
    val complete: Boolean
){
        fun toTask(): DailyTask{
            return DailyTask(
                id = this.id,
                title =this.title,
                description = this.description,
                start = this.start,
                end = this.end,
                date = this.date,
                type = this.type,
                creationTime = this.creationTime,
                isComplete = this.complete
            )
        }
}