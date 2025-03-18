package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.hse.smartcalendar.AuthViewModel
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme



//@Preview
//@Composable
//fun CalendarScreen() {
//    SmartCalendarTheme { CalendarScreen(viewModel = AuthViewModel(), navController = rememberNavController()) }
//}

@Composable
fun CalendarScreen( openDrawer: ()->Unit, navController: NavController) {
    Scaffold(topBar =
    {TopButton(navController = navController, text = "", openMenu = openDrawer)},
        content = {paddingValues ->  Text("kek", modifier = Modifier.padding(paddingValues))})
}
