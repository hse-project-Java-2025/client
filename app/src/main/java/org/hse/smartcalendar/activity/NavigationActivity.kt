package org.hse.smartcalendar.activity

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import org.hse.smartcalendar.AuthViewModel
import org.hse.smartcalendar.notification.ReminderViewModel
import org.hse.smartcalendar.notification.ReminderViewModelFactory
import org.hse.smartcalendar.ui.elements.AchievementsScreen
import org.hse.smartcalendar.ui.elements.ChangeLogin
import org.hse.smartcalendar.ui.elements.ChangePassword
import org.hse.smartcalendar.ui.elements.DailyTasksList
import org.hse.smartcalendar.ui.elements.SettingsScreen
import org.hse.smartcalendar.ui.elements.Statistics
import org.hse.smartcalendar.ui.elements.TaskEditWindow
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.AppDrawer
import org.hse.smartcalendar.utility.Screens
import org.hse.smartcalendar.utility.rememberNavigation
import org.hse.smartcalendar.view.model.ListViewModel
import org.hse.smartcalendar.view.model.SettingsViewModel
import org.hse.smartcalendar.view.model.StatisticsViewModel
import org.hse.smartcalendar.view.model.TaskEditViewModel

class NavigationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val listModel = ListViewModel(intent.getLongExtra("id", -1))
        val editModel = TaskEditViewModel(listModel)
        setContent {
            SmartCalendarTheme {
                App(AuthViewModel(), listModel, editModel)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    authModel: AuthViewModel,
    listModel: ListViewModel,
    editModel: TaskEditViewModel,
    startDestination: String = Screens.CALENDAR.route
) {
    val statisticsModel = StatisticsViewModel()
    val navigation = rememberNavigation()
    val coroutineScope = rememberCoroutineScope()
    val navBackStackEntry by navigation.navController.currentBackStackEntryAsState()
    val initialRoute =
        navBackStackEntry?.destination?.route ?: Screens.CALENDAR.route
    val isExpandedScreen =false
    val DrawerState = rememberDrawerState(isExpandedScreen)
    val openDrawer: ()-> Unit = { 
        val currentRoute = navigation.navController.currentDestination?.route
        coroutineScope.launch { DrawerState.open() }
    }
    var settingsViewModel=SettingsViewModel()
    val reminderModel: ReminderViewModel = viewModel(factory = ReminderViewModelFactory(
        LocalContext.current.applicationContext as Application
    ))
    //val reminderModel: ReminderViewModel = viewModel(factory =  ReminderViewModel.Factory)
    //Main Navigation element  - Drawer open from left side
    //to Navigate pass navigation to function and call navigation.navigateTo()
    //If need more Screen/add to Screen, append to AppDrawer Button with Icons
    //Icons import from https://composeicons.com/

    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                currentRoute = initialRoute,
                navigation,
                closeDrawer = { coroutineScope.launch { DrawerState.close() } }
            )
        },
        drawerState = DrawerState,
        gesturesEnabled = !isExpandedScreen
    ) {
        NavHost(
            navController = navigation.navController,
            startDestination = startDestination,
            modifier = Modifier
        ) {
            composable(route = Screens.SETTINGS.route) {
                SettingsScreen(viewModel = authModel,
                    navigation, openDrawer, reminderModel
                )
            }
            composable(Screens.CALENDAR.route) {
                DailyTasksList(
                    listModel, openDrawer = openDrawer,
                    taskEditViewModel = editModel,
                    navigation = navigation,
                    navController = navigation.navController,
                    reminderModel = reminderModel
                )
            }
            composable(route = Screens.CHANGE_LOGIN.route) {
                ChangeLogin(authModel)
            }
            composable(route = Screens.CHANGE_PASSWORD.route) {
                ChangePassword(authModel)
            }
            composable(route = Screens.STATISTICS.route) {
                Statistics(navigation, openDrawer, statisticsModel)
            }
            composable(route = Screens.ACHIEVEMENTS.route) {
                AchievementsScreen(navigation, openDrawer, statisticsModel)
            }
            composable(Screens.EDIT_TASK.route) {
                TaskEditWindow(
                    onSave = {},
                    onDelete = { task ->
                        listModel.removeDailyTask(task)

                    }, onCancel = {},
                    taskEditViewModel = editModel, navController = navigation.navController
                )
            }
        }
    }

}


/**
 * Determine the drawer state to pass to the modal drawer.
 */
@Composable
private fun rememberDrawerState(isExpandedScreen: Boolean): DrawerState {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    return if (!isExpandedScreen) {
        // If we want to allow showing the drawer, we use a real, remembered drawer
        // state defined above
        drawerState
    } else {
        // If we don't want to allow the drawer to be shown, we provide a drawer state
        // that is locked closed. This is intentionally not remembered, because we
        // don't want to keep track of any changes and always keep it closed
        DrawerState(DrawerValue.Closed)
    }
}
