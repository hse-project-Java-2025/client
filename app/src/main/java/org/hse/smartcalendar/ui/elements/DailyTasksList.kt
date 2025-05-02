package org.hse.smartcalendar.ui.elements

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.notification.ReminderViewModel
import org.hse.smartcalendar.notification.ReminderViewModelFactory
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.utility.rememberNavigation
import org.hse.smartcalendar.view.model.ListViewModel
import org.hse.smartcalendar.view.model.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTasksList(viewModel: ListViewModel, openDrawer: ()->Unit, navigation: Navigation,
                   settingsModel: SettingsViewModel) {
    val reminderModel: ReminderViewModel = viewModel(
        factory = ReminderViewModelFactory(
            LocalContext.current.applicationContext as Application
        )
    )
    val taskTitle = rememberSaveable { mutableStateOf("") }
    val taskType = rememberSaveable { mutableStateOf(DailyTaskType.COMMON) }
    val taskDescription = rememberSaveable { mutableStateOf("") }
    val startTime = rememberSaveable { mutableIntStateOf( 0) }
    val endTime = rememberSaveable { mutableIntStateOf( 0) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    fun serverAddTask(dailyTask: DailyTask){
    }
    val scope = rememberCoroutineScope()
    val isBottomSheetVisible = rememberSaveable { mutableStateOf(false) }
    Scaffold (
        topBar = {
            TopButton(openMenu = openDrawer, navigation = navigation, text = formatLocalDate(viewModel.getScheduleDate()))
        },
        bottomBar = { ListBottomBar(viewModel, scope, isBottomSheetVisible, sheetState) },
        content = { paddingValues ->
            LazyColumn (modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                items(viewModel.dailyTaskList) {
                    DailyTaskCard(it, onCompletionChange = {
                        viewModel.changeTaskCompletion(it, !it.isComplete())
                    })
                }
            }
            BottomSheet(
                isBottomSheetVisible = isBottomSheetVisible,
                sheetState = sheetState,
                onDismiss = {
                    scope.launch { sheetState.hide() }
                        .invokeOnCompletion { isBottomSheetVisible.value = false }
                },
                taskTitle = taskTitle,
                taskType = taskType,
                taskDescription = taskDescription,
                startTime = startTime,
                endTime = endTime,
                viewModel = viewModel,
                addTask = {task -> viewModel.addDailyTask(task); serverAddTask(task);
                    reminderModel.scheduleReminder(task, 10, settingsModel)
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTopBar(date: LocalDate) {
    TopAppBar(
        title = {
            Text(formatLocalDate(date))
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            scrolledContainerColor = MaterialTheme.colorScheme.error,
            navigationIconContentColor = MaterialTheme.colorScheme.error,
            titleContentColor = MaterialTheme.colorScheme.outline,
            actionIconContentColor = MaterialTheme.colorScheme.error
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListBottomBar(
    viewModel: ListViewModel,
    scope: CoroutineScope,
    isBottomSheetVisible: MutableState<Boolean>,
    sheetState: SheetState
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
                    viewModel.moveToPreviousDailySchedule()
                },
                Modifier
                    .testTag("moveToPreviousScheduleButton")
                    .weight(1f)
            ) {
                Text("Previous")
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Button(
                onClick = {
                    scope.launch {
                        isBottomSheetVisible.value = true
                        sheetState.expand()
                    }
                },
                Modifier
                    .testTag("addDailyTaskButton")
                    .weight(1f)
            ) {
                Text(text = "Create task")
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Button(
                onClick = { viewModel.moveToNextDailySchedule() },
                modifier = Modifier
                    .testTag("moveToNextScheduleButton")
                    .weight(1f)
            ) {
                Text(text = "Next")
            }
        }
    }
}

fun formatLocalDate(date: LocalDate): String {
    val formatter = LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        dayOfMonth()
        chars(", ")
        year()
    }
    return date.format(formatter)
}

@Composable
@Preview(showBackground = true)
fun DailyTaskListPreview() {
    val viewModelPreview = ListViewModel(1488)
    SmartCalendarTheme {
        DailyTasksList(viewModelPreview, {}, rememberNavigation(), SettingsViewModel())
    }
}