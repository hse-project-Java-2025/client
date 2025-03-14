package org.hse.smartcalendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Switch
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.hse.smartcalendar.ui.theme.SmartCalendartestTheme

@Preview
@Composable
fun SettingsScreen() {
    SmartCalendartestTheme { SettingsScreen(viewModel = AuthViewModel(), navController = rememberNavController()) }
}

private fun changeReminders(state: Boolean){
    if (state){
        // set Reminders
    } else {
        // cancel Reminders
    }
}

@Composable
fun SettingsScreen(viewModel: AuthViewModel, navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        //verticalArrangement = Arrangement.Center,
        //horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Login", fontSize = 22.sp)
        Text("Password", fontSize = 22.sp)
        Button(
            onClick = { navController.navigate(Screen.ChangePassword.name) },
            //modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change")
        }
        var isSwitched by remember { mutableStateOf(true) }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            //Text("Reminders", modifier = Modifier.fillMaxHeight(), fontSize = 22.sp)
            SwitchWithText("Reminders", state = isSwitched, onStateChange = {x->changeReminders(x)})
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
        Text(text = label)
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Checkbox(
            checked = state,
            onCheckedChange = null
        )
    }
}