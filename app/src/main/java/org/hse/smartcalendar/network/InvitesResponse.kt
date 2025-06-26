package org.hse.smartcalendar.network

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.data.Invite
import org.hse.smartcalendar.data.SharedInfo
import java.util.UUID

@Serializable
data class InviteResponse(
    val id: String,
    val title: String,
    val description: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val location: String,
    val type: String,
    val creationTime: LocalDateTime,
    val organizer: OrganizerResponse,
    val completed: Boolean,
    val invitees: List<String>,
    val participants: List<OrganizerResponse>,
    val shared: Boolean
)

@Serializable
data class OrganizerResponse(
    val username: String,
    val email: String
)

fun InviteResponse.toInvite(): Invite {
    val task = DailyTask(
        id = UUID.fromString(id),
        title = title,
        description = description,
        start = start.time,
        end = end.time,
        date = start.date,
        type = DailyTaskType.valueOf(type.uppercase()),
        creationTime = creationTime,
        isComplete = completed,
        sharedInfo = SharedInfo(
            isShared = shared,
            invitees = invitees,
            participants = participants.map { it.email },
            organizerName = organizer.username
        )
    )
    return Invite(
        id = UUID.fromString(id),
        inviterName = organizer.username,
        task = task
    )
}