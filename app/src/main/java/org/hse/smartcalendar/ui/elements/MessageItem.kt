package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.hse.smartcalendar.data.ChatMessage

@Composable
fun MessageItem(message: ChatMessage, isCurrentUser: Boolean) {
    val alignment = if (isCurrentUser) Alignment.End else Alignment.Start
    val surfaceColor = if (isCurrentUser) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.secondary
    val textColor = if (isCurrentUser) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.onSecondary
    val formatter = LocalTime.Format {
        hour()
        char(':')
        minute()
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = alignment
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 1.dp,
            color = surfaceColor
        ) {
            Column {
                Text(
                    text = message.message,
                    fontSize = 16.sp,
                    color = textColor,
                    modifier = Modifier.padding(12.dp)
                )
                Text(
                    text = message.timestamp.time.format(formatter),
                    fontSize = 16.sp,
                    color = textColor,
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.End),
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MessageItemPreview() {
    val message = ChatMessage(
        "Max don't have sex with your ex", "USER",
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )
    Column {
        MessageItem(message, true)
        MessageItem(message, false)
    }
}