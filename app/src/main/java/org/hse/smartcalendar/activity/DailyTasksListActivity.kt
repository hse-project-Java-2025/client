package org.hse.smartcalendar.activity

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.hse.smartcalendar.data.WorkManagerHolder
import org.hse.smartcalendar.view.model.ReminderViewModel
import org.hse.smartcalendar.view.model.ReminderViewModelFactory
import org.hse.smartcalendar.ui.screens.StatisticsScreen
import org.hse.smartcalendar.ui.task.DailyTasksList
import org.hse.smartcalendar.ui.task.TaskEditWindow
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.Screens
import org.hse.smartcalendar.utility.rememberNavigation
import org.hse.smartcalendar.view.model.ListViewModel
import org.hse.smartcalendar.view.model.StatisticsManager
import org.hse.smartcalendar.view.model.StatisticsViewModel
import org.hse.smartcalendar.view.model.TaskEditViewModel

/**
 * Обеспечивает быстрый доступ к основной части приложения, не является его частью
 * (можно менять)
 */
class DailyTasksListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WorkManagerHolder.init(this)
        enableEdgeToEdge()
        val statisticsModel = StatisticsViewModel()
        val statisticsManager = StatisticsManager(statisticsModel)
        val listViewModel = ListViewModel(statisticsManager)
        val taskEditViewModel = TaskEditViewModel(listViewModel = listViewModel)
        setContent {
            SmartCalendarTheme {
                ListNavigation(listViewModel, taskEditViewModel, statisticsModel)
            }
        }
    }
}

@Composable
fun ListNavigation(
    listViewModel: ListViewModel,
    taskEditViewModel: TaskEditViewModel,
    statisticsViewModel: StatisticsViewModel
) {
    val reminderModel: ReminderViewModel = viewModel(factory = ReminderViewModelFactory(
        LocalContext.current.applicationContext as Application
    ))
    val navigation = rememberNavigation()
    val navController = navigation.navController
    NavHost(navController = navController, startDestination = Screens.CALENDAR.route) {
        composable(Screens.CALENDAR.route) {
            DailyTasksList(
                listViewModel,
                taskEditViewModel,
                reminderModel = reminderModel,
                { },
                navigation,
                navController
            )
        }
        composable(Screens.EDIT_TASK.route) {
            TaskEditWindow(
                onSave = {},
                onDelete = { task ->
                    listViewModel.removeDailyTask(task)

                }, onCancel = {},
                taskEditViewModel = taskEditViewModel, navController = navController
            )
        }
        composable(route = Screens.SETTINGS.route) {
            StatisticsScreen(navigation, {}, statisticsViewModel)
        }
    }
}
