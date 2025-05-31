package org.hse.smartcalendar.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
fun LoadingScreen(navigation: Navigation){
    val viewModel: InitViewModel = viewModel()//гарантирует 1 модель на Composable
    val initState by viewModel.initResult.observeAsState()
    LaunchedEffect(Unit) {
        viewModel.initUser()
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
                viewModel.initUser()
            },
        ) {
            Text("Retry")
        }
        when (val state = initState) {
            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {
                LaunchedEffect(state) {
                    delay(1000)
                    navigation.navigateToMainApp(Screens.CALENDAR.route)
                }
            }
            is NetworkResponse.Error -> {
                Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
            }
            is NetworkResponse.NetworkError -> {
                Text("Check internet connection: ${state.exceptionMessage}", color = MaterialTheme.colorScheme.error)
            }
            null -> {}
        }
    }
    //При успехе
//    LaunchedEffect(state) {
//        delay(1000)
//        navigation.navigateToMainApp(Screens.CALENDAR.route)
//    }
}