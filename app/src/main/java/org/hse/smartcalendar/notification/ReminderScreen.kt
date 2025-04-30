package org.hse.smartcalendar.notification

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.hse.smartcalendar.activity.NavigationActivity
import org.hse.smartcalendar.notification.simple.NotificationHandler
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ReminderScreen() {
    val viewModel: ReminderViewModel = viewModel(
        factory = ReminderViewModelFactory(
            LocalContext.current.applicationContext as Application
        )
    )
    val postNotificationPermission = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val hasAccessNotificationPolicyPermission =rememberPermissionState(permission = Manifest.permission.ACCESS_NOTIFICATION_POLICY)
    LaunchedEffect(key1 = true) {
        if (!postNotificationPermission.status.isGranted) {
            postNotificationPermission.launchPermissionRequest()
        }
        if (!hasAccessNotificationPolicyPermission.status.isGranted){
            hasAccessNotificationPolicyPermission.launchPermissionRequest()
        }
    }
    Button(
        onClick = {viewModel.scheduleReminder(10000, TimeUnit.MILLISECONDS, DataSource.plants.get(0).name) },
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Notify")
    }
}