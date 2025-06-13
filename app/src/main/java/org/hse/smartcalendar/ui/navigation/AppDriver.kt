package org.hse.smartcalendar.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import org.hse.smartcalendar.ui.elements.Calendar_add_on
import org.hse.smartcalendar.ui.elements.Calendar_today
import org.hse.smartcalendar.ui.elements.Event_upcoming
import org.hse.smartcalendar.ui.elements.Finance
import org.hse.smartcalendar.ui.elements.Follow_the_signs
import org.hse.smartcalendar.ui.elements.Medal
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.utility.Screens
import org.hse.smartcalendar.utility.rememberNavigation


@Composable
fun AppDrawer(
    currentRoute: String,
    navigation: Navigation,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(modifier) {
        AppNavigationDrawerItem(label = "Calendar", icon = Calendar_today, destination = Screens.CALENDAR,
            currentRoute = currentRoute, navigation = navigation, closeDrawer = closeDrawer)
        AppNavigationDrawerItem(label = "Settings", icon = Icons.Filled.Settings, destination = Screens.SETTINGS,
            currentRoute = currentRoute, navigation = navigation, closeDrawer = closeDrawer)
        AppNavigationDrawerItem(label = "My progress", icon = Follow_the_signs, destination = Screens.STATISTICS,
            currentRoute = currentRoute, navigation = navigation, closeDrawer = closeDrawer)
        AppNavigationDrawerItem(label = "Achievements", icon = Medal, destination = Screens.ACHIEVEMENTS,
            currentRoute = currentRoute, navigation = navigation, closeDrawer = closeDrawer)
        AppNavigationDrawerItem(label = "Rating", icon = Finance, destination = Screens.RATING,
            currentRoute = currentRoute, navigation = navigation, closeDrawer = closeDrawer)
        AppNavigationDrawerItem(label = "My Calendars", icon = Calendar_add_on, destination = Screens.MY_CALENDARS,
            currentRoute = currentRoute, navigation = navigation, closeDrawer = closeDrawer)
        AppNavigationDrawerItem(label = "Shared events", icon = Event_upcoming, destination = Screens.SHARED_EVENTS,
            currentRoute = currentRoute, navigation = navigation, closeDrawer = closeDrawer)
    }
}
@Composable
fun AppNavigationDrawerItem(currentRoute: String, label: String, icon: ImageVector, destination: Screens, navigation: Navigation, closeDrawer: () -> Unit){
    NavigationDrawerItem(
        label = {Text(label)},
        icon = { Icon(icon, null) },
        selected = currentRoute == destination.route,
        onClick = { navigation.navigateTo(destination.route); closeDrawer() },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}
@Preview("Drawer contents")
@Composable
fun PreviewAppDrawer() {
    SmartCalendarTheme {
        AppDrawer(
            currentRoute = Screens.CALENDAR.name,
            rememberNavigation(),
            closeDrawer = { }
        )
    }
}
