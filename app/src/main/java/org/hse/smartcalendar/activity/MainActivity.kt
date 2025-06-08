package org.hse.smartcalendar.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.hse.smartcalendar.data.WorkManagerHolder
import org.hse.smartcalendar.ui.navigation.App
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.view.model.ListViewModel
import org.hse.smartcalendar.view.model.StatisticsManager
import org.hse.smartcalendar.view.model.StatisticsViewModel
import org.hse.smartcalendar.view.model.TaskEditViewModel


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getNotificationsPermissions(){
        val permissionPostState =
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
        val permissionNotificationState =
            checkSelfPermission(Manifest.permission.ACCESS_NOTIFICATION_POLICY)
        if (permissionPostState == PackageManager.PERMISSION_DENIED
            || permissionNotificationState == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                    Manifest.permission.POST_NOTIFICATIONS
                ),
                11
            )
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WorkManagerHolder.init(this)
        val statisticsModel = StatisticsViewModel()
        val listModel =  ListViewModel(StatisticsManager(statisticsModel))
        val editModel =  TaskEditViewModel(listModel)
        enableEdgeToEdge()
        setContent {
            SmartCalendarTheme {
                if (SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    getNotificationsPermissions()
                }
                App(statisticsVM = statisticsModel, listVM = listModel, taskEditVM = editModel)
            }
        }
    }
}