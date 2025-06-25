package org.hse.smartcalendar.network

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.utility.TimeUtils
data class InviteRequest(val loginOrEmail: String)
data class LoginRequest(
    val username: String,
    val password: String
)
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val firstDay:String = TimeUtils.getCurrentDateTime().date.toString()
)
data class ChangeCredentialsRequest(
    val currentUsername: String,
    val currentPassword: String,
    val newUsername: String,
    val newPassword: String
)
@Serializable
data class EditTaskRequest(
    val creationTime: LocalDateTime,
    val title: String,
    val description: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val location: String,
    val type: DailyTaskType,
    val completed: Boolean
){
    companion object{
        fun fromTask(task: DailyTask): EditTaskRequest{
            return EditTaskRequest(
                title = task.getDailyTaskTitle(),
                description = task.getDailyTaskDescription(),
                start = LocalDateTime(task.getTaskDate(), task.getDailyTaskStartTime()),
                end = LocalDateTime(task.getTaskDate(), task.getDailyTaskEndTime()),
                location = "",
                type = task.getDailyTaskType(),
                creationTime = task.getDailyTaskCreationTime(),
                completed = task.isComplete()
            )
        }
    }
}
@Serializable
data class CompleteStatusRequest(
    val completed: Boolean
){
    companion object {
        fun fromTask(task: DailyTask): CompleteStatusRequest {
            return CompleteStatusRequest(task.isComplete())
        }
    }
}

@Serializable
data class TaskRequest(
    val id: String,
    val title: String,
    val description: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val date: String,
    val type: DailyTaskType,
    val creationTime: LocalDateTime,
    val complete: Boolean
) {
    companion object {
        fun fromTask(task: DailyTask): TaskRequest {
            val date = task.getTaskDate()
            return TaskRequest(
                id = task.getId().toString(),
                title = task.getDailyTaskTitle(),
                description = task.getDailyTaskDescription(),
                start = LocalDateTime(date = date, time = task.getDailyTaskStartTime()),
                end = LocalDateTime(date = date, time = task.getDailyTaskEndTime()),
                date = date.toString(),
                type = task.getDailyTaskType(),
                creationTime = task.getDailyTaskCreationTime(),
                complete = task.isComplete()
            )
        }
    }
}