package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.fromMinutesOfDay
import org.hse.smartcalendar.utility.toMinutesOfDay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    isBottomSheetVisible: MutableState<Boolean>,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    taskTitle: MutableState<String>,
    taskType: MutableState<DailyTaskType>,
    taskDescription: MutableState<String>,
    startTime: MutableState<Int>,
    endTime: MutableState<Int>,
    addTask: (DailyTask) -> Unit
) {
    val expendedTypeSelection = remember { mutableStateOf(false) }
    if (isBottomSheetVisible.value) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
            dragHandle = null,
            scrimColor = Color.Black.copy(alpha = .5f),
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            Column (
                modifier = Modifier
                    //.navigationBarsPadding()
                    .padding(12.dp) // Outer padding
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .padding(12.dp) // Inner padding
            ) {
                Spacer(modifier = Modifier.padding(12.dp))
                TextField(
                    value = taskTitle.value,
                    onValueChange = { taskTitle.value = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.padding(12.dp))
                ExposedTypeSelectionMenu(
                    expanded = expendedTypeSelection,
                    type = taskType
                )
                Spacer(modifier = Modifier.padding(12.dp))
                TextField(
                    value = taskDescription.value,
                    onValueChange = { taskDescription.value = it },
                    label = { Text("Task Description") },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.padding(12.dp))
                TimeSelectorRow(
                    startTime = startTime,
                    endTime = endTime
                )

                Spacer(modifier = Modifier.padding(12.dp))
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {addNewTask(
                        addTask = addTask,
                        isBottomSheetVisible = isBottomSheetVisible,
                        taskTitle = taskTitle,
                        taskType = taskType,
                        taskDescription = taskDescription,
                        startTime = startTime,
                        endTime = endTime
                    )}

                ) {
                    Text("Add task")
                }
            }
        }
    }
}

@Composable
fun TimeSelectorRow(
    modifier: Modifier = Modifier,
    startTime: MutableState<Int>,
    endTime: MutableState<Int>
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        TimeInputField(
            text = "start",
            modifier = Modifier.weight(1f),
            time = startTime
        )
        Text(text = "  due to",
            modifier = Modifier.weight(0.5f))
        TimeInputField(
            text = "end",
            modifier = Modifier.weight(1f),
            time = endTime
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimeSelectorRowPreview() {
    val startTime = rememberSaveable { mutableIntStateOf(0) }
    val endTime = rememberSaveable { mutableIntStateOf(0) }
    MaterialTheme {
        TimeSelectorRow(
            startTime = startTime,
            endTime = endTime
        )
    }
}

fun addNewTask(
    addTask: (DailyTask) -> Unit,
    isBottomSheetVisible: MutableState<Boolean>,
    taskTitle: MutableState<String>,
    taskType: MutableState<DailyTaskType>,
    taskDescription: MutableState<String>,
    startTime: MutableState<Int>,
    endTime: MutableState<Int>,
    ) {
    addTask(
        DailyTask(
            title = taskTitle.value,
            type = taskType.value,
            description = taskDescription.value,
            start = LocalTime.fromMinutesOfDay(startTime.value),
            end = LocalTime.fromMinutesOfDay(endTime.value),
        )
    )
    isBottomSheetVisible.value = false
    taskTitle.value = ""
    taskDescription.value = ""
    startTime.value = 0
    endTime.value = 0

}


//TODO Добавить обработку не валидных случаев
@Composable
fun TimeInputField(
    modifier: Modifier = Modifier,
    text: String = "",
    time: MutableState<Int>
) {
    var timeText by remember { mutableStateOf(TextFieldValue("")) }
    TextField(
        value = timeText,
        onValueChange = { newText ->
            val formattedText = formatTimeInput(newText.text)
            timeText = TextFieldValue(formattedText, newText.selection)
            if (formattedText.length == 5) {
                time.value = LocalTime.toMinutesOfDay(LocalTime.parse(formattedText))
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        label = { Text(
            text = text
        ) },
        modifier = modifier,
        singleLine = true
    )
}

// TODO Попытаться написать перенос.
fun formatTimeInput(input: String): String {
    val digitsOnly = input.filter { it.isDigit() }
    val formatted = StringBuilder()

    for (i in digitsOnly.indices) {
        if (i == 2) {
            formatted.append(':')
        }
        if (i >= 4) {
            break
        }
        formatted.append(digitsOnly[i])
    }

    return formatted.toString()
}

@Preview(showBackground = true)
@Composable
fun TimeInputFieldPreview() {
    val time = remember { mutableIntStateOf(0) }
    MaterialTheme {
            TimeInputField(
                text = "time",
                time = time
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun BottomSheetPreview() {
    SmartCalendarTheme {
        val taskTitle = rememberSaveable { mutableStateOf("") }
        val taskType = rememberSaveable { mutableStateOf(DailyTaskType.COMMON) }
        val taskDirection = rememberSaveable { mutableStateOf("") }
        val startTime = rememberSaveable { mutableIntStateOf( 0) }
        val endTime = rememberSaveable { mutableIntStateOf( 0) }
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        val scope = rememberCoroutineScope()
        val isBottomSheetVisible = remember { mutableStateOf(true)}

        BottomSheet(
            isBottomSheetVisible = isBottomSheetVisible,
            sheetState = sheetState,
            onDismiss = {
                scope.launch { sheetState.hide() }
                .invokeOnCompletion { isBottomSheetVisible.value = false }
            },
            taskTitle = taskTitle,
            taskDescription = taskDirection,
            startTime = startTime,
            endTime = endTime,
            addTask = {},
            taskType = taskType
        )
    }
}