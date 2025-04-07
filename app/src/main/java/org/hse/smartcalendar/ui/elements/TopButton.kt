package org.hse.smartcalendar.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.utility.rememberNavigation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopButton(openMenu: (()-> Unit)? = null, navigation: Navigation, text: String) {
    TopAppBar(
        title = {
            Text(text = text)
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

            IconButton(onClick = { navigation.navigateTo("Settings")}) {
                Icon(Icons.Filled.Settings, contentDescription = null)
            }

            IconButton(
                onClick = {}) {
                Icon(imageVector = Icons.Filled.MoreVert,
                    contentDescription = null)
            }
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            scrolledContainerColor = MaterialTheme.colorScheme.error,
            navigationIconContentColor = MaterialTheme.colorScheme.error,
            titleContentColor = MaterialTheme.colorScheme.outline,
            actionIconContentColor = MaterialTheme.colorScheme.error
        )
    )
}

@Preview
@Composable
fun TopButtonPreview(){
    TopButton({}, rememberNavigation(), "TopButton")
}