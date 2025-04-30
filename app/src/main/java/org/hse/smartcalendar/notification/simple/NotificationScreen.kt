import android.Manifest
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.hse.smartcalendar.notification.simple.NotificationHandler

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationScreen(context: Context) {
    val postNotificationPermission = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val notificationHandler = NotificationHandler(context)

    LaunchedEffect(key1 = true) {
        if (!postNotificationPermission.status.isGranted) {
            postNotificationPermission.launchPermissionRequest()
        }
    }

    Column{
        Button(onClick = {
            notificationHandler.showSimpleNotification()
        }) { Text(text = "Simple notification") }
        Button(onClick = {
            notificationHandler.showSimpleNotification()
        }) { Text(text = "Simple notification") }
        Button(onClick = {
            notificationHandler.showSimpleNotification()
        }) { Text(text = "Simple notification") }
    }
}