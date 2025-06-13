package org.hse.smartcalendar.ui.navigation

import android.app.Application
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import kotlinx.coroutines.launch
import org.hse.smartcalendar.AuthScreen
import org.hse.smartcalendar.AuthType
import org.hse.smartcalendar.AuthViewModel
import org.hse.smartcalendar.view.model.ReminderViewModel
import org.hse.smartcalendar.view.model.ReminderViewModelFactory
import org.hse.smartcalendar.ui.screens.AchievementsScreen
import org.hse.smartcalendar.ui.screens.ChangeLogin
import org.hse.smartcalendar.ui.screens.ChangePassword
import org.hse.smartcalendar.ui.screens.GreetingScreen
import org.hse.smartcalendar.ui.screens.InvitesScreen
import org.hse.smartcalendar.ui.screens.LoadingScreen
import org.hse.smartcalendar.ui.screens.SettingsScreen
import org.hse.smartcalendar.ui.screens.StatisticsScreen
import org.hse.smartcalendar.ui.task.DailyTasksList
import org.hse.smartcalendar.ui.task.TaskEditWindow
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.utility.Screens
import org.hse.smartcalendar.utility.rememberNavigation
import org.hse.smartcalendar.view.model.InvitesViewModel
import org.hse.smartcalendar.view.model.ListViewModel
import org.hse.smartcalendar.view.model.StatisticsViewModel
import org.hse.smartcalendar.view.model.TaskEditViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    startDestination: String = Screens.GREETING.route,
    statisticsVM: StatisticsViewModel,
    listVM: ListViewModel,
    taskEditVM: TaskEditViewModel
) {
    val invitesModel: InvitesViewModel = viewModel()
    val authModel: AuthViewModel = viewModel()
    val navigation = rememberNavigation()
    val coroutineScope = rememberCoroutineScope()
    val currentRoute = navigation.navController.currentDestination?.route ?: startDestination
    //Всё что касается drawer
    val navBackStackEntry by navigation.navController.currentBackStackEntryAsState()
    val initialRoute =
        navBackStackEntry?.destination?.route ?: Screens.CALENDAR.route
    val drawerEnabled = currentRoute !in listOf(Screens.LOGIN.route, Screens.GREETING.route, Screens.REGISTER.route)
    val isExpandedScreen =false
    val navigationDrawerState = rememberDrawerState(isExpandedScreen)
    val notificationDrawerState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            true
        }
    )
    val openDrawer: ()-> Unit = {
        coroutineScope.launch { navigationDrawerState.open() }
    }
    if (drawerEnabled) {
        ModalNavigationDrawer(
            drawerContent = {
                AppDrawer(
                    currentRoute = initialRoute,
                    navigation,
                    closeDrawer = { coroutineScope.launch { navigationDrawerState.close() } }
                )
            },
            drawerState = navigationDrawerState,
            gesturesEnabled = !isExpandedScreen
        ){NestedNavigator(navigation, authModel,openDrawer,statisticsVM, listVM, taskEditVM, invitesModel )
        }
    } else {
        NestedNavigator(navigation, authModel,openDrawer, statisticsVM, listVM, taskEditVM, invitesModel )
    }
}
@Composable
fun NestedNavigator(navigation: Navigation, authModel: AuthViewModel,openDrawer: ()-> Unit,
                    statisticsModel: StatisticsViewModel, listModel: ListViewModel, editModel: TaskEditViewModel,
                    invitesModel: InvitesViewModel){
    val reminderModel: ReminderViewModel = viewModel(factory = ReminderViewModelFactory(
        LocalContext.current.applicationContext as Application
    ))
    NavHost(
        navController = navigation.navController,
        startDestination = Screens.Subgraph.AUTH.route
    ) {
        // Auth Graph
        navigation(
            startDestination = Screens.GREETING.route,
            route = Screens.Subgraph.AUTH.route
        ) {
            composable(Screens.GREETING.route) {
                GreetingScreen(navigation)
            }
            composable(Screens.REGISTER.route) {
                AuthScreen(navigation, authModel, AuthType.Register)
            }
            composable(Screens.LOGIN.route) {
                AuthScreen(navigation, authModel, AuthType.Login)
            }
            composable(Screens.LOADING.route) {
                LoadingScreen(navigation, statisticsModel, listModel)
            }
        }

        // Main Graph with Drawer
        navigation(
            startDestination = Screens.CALENDAR.route,
            route = Screens.Subgraph.MAIN.route
        ) {
            composable(route = Screens.SETTINGS.route) {
                SettingsScreen(
                    viewModel = authModel,
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
                ChangeLogin(authModel, navigation)
            }
            composable(route = Screens.CHANGE_PASSWORD.route) {
                ChangePassword(authModel, navigation)
            }
            composable(route = Screens.STATISTICS.route) {
                StatisticsScreen(navigation, openDrawer, statisticsModel)
            }
            composable(route = Screens.ACHIEVEMENTS.route) {
                AchievementsScreen(navigation, openDrawer, statisticsModel)
            }
            composable(route = Screens.SHARED_EVENTS.route) {
                InvitesScreen(navigation, openDrawer, invitesModel)
            }
            composable(Screens.EDIT_TASK.route) {
                TaskEditWindow(
                    onSave = {task->reminderModel.scheduleReminder(task)},
                    onDelete = { task ->
                        listModel.removeDailyTask(task)
                        reminderModel.cancelReminder(task)
                    }, onCancel = {},
                    taskEditViewModel = editModel,
                    reminderModel = reminderModel,
                    navController = navigation.navController
                )
            }
        }
    }
}

@Composable
private fun rememberDrawerState(isExpandedScreen: Boolean): DrawerState {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    return if (!isExpandedScreen) {
        drawerState
    } else {
        DrawerState(DrawerValue.Closed)
    }
}
