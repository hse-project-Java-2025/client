package org.hse.smartcalendar.ui.task

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material3.Text
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.utility.Screens
import org.hse.smartcalendar.utility.fromMinutesOfDay
import org.hse.smartcalendar.utility.toMinutesOfDay
import org.hse.smartcalendar.view.model.ListViewModel
import org.hse.smartcalendar.view.model.StatisticsManager
import org.hse.smartcalendar.view.model.StatisticsViewModel
import org.hse.smartcalendar.view.model.TaskEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskEditWindow(
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onDelete: (DailyTask) -> Unit,
    taskEditViewModel: TaskEditViewModel,
    navController: NavController
) {
    val listViewModel = hiltViewModel<ListViewModel>()
    val taskState = taskEditViewModel.getTask()
    val titleState = remember { mutableStateOf(taskState.getDailyTaskTitle()) }
    val taskType = rememberSaveable { mutableStateOf(taskState.getDailyTaskType()) }
    val descriptionState = remember { mutableStateOf(taskState.getDailyTaskDescription()) }
    val startTime =
        rememberSaveable { mutableIntStateOf(LocalTime.toMinutesOfDay(taskState.getDailyTaskStartTime())) }
    val endTime =
        rememberSaveable { mutableIntStateOf(LocalTime.toMinutesOfDay(taskState.getDailyTaskEndTime())) }

    val isConflictInTimeField = rememberSaveable { mutableStateOf(false) }
    val isEmptyTitle = rememberSaveable { mutableStateOf(false) }
    val isNestedTask = rememberSaveable { mutableStateOf(false) }
    val fstFiledHasFormatError = rememberSaveable { mutableStateOf(false) }
    val sndFiledHasFormatError = rememberSaveable { mutableStateOf(false) }
    val expendedTypeSelection = rememberSaveable { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            TaskEditBottomBar(
                onSave = {
                    taskEditViewModel.changes.setDailyTaskStartTime(
                        LocalTime.fromMinutesOfDay(
                            startTime.intValue
                        )
                    )
                    taskEditViewModel.changes.setDailyTaskEndTime(LocalTime.fromMinutesOfDay(endTime.intValue))
                    taskEditViewModel.changes.setDailyTaskType(taskType.value)
                    taskEditViewModel.updateInnerTask(
                        listViewModel = listViewModel,
                        isEmptyTitle,
                        isConflictInTimeField,
                        isNestedTask
                    )
                    onSave()
                    if (!isEmptyTitle.value && !isConflictInTimeField.value && !isNestedTask.value) {
                        navController.navigate(Screens.CALENDAR.route)
                    }
                },
                onCancel = {
                    onCancel()
                    navController.navigate(Screens.CALENDAR.route)
                },
                onDelete = {
                    onDelete(taskEditViewModel.getTask())
                    navController.navigate(Screens.CALENDAR.route)
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    //.navigationBarsPadding()
                    .padding(12.dp) // Outer padding
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .padding(12.dp) // Inner padding
            )
            {
                TextField(
                    value = titleState.value,
                    onValueChange = {
                        titleState.value = it
                        taskEditViewModel.changes.setDailyTaskTitle(it)
                    },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isEmptyTitle.value,
                    supportingText = {
                        if (isEmptyTitle.value) {
                            androidx.compose.material3.Text("Empty Title")
                        }
                    }
                )

                Spacer(modifier = Modifier.padding(12.dp))
                ExposedTypeSelectionMenu(
                    expanded = expendedTypeSelection,
                    type = taskType
                )
                Spacer(modifier = Modifier.padding(5.dp))
                TextField(
                    value = descriptionState.value,
                    onValueChange = {
                        descriptionState.value = it
                        taskEditViewModel.changes.setDailyTaskDescription(it)
                    },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(12.dp))

                TimeSelectorRow(
                    startTime = startTime,
                    endTime = endTime,
                    isConflictInTimeField = isConflictInTimeField,
                    fstFiledHasFormatError = fstFiledHasFormatError,
                    sndFiledHasFormatError = sndFiledHasFormatError
                )

                if (isNestedTask.value) {
                    Box(Modifier.align(Alignment.CenterHorizontally)) {
                        androidx.compose.material3.Text(
                            text = "Conflict with previous task",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun TaskEditWindowPreview() {
    TaskEditWindow(
        onSave = { },
        onCancel = { },
        onDelete = { },
        taskEditViewModel = hiltViewModel(),
        navController = NavController(
            LocalContext.current
        )
    )
}

@Composable
fun TaskEditBottomBar(
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit,
) {
    BottomAppBar(
        containerColor = Color.LightGray,
        contentColor = Color.White,
        modifier = Modifier.height(100.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .align(Alignment.Bottom),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    onDelete()
                },
                modifier = Modifier
                    .testTag("deleteTaskButton")
                    .weight(1f)
            ) {
                androidx.compose.material3.Text("Delete")
            }

            Spacer(modifier = Modifier.padding(5.dp))
            Button(
                onClick = {
                    onSave()
                },
                modifier = Modifier
                    .testTag("updateTaskButton")
                    .weight(1f)
            ) {
                androidx.compose.material3.Text("Update")
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Button(
                onClick = {
                    onCancel()
                },
                modifier = Modifier
                    .testTag("cancelEditButton")
                    .weight(1f)
            ) {
                androidx.compose.material3.Text("Cancel")
            }
        }
    }
}