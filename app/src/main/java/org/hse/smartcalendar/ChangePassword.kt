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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.hse.smartcalendar.ui.theme.SmartCalendartestTheme

@Preview
@Composable
fun ChangePassword() {
    SmartCalendartestTheme { ChangePassword(viewModel = AuthViewModel(), navController = rememberNavController()) }
}

@Preview
@Composable
fun ChangeLogin() {
    SmartCalendartestTheme { ChangeLogin(viewModel = AuthViewModel(), navController = rememberNavController()) }
}

@Composable
fun ChangeLogin(viewModel: AuthViewModel, navController: NavController) {
    ChangePassword(viewModel, navController)
}

@Composable
fun ChangePassword(viewModel: AuthViewModel, navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var newPassword1 by remember { mutableStateOf("") }
    var newPassword2 by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        TextField(
//            value = username,
//            onValueChange = { username = it },
//            label = { Text("Username") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        TextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            modifier = Modifier.fillMaxWidth(),
//            visualTransformation = PasswordVisualTransformation()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        TextField(
//            value = newPassword1,
//            onValueChange = { newPassword1 = it },
//            label = { if (isChangeLogin) Text("New login") else {Text("New password")} },
//            modifier = Modifier.fillMaxWidth(),
//            visualTransformation = PasswordVisualTransformation()
//        )
//        TextField(
//            value = newPassword2,
//            onValueChange = { newPassword2 = it },
//            label = { if (isChangeLogin) Text("Repeat new login") else {Text("Repeat new password")} },
//            modifier = Modifier.fillMaxWidth(),
//            visualTransformation = PasswordVisualTransformation()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = {
//                if (isChangeLogin) viewModel.changeLogin(username, password, newPassword1, newPassword2) else
//                viewModel.changePassword(username, password, newPassword1, newPassword2)},
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            if (isChangeLogin) Text("Change login") else Text("Change password")
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        when (val state = authState) {
//            is AuthViewModel.AuthState.Loading -> {
//                CircularProgressIndicator()
//            }
//            is AuthViewModel.AuthState.Success -> {
//                Text("Change password successful")
//            }
//            is AuthViewModel.AuthState.Error -> {
//                Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
//            }
//            else -> {}
//        }
//    }
}