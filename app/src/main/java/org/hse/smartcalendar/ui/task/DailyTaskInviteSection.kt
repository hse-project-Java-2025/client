package org.hse.smartcalendar.ui.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedInviteSection(
    isShared: MutableState<Boolean>,
    invitees: SnapshotStateList<String>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = {
                isShared.value = !isShared.value
                if (isShared.value && invitees.isEmpty()) {
                    invitees.add("")
                }
                if (!isShared.value) {
                    invitees.clear()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isShared.value)
                    "Joint event"
                else
                    "Individual event"
            )
        }
        if (isShared.value) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Write logins: ",
                style = MaterialTheme.typography.labelSmall
            )
            invitees.forEachIndexed { index, _ ->
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = invitees[index],
                    onValueChange = { newValue ->
                        invitees[index] = newValue
                    },
                    placeholder = { Text("Login ${index + 1}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        invitees.add("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add another login"
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun SharedInvitePreview() {
    SmartCalendarTheme {

        val isShared = remember { mutableStateOf(false) }
        val invitees = remember { mutableStateListOf("") }
        Column(modifier = Modifier.padding(16.dp)) {
            SharedInviteSection(
                isShared = isShared,
                invitees = invitees
            )
        }
    }
}