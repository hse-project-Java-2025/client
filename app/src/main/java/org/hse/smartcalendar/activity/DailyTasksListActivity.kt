package org.hse.smartcalendar.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.hse.smartcalendar.ui.elements.DailyTasksList
import org.hse.smartcalendar.ui.theme.SmartCalendartestTheme
import org.hse.smartcalendar.view.model.ListViewModel

class DailyTasksListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val model = ListViewModel()
        setContent {
            SmartCalendartestTheme {
                Row (Modifier.padding(2.dp)) {
                    DailyTasksList(model)
                }
            }
        }
    }
}