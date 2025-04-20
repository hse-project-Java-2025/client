package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailySchedule
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.fromMinutesOfDay
import org.hse.smartcalendar.utility.toMinutesOfDay
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    isBottomSheetVisible: MutableState<Boolean>,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onRecordStop: () -> DailyTask? = { null },
    audioFile: MutableState<File?>,
    taskTitle: MutableState<String>,
    taskType: MutableState<DailyTaskType>,
    taskDescription: MutableState<String>,
    startTime: MutableState<Int>,
    endTime: MutableState<Int>,
    addTask: (DailyTask) -> Unit
) {
    val expendedTypeSelection = rememberSaveable { mutableStateOf(false) }
    val isConflictInTimeField = rememberSaveable { mutableStateOf(false) }
    val isEmptyTitle = rememberSaveable { mutableStateOf(false) }
    val isNestedTask = rememberSaveable { mutableStateOf(false) }
    val fstFiledHasFormatError = rememberSaveable { mutableStateOf(false) }
    val sndFiledHasFormatError = rememberSaveable { mutableStateOf(false) }

    val isErrorInRecorder = rememberSaveable { mutableStateOf(false) }

    if (isBottomSheetVisible.value) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismiss()
                isConflictInTimeField.value = false
                isEmptyTitle.value = false
                isNestedTask.value = false
                fstFiledHasFormatError.value = false
                sndFiledHasFormatError.value = false
            },
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
                Spacer(modifier = Modifier.padding(6.dp))

                if (isNestedTask.value) {
                    Box(Modifier.align(Alignment.CenterHorizontally)) {
                        Text(
                            text = "Conflict with previous task",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(6.dp))
                TextField(
                    value = taskTitle.value,
                    onValueChange = {
                        taskTitle.value = it
                        if (it.isNotEmpty()) {
                            isEmptyTitle.value = false
                        }
                    },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isEmptyTitle.value,
                    supportingText = {
                        if (isEmptyTitle.value) {
                            Text("Empty Title")
                        }
                    }
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
                    endTime = endTime,
                    isConflictInTimeField = isConflictInTimeField,
                    fstFiledHasFormatError = fstFiledHasFormatError,
                    sndFiledHasFormatError = sndFiledHasFormatError
                )

                Spacer(modifier = Modifier.padding(12.dp))

                Box(Modifier.align(Alignment.CenterHorizontally)) {
                    if (isErrorInRecorder.value) {
                        Text(
                            text = "Error in Audio",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    } else {
                        Text(
                            text = "",
                            fontSize = 12.sp
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    AudioRecorderButton(
                        modifier = Modifier.weight(1.0f),
                        audioFile = audioFile,
                        onStop = {
                            val task = onRecordStop()
                            if (task != null) {
                                isErrorInRecorder.value = false
                                taskTitle.value = task.getDailyTaskTitle()
                                taskDescription.value = task.getDailyTaskDescription()
                                taskType.value = task.getDailyTaskType()
                                startTime.value =
                                    LocalTime.toMinutesOfDay(task.getDailyTaskStartTime())
                                endTime.value = LocalTime.toMinutesOfDay(task.getDailyTaskEndTime())
                            } else {
                                isErrorInRecorder.value = true
                            }
                        }
                    )
                    Spacer(modifier = Modifier.padding(12.dp))
                    Button(
                        modifier = Modifier.weight(1.0f),
                        onClick = {
                            addNewTask(
                                addTask = addTask,
                                isBottomSheetVisible = isBottomSheetVisible,
                                taskTitle = taskTitle,
                                taskType = taskType,
                                taskDescription = taskDescription,
                                startTime = startTime,
                                endTime = endTime,
                                isConflictInTimeField = isConflictInTimeField,
                                isEmptyTitle = isEmptyTitle,
                                isNestedTask = isNestedTask,
                                fstFiledHasFormatError = fstFiledHasFormatError,
                                sndFiledHasFormatError = sndFiledHasFormatError
                            )
                        },
                    ) {
                        Text("Add task")
                    }
                }
            }
        }
    }
}

@Composable
fun TimeSelectorRow(
    modifier: Modifier = Modifier,
    startTime: MutableState<Int>,
    endTime: MutableState<Int>,
    isConflictInTimeField: MutableState<Boolean>,
    fstFiledHasFormatError: MutableState<Boolean>,
    sndFiledHasFormatError: MutableState<Boolean>,
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        TimeInputField(
            label = "start",
            modifier = Modifier.weight(1f),
            timeInMinutes = startTime,
            formatHasError = fstFiledHasFormatError
        )
        Text(
            text = "  due to",
            modifier = Modifier.weight(0.5f),
        )
        TimeInputField(
            label = "end",
            modifier = Modifier.weight(1f),
            timeInMinutes = endTime,
            isConflictInTimeField = isConflictInTimeField,
            formatHasError = sndFiledHasFormatError
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimeSelectorRowPreview() {
    val startTime = rememberSaveable { mutableIntStateOf(0) }
    val endTime = rememberSaveable { mutableIntStateOf(0) }
    val isConflictInTimeField = rememberSaveable { mutableStateOf(false) }
    val fstFiledHasFormatError = rememberSaveable { mutableStateOf(false) }
    val sndFiledHasFormatError = rememberSaveable { mutableStateOf(false) }

    MaterialTheme {
        TimeSelectorRow(
            startTime = startTime,
            endTime = endTime,
            isConflictInTimeField = isConflictInTimeField,
            fstFiledHasFormatError = fstFiledHasFormatError,
            sndFiledHasFormatError = sndFiledHasFormatError
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
    isConflictInTimeField: MutableState<Boolean>,
    isEmptyTitle: MutableState<Boolean>,
    isNestedTask: MutableState<Boolean>,
    fstFiledHasFormatError: MutableState<Boolean>,
    sndFiledHasFormatError: MutableState<Boolean>,
    ) {
    isConflictInTimeField.value = false
    isEmptyTitle.value = false
    if (startTime.value > endTime.value) {
        isConflictInTimeField.value = true
    }
    if (taskTitle.value.isEmpty()) {
        isEmptyTitle.value = true
    }
    if (isConflictInTimeField.value || isEmptyTitle.value) {
        return
    }

    val newTask = DailyTask(
            title = taskTitle.value,
            type = taskType.value,
            description = taskDescription.value,
            start = LocalTime.fromMinutesOfDay(startTime.value),
            end = LocalTime.fromMinutesOfDay(endTime.value),
        )

    try {
        addTask(
            newTask
        )
    } catch (exception: DailySchedule.NestedTaskException) {
        isNestedTask.value = true
        return
    }

    isBottomSheetVisible.value = false
    taskTitle.value = ""
    taskDescription.value = ""
    startTime.value = 0
    endTime.value = 0
    isConflictInTimeField.value = false
    isEmptyTitle.value = false
    isNestedTask.value = false
    fstFiledHasFormatError.value = false
    sndFiledHasFormatError.value = false

}

@Composable
fun TimeInputField(
    modifier: Modifier = Modifier,
    label: String = "",
    timeInMinutes: MutableState<Int>,
    isConflictInTimeField: MutableState<Boolean> = mutableStateOf(false),
    formatHasError: MutableState<Boolean>
) {
    var timeTextValue by remember { mutableStateOf(TextFieldValue("")) }
    TextField(
        value = timeTextValue,
        onValueChange = { newText ->
            val formattedText = formatTimeInput(newText.text)
            timeTextValue = TextFieldValue(formattedText, newText.selection)
            if (timeTextValue.selection == TextRange(3)) {
                timeTextValue = TextFieldValue(timeTextValue.text, TextRange(4))
            }
            if (formattedText.length == 5) {
                try {
                timeInMinutes.value = LocalTime.toMinutesOfDay(LocalTime.parse(formattedText))
                } catch (exception: IllegalArgumentException) {
                    val maxLocalTime = LocalTime(23, 59)
                    timeInMinutes.value = LocalTime.toMinutesOfDay(maxLocalTime)
                    timeTextValue = TextFieldValue(maxLocalTime.toString(), newText.selection)
                }
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        label = { Text(
            text = label
        ) },
        modifier = modifier,
        singleLine = true,
        isError = formatHasError.value || isConflictInTimeField.value,
        supportingText = {
            if (formatHasError.value) {
                Text("Invalid format")
            } else if (isConflictInTimeField.value) {
                Text("Time conflict")
            }
        }
    )
}

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
    val time = rememberSaveable { mutableIntStateOf(0) }
    val isConflictInTimeField = rememberSaveable { mutableStateOf(false) }
    val formatHasError = rememberSaveable { mutableStateOf(false) }
    SmartCalendarTheme {
            TimeInputField(
                label = "time",
                timeInMinutes = time,
                isConflictInTimeField = isConflictInTimeField,
                formatHasError = formatHasError
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun BottomSheetPreview() {
    SmartCalendarTheme {
        val audioFile: MutableState<File?> = rememberSaveable { mutableStateOf(null) }
        val taskTitle = rememberSaveable { mutableStateOf("") }
        val taskType = rememberSaveable { mutableStateOf(DailyTaskType.COMMON) }
        val taskDirection = rememberSaveable { mutableStateOf("") }
        val startTime = rememberSaveable { mutableIntStateOf( 0) }
        val endTime = rememberSaveable { mutableIntStateOf( 0) }
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        val scope = rememberCoroutineScope()
        val isBottomSheetVisible = rememberSaveable { mutableStateOf(true) }

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
            taskType = taskType,
            audioFile = audioFile,
        )
    }
}