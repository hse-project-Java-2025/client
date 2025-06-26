package org.hse.smartcalendar.data

import kotlinx.serialization.Serializable
import org.hse.smartcalendar.utility.UUIDSerializer
import java.util.UUID

@Serializable
data class Invite(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val inviterName: String,
    val task: DailyTask,
)