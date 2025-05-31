package org.hse.smartcalendar.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController


//@Preview
//@Composable
//fun CalendarScreen() {
//    SmartCalendarTheme { CalendarScreen(viewModel = AuthViewModel(), navController = rememberNavController()) }
//}

@Composable
fun CalendarScreen( openDrawer: ()->Unit, navController: NavController) {
    Scaffold(//topBar = {TopButton(navController = navController, text = "", openMenu = openDrawer)},
        content = {paddingValues ->  Text("kek", modifier = Modifier.padding(paddingValues))})
}
