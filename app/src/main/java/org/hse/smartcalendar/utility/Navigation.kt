package org.hse.smartcalendar.utility

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import org.hse.smartcalendar.Screen

object Destinations {
    const val CALENDAR_ROUTE = "Calendar"
    const val SETTINGS_ROUTE = "Settings"
}


class NavigationActions(navController: NavHostController) {
    val navigateToCalendar: () -> Unit = {
        navController.navigate(Screen.Calendar) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToSettings: () -> Unit = {
        navController.navigate(Screen.Settings) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
