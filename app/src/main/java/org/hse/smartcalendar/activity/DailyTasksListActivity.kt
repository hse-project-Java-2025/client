package org.hse.smartcalendar.activity

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.hse.smartcalendar.notification.ReminderViewModel
import org.hse.smartcalendar.notification.ReminderViewModelFactory
import org.hse.smartcalendar.ui.elements.DailyTasksList
import org.hse.smartcalendar.ui.elements.TaskEditWindow
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.Screens
import org.hse.smartcalendar.utility.rememberNavigation
import org.hse.smartcalendar.view.model.ListViewModel
import org.hse.smartcalendar.view.model.TaskEditViewModel

class DailyTasksListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val listViewModel = ListViewModel(intent.getLongExtra("id", -1))
        val taskEditViewModel = TaskEditViewModel(listViewModel = listViewModel)
        setContent {
            val navController = rememberNavController()
            SmartCalendarTheme {
                ListNavigation(listViewModel, taskEditViewModel, navController)
            }
        }
    }
}

@Composable
fun ListNavigation(
    listViewModel: ListViewModel,
    taskEditViewModel: TaskEditViewModel,
    navController: NavHostController
) {
    val reminderModel: ReminderViewModel = viewModel(factory = ReminderViewModelFactory(
        LocalContext.current.applicationContext as Application
    ))
    NavHost(navController = navController, startDestination = Screens.CALENDAR.route) {
        composable(Screens.CALENDAR.route) {
            DailyTasksList(
                listViewModel,
                taskEditViewModel,
                reminderModel = reminderModel,
                { },
                rememberNavigation(),
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
    }
}
