package org.hse.smartcalendar.ui.elements

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hse.smartcalendar.activity.App
import org.hse.smartcalendar.AuthViewModel
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.utility.Screens
import org.hse.smartcalendar.view.model.ListViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import org.hse.smartcalendar.notification.ReminderViewModel
import org.hse.smartcalendar.notification.ReminderViewModelFactory
import org.hse.smartcalendar.view.model.SettingsViewModel

//здесь работает навигация
@Preview
@Composable
fun SettingsScreen() {
    SmartCalendarTheme {
        App(listModel = ListViewModel(-1), authModel = AuthViewModel(), startDestination = Screens.SETTINGS.route)
    }
}


@Composable
fun SettingsScreen(viewModel: AuthViewModel,
                   navigation: Navigation,
                   openDrawer: (()->Unit)?=null){
    val reminderModel: ReminderViewModel = viewModel(
        factory = ReminderViewModelFactory(
            LocalContext.current.applicationContext as Application
        )
    )
    Scaffold(
        topBar = { TopButton(openDrawer, navigation, "Settings") }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text("Login", fontSize = 22.sp)
            Button(
                onClick = { navigation.navigateTo(Screens.CHANGE_LOGIN.route)
                          },
            ) {
                Text("Change")
            }
            Text("Password", fontSize = 22.sp)
            Button(
                onClick = { navigation.navigateTo(Screens.CHANGE_PASSWORD.route)
                          },
            ) {
                Text("Change")
            }
            SettingsClickableComp(Reminder, "Reminder", reminderModel.isReminders.collectAsState()){
                reminderModel.switchReminders()
            }
        }
    }
}


@Composable
fun SettingsClickableComp(
    imageVector: ImageVector,
    title: String,
    state: State<Boolean>,
    onClick: () -> Unit
) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = onClick,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.surfaceTint
                        ),
                        modifier = Modifier
                            .padding(16.dp),
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(modifier = Modifier.weight(1.0f))
                Switch(
                    checked = state.value,
                    onCheckedChange = { onClick() }
                )
            }
            HorizontalDivider()
        }

    }
}