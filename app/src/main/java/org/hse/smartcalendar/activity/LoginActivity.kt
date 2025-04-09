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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.ViewModelProvider
import org.hse.smartcalendar.activity.NavigationActivity
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme

enum class AuthType(val title: String){
    Login("Login"),
    Register("Register")
}


class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        setContent {
            SmartCalendarTheme {
                AuthScreen(authViewModel, AuthType.Register)
            }
        }
    }
}
class LoginActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        setContent {
            SmartCalendarTheme {
                AuthScreen(authViewModel, AuthType.Login)
            }
        }
    }
}

@Composable
fun AuthScreen(viewModel: AuthViewModel, authType:AuthType) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val authState by viewModel.registerResult.observeAsState()
    val context = LocalContext.current
    Button(
        onClick = {val intent = Intent(context, NavigationActivity::class.java)
            context.startActivity(intent) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Calendar")
    }
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
            modifier = Modifier.fillMaxWidth(),
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
                    "signupButtonTest"
                    else
                    "loginButtonTest")
        ) {
            Text(authType.title)
        }
        when (val state = authState) {
            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {
                Text("Login successful! Token: ${state.data.id}")
                Thread.sleep(1000)
                val id = state.data.id
                val intent = Intent(LocalContext.current, NavigationActivity::class.java)
                intent.putExtra("id", id)
                context.startActivity(intent)
            }
            is NetworkResponse.Error -> {
                Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
            }
            else -> {}
        }
    }
}


@Preview
@Composable
fun AuthScreenPreview() {
    SmartCalendarTheme { AuthScreen(viewModel = AuthViewModel(), AuthType.Login) }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    SmartCalendarTheme { AuthScreen(viewModel = AuthViewModel(), AuthType.Register) }
}