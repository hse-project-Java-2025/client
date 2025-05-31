package org.hse.smartcalendar.network

import kotlinx.datetime.LocalDateTime
import org.hse.smartcalendar.network.NetworkResponse.Error
import retrofit2.Response
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import java.util.UUID

data class LoginRequest(
    val username: String,
    val password: String
)
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)
data class ChangeCredentialsRequest(
    val currentUsername: String,
    val currentPassword: String,
    val newUsername: String,
    val newPassword: String
)

data class AddTaskRequest(
    val id: UUID,
    val creationTime: LocalDateTime,
    val title: String,
    val description: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val location: String,
    val type: DailyTaskType
){
    companion object{
        fun fromTask(task: DailyTask): AddTaskRequest{
            return AddTaskRequest(
                title = task.getDailyTaskTitle(),
                description = task.getDailyTaskDescription(),
                start = LocalDateTime(task.getTaskDate(), task.getDailyTaskStartTime()),
                end = LocalDateTime(task.getTaskDate(), task.getDailyTaskEndTime()),
                location = "",
                type = task.getDailyTaskType(),
                id = task.getId(),
                creationTime = task.getDailyTaskCreationTime()
            )
        }
    }
}