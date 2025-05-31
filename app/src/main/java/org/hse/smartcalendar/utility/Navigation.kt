package org.hse.smartcalendar.utility

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
enum class Screens(val route: String) {
    LOGIN("login"),
    GREETING("greeting"),
    REGISTER("register"),
    LOADING("loading"),
    CHANGE_PASSWORD("changePassword"),
    CHANGE_LOGIN("changeLogin"),
    CALENDAR("calendar"),
    SETTINGS( "settings"),
    STATISTICS( "statistics"),
    ACHIEVEMENTS("achievements"),
    EDIT_TASK("editTask"),
    MY_CALENDARS("myCalendars"),
    RATING("rating"),
    AI_ASSISTANT("aiAssistant");
    enum class Subgraph(val route: String) {
        AUTH("auth"),
        MAIN("main")
    }
}

@Composable
fun rememberNavigation(
    navController: NavHostController = rememberNavController()
): Navigation = remember(navController) {
    Navigation(navController)
}

@Stable
class Navigation(val navController: NavHostController) {
    fun upPress() {
        navController.navigateUp()
    }
    fun navigateTo (route: String) {
        if (route != navController.currentDestination?.route) {
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
    fun navigateToMainApp (route: String,
                           oldRouteToPopUp: String = Screens.Subgraph.AUTH.route
    ) {
        if (route != navController.currentDestination?.route) {
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                popUpTo(oldRouteToPopUp) { this.inclusive = inclusive }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
}
