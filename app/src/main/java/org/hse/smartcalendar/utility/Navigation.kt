package org.hse.smartcalendar.utility

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

enum class Screens(val route: String) {
    CHANGEPASSWORD("changePassword"),
    CHANGELOGIN("changeLogin"),
    CALENDAR("calendar"),
    SETTINGS( "settings"),
    STATISTICS( "statistics"),
}


class NavigationActions(navController: NavHostController) {
    val navigateToCalendar: () -> Unit = {
        navController.navigate(Screens.CALENDAR.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToSettings: () -> Unit = {
        navController.navigate(Screens.SETTINGS.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToChangeLogin: () -> Unit = {
        navController.navigate(Screens.CHANGELOGIN.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToChangePassword: () -> Unit = {
        navController.navigate(Screens.CHANGEPASSWORD.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToStatistics: () -> Unit = {
        navController.navigate(Screens.STATISTICS.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
