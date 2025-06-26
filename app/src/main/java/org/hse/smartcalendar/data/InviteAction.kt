package org.hse.smartcalendar.data

import kotlinx.serialization.Serializable
import org.hse.smartcalendar.utility.UUIDSerializer
import java.util.UUID

@Serializable
data class InviteAction(
    val type: Type,
    @Serializable(with = UUIDSerializer::class)
    val eventId: UUID,
    val loginOrEmail: String
) {
    enum class Type {
        ACCEPT,
        INVITE,
        REMOVE_INVITE;

        override fun toString(): String = name
    }

    companion object {
        const val jsonName = "invite_action_json"
    }
}