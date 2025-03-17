package org.hse.smartcalendar.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.hse.smartcalendar.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionTopAppbar(openMenu: (()-> Unit)? = null, elevation: Dp, navController: NavController) {
    TopAppBar(
        title = {
            Text(text = "Smth more")
        },
        navigationIcon = {
            IconButton(onClick = { openMenu?.invoke() }) {
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
                onClick = {}) {
                Icon(imageVector = Icons.Filled.MoreVert,
                    contentDescription = null)
            }
        }
    )
}