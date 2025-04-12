package org.hse.smartcalendar.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    NavHost(navController = navController, startDestination = Screens.CALENDAR.route) {
        composable(Screens.CALENDAR.route) {
            DailyTasksList(
                listViewModel,
                taskEditViewModel,
                { },
                rememberNavigation(),
                navController
            )
        }
        composable(Screens.EDIT_TASK.route) {
            TaskEditWindow(onSave = {
                taskEditViewModel.updateTask()
            },
                onDelete = { task ->
                    listViewModel.removeDailyTask(task)

                }, onCancel = {},
                taskEditViewModel = taskEditViewModel, navController = navController
            )
        }
    }
}
