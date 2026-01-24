package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.data.Invite
import org.hse.smartcalendar.data.SuggestedTask
import org.hse.smartcalendar.ui.task.DailyTaskCard
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import java.util.UUID

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskItemBase(
    task: DailyTask,
    expandedState: MutableState<Boolean> = remember { mutableStateOf(false) },
    onAccept: () -> Boolean,
    onDecline: () -> Unit,
    expandable: Boolean = true,
    statusContent: @Composable (() -> Unit)? = null
) {
    val dismissState = rememberDismissState { dismissValue ->
        when (dismissValue) {
            DismissValue.DismissedToEnd -> onAccept()
            DismissValue.DismissedToStart -> {
                onDecline()
                true
            }
            else -> false
        }
    }

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        background = {
            val color = when (dismissState.dismissDirection) {
                DismissDirection.StartToEnd -> Color(0xFFAAF683)
                DismissDirection.EndToStart -> Color(0xFFFF6B6B)
                null -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 16.dp),
                contentAlignment = when (dismissState.dismissDirection) {
                    DismissDirection.StartToEnd -> Alignment.CenterStart
                    DismissDirection.EndToStart -> Alignment.CenterEnd
                    else -> Alignment.Center
                }
            ) {
                Icon(
                    imageVector = if (dismissState.dismissDirection == DismissDirection.StartToEnd)
                        Icons.Default.Check else Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        dismissContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                DailyTaskCard(
                    task = task,
                    onCompletionChange = {},
                    onLongPressAction = {},
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
                statusContent?.let {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        it()
                    }
                }
                if (expandable){
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                        TextButton(onClick = { expandedState.value = !expandedState.value }) {
                            Text(if (expandedState.value) "Hide Details" else "Show Details")
                        }
                }
                    }
                if (expandedState.value) {
                    DailyTaskCard(
                        task = task,
                        onCompletionChange = {},
                        onLongPressAction = {},
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun InviteItem(
    invite: Invite,
    onAccept: () -> Boolean,
    onDecline: () -> Unit
) {
    TaskItemBase(
        task = invite.task,
        onAccept = onAccept,
        onDecline = onDecline,
    )
}


@Preview(showBackground = true)
@Composable
fun InviteItemPreview() {
    SmartCalendarTheme {
        val task = DailyTask.fromTimeAndType(
            type = DailyTaskType.WORK,
            start = LocalTime(4, 0),
            end = LocalTime(5, 0),
            date = DailyTask.defaultDate
        )
        val invite = Invite(
            id = UUID.randomUUID(),
            inviterName = "Alice",
            task = task
        )

        Surface(modifier = Modifier.padding(16.dp)) {
            InviteItem(
                invite = invite,
                onAccept = {true},
                onDecline = {}
            )
        }
    }
}