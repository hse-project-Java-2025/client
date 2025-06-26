package org.hse.smartcalendar.ui.task

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.data.SharedInfo
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.view.model.ReminderViewModel
import org.hse.smartcalendar.view.model.ReminderViewModelFactory
import org.hse.smartcalendar.ui.navigation.TopButton
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.utility.Screens
import org.hse.smartcalendar.utility.rememberNavigation
import org.hse.smartcalendar.view.model.ListViewModel
import org.hse.smartcalendar.view.model.StatisticsManager
import org.hse.smartcalendar.view.model.StatisticsViewModel
import org.hse.smartcalendar.view.model.TaskEditViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTasksList(
    viewModel: ListViewModel,
    taskEditViewModel: TaskEditViewModel,
    reminderModel: ReminderViewModel,
    openDrawer: () -> Unit,
    navigation: Navigation,
    navController: NavController
) {
    val audioFile: MutableState<File?> = rememberSaveable { mutableStateOf(null) }
    val taskTitle = rememberSaveable { mutableStateOf("") }
    val taskType = rememberSaveable { mutableStateOf(DailyTaskType.COMMON) }
    val taskDescription = rememberSaveable { mutableStateOf("") }
    val startTime = rememberSaveable { mutableIntStateOf(0) }
    val endTime = rememberSaveable { mutableIntStateOf(0) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val actionResult by viewModel.actionResult.observeAsState()
    val scope = rememberCoroutineScope()
    val isBottomSheetVisible = rememberSaveable { mutableStateOf(false) }
    var showLoading by remember { mutableStateOf(false) }
    LaunchedEffect(actionResult) {
        if (actionResult is NetworkResponse.Loading) {
            delay(500)
            if (actionResult is NetworkResponse.Loading) {
                showLoading = true
            }
        } else {
            showLoading = false
        }
    }
    Scaffold(
        topBar = {
            TopButton(
                openMenu = openDrawer,
                navigation = navigation,
                text = formatLocalDate(viewModel.getScheduleDate())
            )
        },
        bottomBar = { ListBottomBar(viewModel, scope, isBottomSheetVisible, sheetState) },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(viewModel.dailyTaskList) {
                    DailyTaskCard(
                        it,
                        onCompletionChange = {
                            if (!it.isComplete()){reminderModel.cancelReminder(it)}
                            else {reminderModel.scheduleReminder(it)}
                            viewModel.changeTaskCompletion(it, !it.isComplete())
                        },
                        taskEditViewModel = taskEditViewModel,
                        onLongPressAction = {
                            navController.navigate(Screens.EDIT_TASK.route)
                        }
                    )
                }
            }
            BottomSheet(
                isBottomSheetVisible = isBottomSheetVisible,
                sheetState = sheetState,
                onDismiss = {
                    scope.launch { sheetState.hide() }
                        .invokeOnCompletion { isBottomSheetVisible.value = false }
                },
                onRecordStop = {
                    viewModel.sendAudio(
                        audioFile = audioFile,
                        description = ListViewModel.AudioDescription.CONVERT_AUDIO,
                    )
                },
                audioFile = audioFile,
                taskTitle = taskTitle,
                taskType = taskType,
                taskDescription = taskDescription,
                startTime = startTime,
                endTime = endTime,
                viewModel = viewModel,
                addTask = { task ->
                    viewModel.addDailyTask(task);
                    reminderModel.scheduleReminder(task)
                }
            )
        }
    )

    if (showLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Text("Loading task on server", modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
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
    val statisticsViewModel: StatisticsViewModel = viewModel()
    val statisticsManager = StatisticsManager(statisticsViewModel)
    val listViewModel = ListViewModel(statisticsManager)
    listViewModel.addDailyTask(
        DailyTask(
            title = "sss",
            description = "sss",
            start = LocalTime(0, 0),
            end = LocalTime(1, 0),
            date = LocalDate(2022, 5, 4),
            sharedInfo = SharedInfo()
        )
    )
    val taskEditViewModel = TaskEditViewModel(
        listViewModel = listViewModel
    )
    val navController = rememberNavController()
    val reminderModel: ReminderViewModel = viewModel(factory = ReminderViewModelFactory(
        LocalContext.current.applicationContext as Application
    ))
    NavHost(navController, startDestination = Screens.CALENDAR.route) {
        composable(Screens.CALENDAR.route) {
            DailyTasksList(
                listViewModel,
                taskEditViewModel,
                reminderModel,
                {},
                rememberNavigation(),
                navController
            )
        }
        composable("EDIT_TASK") {
            TaskEditWindow(
                onSave = { navController.popBackStack() },
                onCancel = { navController.popBackStack() },
                taskEditViewModel = taskEditViewModel,
                navController = navController,
                onDelete = { },
                reminderModel = reminderModel
            )
        }
    }
    SmartCalendarTheme {
        DailyTasksList(listViewModel, taskEditViewModel,
            reminderModel, {}, rememberNavigation(), navController)
    }
}
