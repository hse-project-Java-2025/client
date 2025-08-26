package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.data.Invite
import org.hse.smartcalendar.data.SuggestedTask
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import java.util.UUID

@Composable
fun SuggestedTaskItem(
    suggestedTask: SuggestedTask,
    onAccept: () -> Boolean,
    onDecline: () -> Unit,
) {
    TaskItemBase(
        task = suggestedTask.task,
        onAccept = onAccept,
        onDecline = onDecline,
        expandable = false,
        statusContent = {
            when (suggestedTask.status) {
                SuggestedTask.TaskStatus.PENDING -> Icon(Icons.Default.Close, contentDescription = "Pending", tint = Color.Gray)
                SuggestedTask.TaskStatus.ACCEPTED -> Icon(Icons.Default.Check, contentDescription = "Accepted", tint = Color.Green)
                SuggestedTask.TaskStatus.DECLINED -> Icon(Icons.Default.Close, contentDescription = "Declined", tint = Color.Red)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SuggestedTaskItemPreview() {
    SmartCalendarTheme {
        val task = DailyTask.fromTimeAndType(
            type = DailyTaskType.WORK,
            start = LocalTime(4, 0),
            end = LocalTime(5, 0),
            date = DailyTask.defaultDate
        )
        val suggestedTaskPending = SuggestedTask(
            task = task,
            status = SuggestedTask.TaskStatus.PENDING
        )

        val suggestedTaskAccepted = SuggestedTask(
            task = task,
            status = SuggestedTask.TaskStatus.ACCEPTED
        )

        Surface(modifier = Modifier.padding(16.dp)) {
            Column() {
                SuggestedTaskItem(
                    suggestedTask = suggestedTaskPending,
                    onAccept = { true },
                    onDecline = {}
                )
                SuggestedTaskItem(
                    suggestedTask = suggestedTaskAccepted,
                    onAccept = { true },
                    onDecline = {}
                )
            }
        }
    }
}