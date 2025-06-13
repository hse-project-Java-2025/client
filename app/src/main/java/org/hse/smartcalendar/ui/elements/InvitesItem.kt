import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import org.hse.smartcalendar.ui.task.DailyTaskCard
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import java.util.UUID

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InviteItem(
    invite: Invite,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val dismissState = rememberDismissState { dismissValue ->
        when (dismissValue) {
            DismissValue.DismissedToEnd -> {
                onAccept()
                true
            }
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
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(invite.inviterName, style = MaterialTheme.typography.labelMedium)
                            Spacer(Modifier.height(4.dp))
                            Text(invite.task.getDailyTaskTitle(), style = MaterialTheme.typography.bodyLarge)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                invite.task.getDailyTaskArrangementString(),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Date: ${invite.task.getTaskDate()}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { expanded = !expanded }) {
                        Text(if (expanded) "Hide Details" else "Show Details")
                    }
                }
                if (expanded) {
                    DailyTaskCard(
                        task = invite.task,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onCompletionChange = {},
                        onLongPressAction = {}
                    )
                }
            }
        }
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
                onAccept = {},
                onDecline = {}
            )
        }
    }
}