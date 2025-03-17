package org.hse.smartcalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.hse.smartcalendar.ui.elements.ActionTopAppbar
import org.hse.smartcalendar.ui.elements.ChangeLogin
import org.hse.smartcalendar.ui.elements.ChangePassword
import org.hse.smartcalendar.ui.elements.NavigationScreen
import org.hse.smartcalendar.ui.elements.NavigationSheet
import org.hse.smartcalendar.ui.elements.SettingsScreen
import org.hse.smartcalendar.ui.elements.Statistics
import org.hse.smartcalendar.ui.theme.SmartCalendartestTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCalendartestTheme {
                setContent {
                    App(viewModel = AuthViewModel())
                }
            }
        }
    }
}

enum class Screen {
    Login,
    Calendar,
    Greeting,
    Settings,
    ChangePassword,
    ChangeLogin,
    Statistics,
    Rating,
    Achievements,
    Navigation,
    MyCalendars,
    AIAssistant
}

@Composable
fun App(
    viewModel: AuthViewModel,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Greeting.name
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.Greeting.name
    )
    val scope = rememberCoroutineScope()
    val isNavSheetVisible = rememberSaveable { mutableStateOf(false) }
    //верхняя кнопка и лист навигации во всём приложении
    Scaffold(
        topBar = { ActionTopAppbar(openMenu = {isNavSheetVisible.value = true}, elevation = 8.dp, navController = navController)
            NavigationSheet(navController, isNavSheetVisible)},
    )
    { innerPadding->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ){
            composable(route=Screen.Greeting.name) {
                Greeting("user", navController)
            }
            composable(route = Screen.Login.name){
                AuthScreen(viewModel, navController)
            }
            composable(route=Screen.Calendar.name) {
                NavigationScreen(viewModel, navController)
            }
            composable(route=Screen.Settings.name) {
                SettingsScreen(viewModel, navController)
            }
            composable(route=Screen.ChangePassword.name) {
                ChangePassword(viewModel, navController)
            }
            composable(route=Screen.ChangeLogin.name) {
                ChangeLogin(viewModel, navController)
            }
            composable(route=Screen.Statistics.name) {
                Statistics(viewModel, navController)
            }
            composable(route=Screen.Rating.name) {
                Statistics(viewModel, navController)
            }
            composable(route=Screen.Achievements.name) {
                Statistics(viewModel, navController)
            }
            composable(route=Screen.MyCalendars.name) {
                Statistics(viewModel, navController)
            }
            composable(route=Screen.AIAssistant.name) {
                Statistics(viewModel, navController)
            }
        }
    }
}

@Composable
fun Greeting(name: String, navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )

        Spacer(modifier = Modifier.
        padding(8.dp))

        Button(
            onClick = {
                navController.navigate(Screen.Login.name)
            },
            modifier = Modifier.fillMaxWidth()) {
            Text("Authorization")
        }
        Button(
            onClick = {
                navController.navigate(Screen.Calendar.name)
            },
            modifier = Modifier.fillMaxWidth()) {
            Text("Calendar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmartCalendartestTheme {
        Greeting("User", navController = rememberNavController())
    }
}