package org.hse.smartcalendar.data

import kotlinx.datetime.LocalDateTime


data class ChatMessage(
    val message: String,
    val senderId: String,
    val timestamp: LocalDateTime,
)