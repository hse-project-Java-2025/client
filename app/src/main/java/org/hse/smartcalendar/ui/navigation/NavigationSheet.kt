package org.hse.smartcalendar.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import org.hse.smartcalendar.data.SettingsButton
import org.hse.smartcalendar.ui.elements.BrainCircuit
import org.hse.smartcalendar.ui.elements.Calendar_add_on
import org.hse.smartcalendar.ui.elements.Finance
import org.hse.smartcalendar.ui.elements.Follow_the_signs
import org.hse.smartcalendar.ui.elements.Medal

enum class Screen {
    Login,
    Calendar,
    Greeting,
    Settings,
    ChangePassword,
    ChangeLogin,
    Statistics,
    Rating,
    Achievements,
    Navigation,
    MyCalendars,
    AIAssistant
}
@Composable
fun NavigationSheet(navController: NavController, isVisible: MutableState<Boolean>){
    AnimatedVisibility(enter = expandHorizontally(), exit = shrinkHorizontally(),
    visible = isVisible.value) {
        val items = NavigationSheetList(navController)
        val selectedItem = remember { mutableStateOf(items[0]) }
        ModalDrawerSheet(

        ) {
            items.forEach { item ->
                NavigationDrawerItem(
                    label = { item.DrawButton() },
                    selected = selectedItem.value == item,
                    onClick = {
                        isVisible.value = false
                        item.action()
                    }
                )
            }
        }
    }
}


fun NavigationSheetList(navController: NavController): List<SettingsButton>{
    return listOf(SettingsButton( "Rating", Finance,
        {navController.navigate(Screen.Statistics.name)} ),
        SettingsButton( "Achievements", Medal,
            {navController.navigate(Screen.Achievements.name)} ),
        SettingsButton( "My progress", Follow_the_signs,
            {navController.navigate(Screen.Statistics.name)} ),
        SettingsButton( "My Calendars", Calendar_add_on,
            {navController.navigate(Screen.MyCalendars.name)} ),
        SettingsButton( "AI Assistant", BrainCircuit,
        {navController.navigate(Screen.MyCalendars.name)} ),
        //добавить чужой календарь(smartcalendarus/setting)
    )
}
