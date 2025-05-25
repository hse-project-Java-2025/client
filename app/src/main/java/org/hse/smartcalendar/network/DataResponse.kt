package org.hse.smartcalendar.network
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import java.util.UUID

@Serializable
data class RegisterResponse (
    val id: Long? = null,
    val username: String? = null,
    val email: String?  = null,
    val password: String? = null,
    val tasks: List<String> = emptyList(),
    val enabled: Boolean? = true,
    val authorities: List<String>? = emptyList(),
    val accountNonExpired: Boolean? = true,
    val credentialsNonExpired: Boolean? = true
)
@Serializable
data class LoginResponse(
    val token: String
)
@Serializable
data class CredentialsResponse(
    val message: String
)
@Serializable
data class AddTaskResponse(
    val message: String
)
@Serializable
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
    @Contextual val start: LocalTime,
    @Contextual val end: LocalTime,
    @Contextual val date: LocalDate,
    @Contextual val type: DailyTaskType,
    @Contextual val creationTime: LocalDateTime,
    val complete: Boolean
){
        fun toTask(): DailyTask{
            return DailyTask(
                id = UUID.fromString(this.id),
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