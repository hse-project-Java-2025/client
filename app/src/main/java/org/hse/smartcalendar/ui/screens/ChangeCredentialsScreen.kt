package org.hse.smartcalendar.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.hse.smartcalendar.AuthViewModel
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.utility.rememberNavigation

@Preview
@Composable
fun ChangePassword() {
    SmartCalendarTheme { ChangePassword(viewModel = AuthViewModel(), rememberNavigation()) }
}

@Preview
@Composable
fun ChangeLogin() {
    SmartCalendarTheme { ChangeLogin(viewModel = AuthViewModel(), rememberNavigation()) }
}

@Composable
fun ChangeLogin(viewModel: AuthViewModel, navigation: Navigation) {
    ChangePassword(viewModel, navigation, true)
}

@Composable
fun ChangePassword(viewModel: AuthViewModel, navigation: Navigation, isChangeLogin: Boolean = false) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var newPassword1 by remember { mutableStateOf("") }
    var newPassword2 by remember { mutableStateOf("") }
    val credentialsState by viewModel.changeCredentialsResult.observeAsState()
    val credentialsName = if (isChangeLogin) "login" else "password"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = newPassword1,
            onValueChange = { newPassword1 = it },
            label = { Text("New $credentialsName") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        TextField(
            value = newPassword2,
            onValueChange = { newPassword2 = it },
            label = {Text("Repeat new $credentialsName")},
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (isChangeLogin) viewModel.changeLogin(username, password, newPassword1, newPassword2) else
                    viewModel.changePassword(username, password, newPassword1, newPassword2)},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change $credentialsName")
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (val state = credentialsState) {
            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {
                Text("Change $credentialsName successful")
                LaunchedEffect(state) {
                    delay(1000)
                    navigation.upPress()
                    viewModel.changeCredentialsResult.value = null
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
}