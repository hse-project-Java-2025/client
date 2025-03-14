package org.hse.smartcalendar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionTopAppbar(onBack: (() -> Unit)? = null, elevation: Dp, navController: NavController) {
    TopAppBar(
        title = {
            Text(text = "TopAppBar")
        },
        navigationIcon = {
            IconButton(onClick = { onBack?.invoke() }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null
                )
            }
        },
        actions = {

            IconButton(onClick = { navController.navigate(Screen.Settings.name);}) {
                Icon(Icons.Filled.Settings, contentDescription = null)
            }

            IconButton(
                onClick = { /* doSomething() */ }) {
                Icon(imageVector = Icons.Filled.MoreVert,
                    contentDescription = null)
            }
        }
    )
}
