package org.hse.smartcalendar.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.hse.smartcalendar.AuthViewModel
import org.hse.smartcalendar.Screen
import org.hse.smartcalendar.ui.theme.SmartCalendartestTheme



@Preview
@Composable
fun CalendarScreen() {
    SmartCalendartestTheme { CalendarScreen(viewModel = AuthViewModel(), navController = rememberNavController()) }
}

@Composable
fun CalendarScreen(viewModel: AuthViewModel, navController: NavController){
    ActionTopAppbar(elevation = 8.dp, navController = navController)
}
