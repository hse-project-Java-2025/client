package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hse.smartcalendar.activity.App
import org.hse.smartcalendar.AuthViewModel
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.utility.Screens
import org.hse.smartcalendar.view.model.ListViewModel

//здесь работает навигация
@Preview
@Composable
fun SettingsScreen() {
    SmartCalendarTheme {
        App(listModel = ListViewModel(-1), authModel = AuthViewModel(), startDestination = Screens.SETTINGS.route)
    }
}


private fun changeReminders(state: Boolean){
    if (state){
        // set Reminders
    } else {
        // cancel Reminders
    }
}

@Composable
fun SettingsScreen(viewModel: AuthViewModel, navigation: Navigation, openDrawer: (()->Unit)?=null){
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
            var isSwitched by remember { mutableStateOf(true) }

            SwitchWithText("Reminders", isSwitched) {
                isSwitched = it
                changeReminders(isSwitched)
            }
        }
    }
}


@Composable
private fun SwitchWithText(label: String, state: Boolean, onStateChange: (Boolean) -> Unit) {

    // Checkbox with text on right side
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clickable(
                interactionSource = interactionSource,
                // This is for removing ripple when Row is clicked
                indication = null,
                role = Role.Switch,
                onClick = {
                    onStateChange(!state)
                }
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(text = label, fontSize = 22.sp)
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Checkbox(
            checked = state,
            onCheckedChange = null
        )
    }
}