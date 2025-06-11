package org.hse.smartcalendar.ui.screens

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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.hse.smartcalendar.AuthViewModel
import org.hse.smartcalendar.view.model.ReminderViewModel
import org.hse.smartcalendar.ui.elements.Password
import org.hse.smartcalendar.ui.elements.Person
import org.hse.smartcalendar.ui.elements.Reminder
import org.hse.smartcalendar.ui.navigation.App
import org.hse.smartcalendar.ui.navigation.TopButton
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.utility.Screens
import org.hse.smartcalendar.view.model.ListViewModel
import org.hse.smartcalendar.view.model.StatisticsManager
import org.hse.smartcalendar.view.model.StatisticsViewModel
import org.hse.smartcalendar.view.model.TaskEditViewModel

//здесь работает навигация
@Preview
@Composable
fun SettingsScreenPreview() {
    SmartCalendarTheme {
        val statisticsViewModel = viewModel<StatisticsViewModel> ()
        val statisticsManager = StatisticsManager(statisticsViewModel)
        val listModel = ListViewModel(statisticsManager = statisticsManager)
        val taskEditViewModel = TaskEditViewModel(listModel)
        App(
            startDestination = Screens.SETTINGS.route,
            listVM = listModel,
            statisticsVM = statisticsViewModel,
            taskEditVM = taskEditViewModel
        )
    }
}


@Composable
fun SettingsScreen(viewModel: AuthViewModel,
                   navigation: Navigation,
                   openDrawer: (()->Unit)?=null,
                   reminderModel: ReminderViewModel){
    val fontSize = 22.sp
    var timeDelay = remember {  mutableStateOf(TextFieldValue("")) }
    val minutesDelay =reminderModel.minutesDelay.collectAsState()
    Scaffold(
        topBar = { TopButton(openDrawer, navigation, "Settings") }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SettingsClickableComp(Person, "Login", fontSize, null){
                navigation.navigateTo(Screens.CHANGE_LOGIN.route)
            }
            SettingsClickableComp(Password, "Password", fontSize, null){
                navigation.navigateTo(Screens.CHANGE_PASSWORD.route)
            }
            SettingsClickableComp(Reminder, "Reminder", state = reminderModel.isReminders.collectAsState(), fontSize = fontSize){
                reminderModel.switchReminders()
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = timeDelay.value,
                    label = { Text("Reminders minutes delay") },
                    placeholder = { Text(minutesDelay.value.toString()) },
                    onValueChange = { newValue ->
                        timeDelay.value = newValue
                    }
                    //error  = timeDelay.value.toString()==""
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        if (timeDelay.value.text != "") {
                            reminderModel.setDelay(timeDelay.value.text.toInt())
                        }
                    }
                ) {
                    Text("Change")
                }
            }
        }
    }
}


@Composable
fun SettingsClickableComp(
    imageVector: ImageVector,
    title: String,
    fontSize: TextUnit,
    state: State<Boolean>?,
    onClick: () -> Unit,
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
                        fontSize = fontSize
                    )
                }
                Spacer(modifier = Modifier.weight(1.0f))
                if (state!=null) {
                    Switch(
                        checked = state.value,
                        onCheckedChange = { onClick() }
                    )
                } else {
                    Button(
                        onClick = onClick
                    ) {
                        Text("Change")
                    }
                }
            }
            HorizontalDivider()
        }

    }
}