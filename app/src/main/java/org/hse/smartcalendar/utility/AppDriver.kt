package org.hse.smartcalendar.utility

import androidx.compose.material.icons.filled.Settings
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.hse.smartcalendar.ui.elements.Calendar_today
import org.hse.smartcalendar.ui.elements.Finance
import org.hse.smartcalendar.ui.elements.Screen


@Composable
fun AppDrawer(
    currentRoute: String,
    navigateToCalendar: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToStatistics: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(modifier) {
        NavigationDrawerItem(
            label = { Text("Calendar") },
            icon = { Icon(Calendar_today, null) },
            selected = currentRoute == Screens.CALENDAR.route,
            onClick = { navigateToCalendar(); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("Settings") },
            icon = { Icon(Icons.Filled.Settings, null) },
            selected = currentRoute == Screens.SETTINGS.route,
            onClick = { navigateToSettings(); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("Statistics") },
            icon = { Icon(Finance, null) },
            selected = currentRoute == Screens.SETTINGS.route,
            onClick = { navigateToStatistics(); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Preview("Drawer contents")
@Composable
fun PreviewAppDrawer() {
    SmartCalendarTheme {
        AppDrawer(
            currentRoute = Screens.CALENDAR.name,
            navigateToCalendar = {},
            navigateToSettings = {},
            {},
            closeDrawer = { }
        )
    }
}
