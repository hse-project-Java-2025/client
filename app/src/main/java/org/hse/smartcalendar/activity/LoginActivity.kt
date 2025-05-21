package org.hse.smartcalendar

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hse.smartcalendar.activity.DailyTasksListActivity
import org.hse.smartcalendar.activity.NavigationActivity
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartCalendarTheme {
                AuthScreen(viewModel = AuthViewModel())
            }
        }
    }
}

@Composable
fun AuthScreen(viewModel: AuthViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {val intent = Intent(context, NavigationActivity::class.java)
                context.startActivity(intent) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calendar")
        }
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
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.login(username, password) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("loginButtonTest")
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (val state = authState) {
            is AuthViewModel.AuthState.Loading -> {
                CircularProgressIndicator()
            }
            is AuthViewModel.AuthState.Success -> {
                Text("Login successful! Token: ${state.token}")
                Thread.sleep(1000)
                val id: Long = 1488
                // TODO Видимо тут должна быть синхронизация.
                val intent = Intent(LocalContext.current, DailyTasksListActivity::class.java)
                intent.putExtra("id", id)
                LocalContext.current.startActivity(intent)
            }
            is AuthViewModel.AuthState.Error -> {
                Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
            }
            else -> {}
        }
    }
}


@Preview
@Composable
fun AuthScreenPreview() {
    SmartCalendarTheme { AuthScreen(viewModel = AuthViewModel()) }
}