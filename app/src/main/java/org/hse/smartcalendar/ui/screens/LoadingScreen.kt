package org.hse.smartcalendar.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.utility.Screens
import org.hse.smartcalendar.view.model.InitViewModel
import org.hse.smartcalendar.view.model.StatisticsViewModel
import org.hse.smartcalendar.view.model.ListViewModel
@Composable
fun LoadingScreen(navigation: Navigation, statisticsVM: StatisticsViewModel, listModel: ListViewModel){
    val initVM: InitViewModel = viewModel()//гарантирует 1 модель на Composable
    val initState by initVM.initResult.observeAsState()
    val statisticsState by statisticsVM.initResult.observeAsState()

    LaunchedEffect(Unit) {
        initVM.initUser()
    }
    LaunchedEffect(initState) {
        if (initState is NetworkResponse.Success) {
            statisticsVM.init()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                initVM.initUser()
            },
        ) {
            Text("Retry")
        }
        when  {
            initState is NetworkResponse.Loading || initState is NetworkResponse.Success && statisticsState is NetworkResponse.Loading-> {
                CircularProgressIndicator()
                Text("Loading user data")
            }
            initState is NetworkResponse.Success && statisticsState is NetworkResponse.Success-> {
                LaunchedEffect(statisticsState) {
                    delay(1000)
                    listModel.loadDailyTasks()
                    navigation.navigateToMainApp(Screens.CALENDAR.route)
                }
            }
            initState is NetworkResponse.Error -> {
                Text("Error: ${(initState as NetworkResponse.Error).message}", color = MaterialTheme.colorScheme.error)
            }
            initState is NetworkResponse.NetworkError -> {
                Text("Check internet connection: ${(initState as NetworkResponse.NetworkError).exceptionMessage}", color = MaterialTheme.colorScheme.error)
            }
            statisticsState is NetworkResponse.Error -> {
                Text("Error loading statistics: ${(statisticsState as NetworkResponse.Error).message}", color = MaterialTheme.colorScheme.error)
            }
            statisticsState is NetworkResponse.NetworkError -> {
                Text("Statistics: check connection: ${(statisticsState as NetworkResponse.NetworkError).exceptionMessage}", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}