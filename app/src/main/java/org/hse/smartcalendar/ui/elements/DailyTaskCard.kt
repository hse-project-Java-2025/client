package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.view.model.ListViewModel
import org.hse.smartcalendar.view.model.TaskEditViewModel


@Composable
fun DailyTaskCard(
    task: DailyTask,
    modifier: Modifier = Modifier,
    onCompletionChange: () -> Unit = { },
    onLongPressAction: () -> Unit = { },
    taskEditViewModel: TaskEditViewModel
) {
    Column(modifier = Modifier
        .padding(5.dp)
        .pointerInput(Unit) {
            detectTapGestures(
                onLongPress = {
                    taskEditViewModel.setTask(task)
                    onLongPressAction()
                }
            )
        }) {

    Surface(
        color = getCardTitleColor(task),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
        tonalElevation = 2.dp,
        shadowElevation = 10.dp,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            var isComplete by remember { mutableStateOf(task.isComplete()) }
            RadioButton(
                onClick = {
                    onCompletionChange()
                    isComplete = !isComplete
                },
                selected = isComplete,
                modifier = Modifier,
                enabled = true,
                colors = RadioButtonColors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.secondary,
                    disabledSelectedColor = Color.Blue,
                    disabledUnselectedColor = Color.Red
                )
            )
            Text(
                text = task.getDailyTaskTitle(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight(400),
                modifier = Modifier.weight(3f)
            )
            Text(
                text = task.getDailyTaskArrangementString(),
                modifier = Modifier
                    .weight(2f)
                    .padding(10.dp)
                    .align(Alignment.Bottom),
                textAlign = TextAlign.End
            )
        }
    }
        Surface(
            color = getCardDescriptionColor(task),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
            tonalElevation = 2.dp,
            shadowElevation = 10.dp,
            modifier = modifier
        ) {
            Spacer(modifier = Modifier.height(42.dp))
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
                Text(
                    text = task.getDailyTaskDescription(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun DailyTaskCardPreview() {
    val taskEditViewModel = TaskEditViewModel(listViewModel = ListViewModel(1488))
    val previewCommonTask = DailyTask(
        title = "Common title example",
        type = DailyTaskType.COMMON,
        description = "Common description Example",
        start = LocalTime(4, 0),
        end = LocalTime(5, 0),
    )


    val previewFitnessTask = DailyTask(
        title = "Fitness title example",
        type = DailyTaskType.FITNESS,
        description = "Fitness description Example",
        start = LocalTime(4, 0),
        end = LocalTime(5, 0),
    )

    val previewWorkTask = DailyTask(
        title = "Work title example",
        type = DailyTaskType.WORK,
        description = "Work description Example",
        start = LocalTime(4, 0),
        end = LocalTime(5, 0),
    )

    val previewStudiesTask = DailyTask(
        isComplete = true,
        title = "Studies title example",
        type = DailyTaskType.STUDIES,
        description = "Studies description Example",
        start = LocalTime(4, 0),
        end = LocalTime(5, 0),
    )

    Column {
        DailyTaskCard(
            task = previewCommonTask,
            onCompletionChange = { previewCommonTask.setCompletion(!previewCommonTask.isComplete()) },
            taskEditViewModel = taskEditViewModel
        )
        DailyTaskCard(
            task = previewFitnessTask,
            onCompletionChange = { previewFitnessTask.setCompletion(!previewFitnessTask.isComplete()) },
            taskEditViewModel = taskEditViewModel
        )
        DailyTaskCard(
            task = previewWorkTask,
            onCompletionChange = { previewWorkTask.setCompletion(!previewWorkTask.isComplete()) },
            taskEditViewModel = taskEditViewModel
        )
        DailyTaskCard(
            task = previewStudiesTask,
            onCompletionChange = { previewStudiesTask.setCompletion(!previewStudiesTask.isComplete()) },
            taskEditViewModel = taskEditViewModel
        )
    }
}

fun getCardTitleColor(task: DailyTask): Color {
    return when (task.getDailyTaskType()) {
        DailyTaskType.FITNESS -> Color.Red
        DailyTaskType.WORK -> Color.Gray
        DailyTaskType.STUDIES -> Color.Blue
        else -> Color.White
    }
}


fun getCardDescriptionColor(task: DailyTask): Color {
    return when (task.getDailyTaskType()) {
        DailyTaskType.FITNESS -> Color.Red
        DailyTaskType.WORK -> Color.DarkGray
        DailyTaskType.STUDIES -> Color.Green
        else -> Color.White
    }
}