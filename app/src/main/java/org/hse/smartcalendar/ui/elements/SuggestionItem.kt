package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.view.model.ListViewModel
import org.hse.smartcalendar.view.model.TaskEditViewModel

@Composable
fun SuggestionItem(listModel: ListViewModel, task: DailyTask) {
    val isAdded = rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.End
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            shadowElevation = 2.dp,
            color = MaterialTheme.colorScheme.secondary,
        ) {
            Column {
                DailyTaskCard(
                    task = task,
                    taskEditViewModel = TaskEditViewModel(listModel),
                    isSuggestion = true
                )

                Button(
                    enabled = !isAdded.value,
                    onClick = {
                        if (!isAdded.value) {
                            listModel.addDailyTask(task)
                            isAdded.value = true
                        }
                    },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.onSecondary,
                        disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    if (!isAdded.value) {
                        Text("Add")
                    } else {
                        Text("Added")
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SuggestionItemPreview() {
    val task = DailyTask(
        title = "Suggestion task",
        description = "Suggestion description",
        start = LocalTime(2, 28),
        end = LocalTime(22, 8)
    )
    SuggestionItem(ListViewModel(-1), task)
}