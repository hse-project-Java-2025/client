package org.hse.smartcalendar.network

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
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
    val id: String,
    val title: String,
    val description: String,
    val start: String,
    val end: String,
    val date: String,
    val type: String,
    val creationTime: String,
    val complete: Boolean
)
        fun toTask(response: TaskResponse): DailyTask{
            return DailyTask(
                id = UUID.fromString(response.id),
                title =response.title,
                description = response.description,
                start = LocalTime.parse(response.start),
                end = LocalTime.parse(response.end),
                date = LocalDate.parse(response.date),
                type = DailyTaskType.fromString(response.type),
                creationTime = LocalDateTime.parse(response.creationTime),
                isComplete = response.complete
            )
        }
