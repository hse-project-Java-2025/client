package org.hse.smartcalendar.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.hse.smartcalendar.data.SuggestedTask
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.ui.elements.AudioRecorderButton
import org.hse.smartcalendar.ui.elements.SuggestedTaskItem
import org.hse.smartcalendar.ui.elements.UserMessageBox
import org.hse.smartcalendar.ui.navigation.TopButton
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.utility.rememberNavigation
import org.hse.smartcalendar.view.model.ChatGptViewModel
import org.hse.smartcalendar.view.model.ListViewModel
import org.hse.smartcalendar.view.model.StatisticsManager
import org.hse.smartcalendar.view.model.StatisticsViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatGptScreen(
    navigation: Navigation,
    openMenu: () -> Unit,
    viewModel: ChatGptViewModel,
    listViewModel: ListViewModel,
) {
    val audioFile: MutableState<File?> = rememberSaveable { mutableStateOf(null) }
    val onRecordStop: () -> Unit = {audioFile.value?.let { file ->
        viewModel.fetchSuggestionsAudio(file)
    }}
    val chatHistory = viewModel.chatHistory
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var query by remember { mutableStateOf("") }
    val chatResult by viewModel.chatResult.observeAsState()
    var showLoading by remember { mutableStateOf(false) }

    LaunchedEffect(chatResult) {
        if (chatResult is NetworkResponse.Loading) {
            delay(500)
            if (chatResult is NetworkResponse.Loading) {
                showLoading = true
            }
        } else {
            showLoading = false
        }
    }
    Scaffold(
        topBar = { TopButton(openMenu, navigation, "Ai assistant") },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(onClick = { viewModel.clearHistory() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = Color.DarkGray
                    )) {
                    Text("Clear dialogue")
                }
            }
            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(chatHistory) {queryIndex, (question, tasks) ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        UserMessageBox("Q: $question")
                        tasks.forEachIndexed { taskIndex, suggestedTask ->
                            if (suggestedTask.status!= SuggestedTask.TaskStatus.DECLINED) {
                                SuggestedTaskItem(
                                    suggestedTask = suggestedTask,
                                    onAccept = {
                                        if (suggestedTask.status == SuggestedTask.TaskStatus.PENDING) {
                                            val notNested = viewModel.acceptTask(suggestedTask)
                                            scope.launch {
                                                if (notNested) {
                                                    listViewModel.addDailyTask(suggestedTask.task)
                                                    snackbarHostState.showSnackbar("Task accepted")
                                                } else {
                                                    snackbarHostState.showSnackbar("Time conflict with existing task")
                                                }
                                            }
                                            notNested
                                        } else false
                                    },
                                    onDecline = {
                                        if (suggestedTask.status == SuggestedTask.TaskStatus.PENDING) {
                                            viewModel.declineTask(suggestedTask)
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Task declined")
                                            }
                                        }
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                AudioRecorderButton(
                    modifier = Modifier,
                    audioFile = audioFile,
                    onStop = onRecordStop,
                    buttonColors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = Color.DarkGray
                ),
                    iconColor = Color.DarkGray
                )
            }
            Row {
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Write question") },
                    colors = OutlinedTextFieldDefaults.colors( unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer )
                )
                Button(shape = RectangleShape,
                    onClick = {
                    if (query.isNotBlank()) {
                        viewModel.fetchSuggestions(query)
                        query = ""
                    }},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = Color.DarkGray
                    ))
                 {
                    Text("Send")
                }
            }

            if (showLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ChatGptScreenPreview() {
    SmartCalendarTheme {
        val statisticsViewModel: StatisticsViewModel = viewModel ()
        val listViewModel: ListViewModel = ListViewModel(StatisticsManager(statisticsViewModel))
        val chatGptViewModel: ChatGptViewModel = viewModel()
        ChatGptScreen(
            navigation = rememberNavigation(),
            viewModel = chatGptViewModel,
            openMenu = {},
            listViewModel = listViewModel
        )
    }
}