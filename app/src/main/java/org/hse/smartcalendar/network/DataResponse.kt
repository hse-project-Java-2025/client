package org.hse.smartcalendar.network

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
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
