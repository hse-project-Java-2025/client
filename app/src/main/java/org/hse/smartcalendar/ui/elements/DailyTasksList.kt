package org.hse.smartcalendar.ui.elements

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.hse.smartcalendar.view.model.ListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTasksList(viewModel: ListViewModel) {
    val taskTitle = rememberSaveable { mutableStateOf("") }
    val taskDescription = rememberSaveable { mutableStateOf("") }
    val startTime = rememberSaveable { mutableIntStateOf( 0) }
    val endTime = rememberSaveable { mutableIntStateOf( 0) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    val isBottomSheetVisible = rememberSaveable { mutableStateOf(false) }

    Scaffold (
        bottomBar = {
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

                    Spacer(
                        modifier = Modifier.padding(5.dp)
                    )

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

                    Spacer(
                        modifier = Modifier.padding(5.dp)
                    )

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
    )  { paddingValues ->
        LazyColumn (modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            items(viewModel.dailyTaskList) {
                DailyTaskCard(it)
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
            taskDescription = taskDescription,
            startTime = startTime,
            endTime = endTime,
            addTask = viewModel::addDailyTask
        )
    }
}

val viewModelPreview = ListViewModel(1488)

@Composable
@Preview
fun DailyTaskListPreview() {
    DailyTasksList(viewModelPreview)
}