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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import kotlinx.coroutines.launch
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.ui.elements.ActionTopAppbar
import org.hse.smartcalendar.ui.elements.BottomSheet
import org.hse.smartcalendar.ui.elements.ChangeLogin
import org.hse.smartcalendar.ui.elements.ChangePassword
import org.hse.smartcalendar.ui.elements.DailyTasksList
import org.hse.smartcalendar.ui.elements.NavigationSheet
import org.hse.smartcalendar.ui.elements.SettingsScreen
import org.hse.smartcalendar.ui.elements.Statistics
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.view.model.ListViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authModel = ListViewModel(intent.getLongExtra("id", -1))
        setContent {
            SmartCalendarTheme {
                App(AuthViewModel(), authModel)
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
    MyCalendars,
    AIAssistant
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    authModel: AuthViewModel,
    listModel: ListViewModel,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Calendar.name
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.Greeting.name
    )
    val isNavSheetVisible = rememberSaveable { mutableStateOf(false) }

    val taskTitle = rememberSaveable { mutableStateOf("") }
    val taskType = rememberSaveable { mutableStateOf(DailyTaskType.COMMON) }
    val taskDescription = rememberSaveable { mutableStateOf("") }
    val startTime = rememberSaveable { mutableIntStateOf( 0) }
    val endTime = rememberSaveable { mutableIntStateOf( 0) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    val isBottomSheetVisible = rememberSaveable { mutableStateOf(false) }

    //верхняя кнопка и лист навигации во всём приложении
    Scaffold(
        topBar = { ActionTopAppbar(openMenu = {isNavSheetVisible.value = true}, elevation = 8.dp, navController = navController)
            NavigationSheet(navController, isNavSheetVisible)
        },
    )
    { innerPadding->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ){
            composable(route=Screen.Calendar.name) {
                DailyTasksList(listModel, navController)
            }
            composable(route=Screen.Settings.name) {
                SettingsScreen(authModel, navController)
            }
            composable(route=Screen.ChangePassword.name) {
                ChangePassword(authModel, navController)
            }
            composable(route=Screen.ChangeLogin.name) {
                ChangeLogin(authModel, navController)
            }
            composable(route=Screen.Statistics.name) {
                Statistics(authModel, navController)
            }
            composable(route=Screen.Rating.name) {
                Statistics(authModel, navController)
            }
            composable(route=Screen.Achievements.name) {
                Statistics(authModel, navController)
            }
            composable(route=Screen.MyCalendars.name) {
                Statistics(authModel, navController)
            }
            composable(route=Screen.AIAssistant.name) {
                Statistics(authModel, navController)
            }
        }
    }
}
