package org.hse.smartcalendar.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.rememberDrawerState
import androidx.navigation.compose.rememberNavController
import org.hse.smartcalendar.ui.elements.DailyTasksList
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.rememberNavigation
import org.hse.smartcalendar.view.model.ListViewModel

class DailyTasksListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ListViewModel(intent.getLongExtra("id", -1))
        setContent {
            SmartCalendarTheme {
                DailyTasksList(viewModel, {}, rememberNavigation())
            }
        }
    }
}
