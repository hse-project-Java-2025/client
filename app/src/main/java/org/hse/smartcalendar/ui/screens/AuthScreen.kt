package org.hse.smartcalendar

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.utility.Screens
import org.hse.smartcalendar.utility.rememberNavigation

enum class AuthType(val title: String){
    Login("Login"),
    Register("Register")
}


@Composable
fun AuthScreen(navigation: Navigation, viewModel: AuthViewModel, authType:AuthType) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val loginState by viewModel.loginResult.observeAsState()
    val coroutineScope = rememberCoroutineScope()
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
            modifier = Modifier
                .fillMaxWidth()
                .testTag("loginField")
        )
        if (authType == AuthType.Register){
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("passwordField"),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { if (authType==AuthType.Register)
                viewModel.signup(username, email, password)
            else
                viewModel.login(username, password)},
            modifier = Modifier
                .fillMaxWidth()
                .testTag(if (authType==AuthType.Register)
                    "signupSubmitButton"
                else
                    "loginSubmitButton")
        ) {
            Text(authType.title)
        }
        when (val state = loginState) {
            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {
                Text("Login successful!")
                navigation.navigateTo(Screens.LOADING.route)
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
@Preview
@Composable
fun AuthScreenPreview() {
    SmartCalendarTheme { AuthScreen(rememberNavigation(), viewModel = AuthViewModel(), AuthType.Login) }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    SmartCalendarTheme { AuthScreen(rememberNavigation(), viewModel = AuthViewModel(), AuthType.Register) }
}