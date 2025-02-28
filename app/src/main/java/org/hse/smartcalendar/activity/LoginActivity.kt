package org.hse.smartcalendar.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hse.smartcalendar.ui.theme.SmartCalendartestTheme
import org.hse.smartcalendar.view.model.AuthViewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartCalendartestTheme {
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
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.login(username, password) },
            modifier = Modifier.fillMaxWidth().testTag("loginButtonTest")
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
                // TODO Видимо тут должна быть синхронизация.
                // Закоментил пока
//                val intent = Intent(LocalContext.current, DailyTasksListActivity::class.java)
//                LocalContext.current.startActivity(intent)
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
    SmartCalendartestTheme { AuthScreen(viewModel = AuthViewModel()) }
}