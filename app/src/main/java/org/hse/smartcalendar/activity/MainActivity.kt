package org.hse.smartcalendar.activity

import NotificationScreen
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.hse.smartcalendar.LoginActivity
import org.hse.smartcalendar.RegisterActivity
import org.hse.smartcalendar.notification.ReminderScreen
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme

class MainActivity : ComponentActivity() {
    //@RequiresApi(Build.VERSION_CODES.O, Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val notificationChannel = NotificationChannel(
            "notification_channel_id",
            "Notification name",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        // Setting up the channel
        notificationManager.createNotificationChannel(notificationChannel)
        enableEdgeToEdge()
        setContent {
            SmartCalendarTheme {
                ReminderScreen()
                //NotificationScreen(this)
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "User",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
            }
        }
    }
    companion object {
        const val CHANNEL_ID = "reminder_id"
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )

        Spacer(modifier = Modifier.
            padding(8.dp))

        Button(
            onClick = {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("authorizationButtonTest")) {
            Text("Authorization")
        }
        Button(
            onClick = {
                val intent = Intent(context, RegisterActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("registerButtonTest")) {
            Text("Signup")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmartCalendarTheme {
        Greeting("User")
    }
}