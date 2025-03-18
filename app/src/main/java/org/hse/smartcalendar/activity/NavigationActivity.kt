package org.hse.smartcalendar.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.hse.smartcalendar.AuthViewModel
import org.hse.smartcalendar.ui.elements.ChangeLogin
import org.hse.smartcalendar.ui.elements.ChangePassword
import org.hse.smartcalendar.ui.elements.DailyTasksList
import org.hse.smartcalendar.ui.elements.SettingsScreen
import org.hse.smartcalendar.ui.elements.Statistics
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.AppDrawer
import org.hse.smartcalendar.utility.Screens
import org.hse.smartcalendar.utility.NavigationActions
import org.hse.smartcalendar.view.model.ListViewModel

class NavigationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val listModel = ListViewModel(intent.getLongExtra("id", -1))
        setContent {
            SmartCalendarTheme {
                App(AuthViewModel(), listModel)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    authModel: AuthViewModel,
    listModel: ListViewModel,
    startDestination: String = Screens.CALENDAR.route
) {
    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        NavigationActions(navController)
    }

    val coroutineScope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        navBackStackEntry?.destination?.route ?: Screens.CALENDAR.route
    val isExpandedScreen =false
    val DrawerState = rememberDrawerState(isExpandedScreen)
    //Main Navigation element  - Drawer open from left side
    //Instead of give NavController to navigate give functions to Screens/Drawer
    //If need more Screen/add to Screen, append to AppDrawer Button with Icons
    //Icons import from https://composeicons.com/
    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                navigateToCalendar = navigationActions.navigateToCalendar,
                navigateToSettings = navigationActions.navigateToSettings,
                navigateToStatistics = navigationActions.navigateToStatistics,
                closeDrawer = { coroutineScope.launch { DrawerState.close() } }
            )
        },
        drawerState = DrawerState,
        gesturesEnabled = !isExpandedScreen
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
        ) {
            composable(route = Screens.SETTINGS.route) {
                SettingsScreen(viewModel = authModel,
                    navigationActions.navigateToChangeLogin, navigationActions.navigateToChangePassword
                )
            }
            composable(Screens.CALENDAR.route) {
                DailyTasksList(listModel, openDrawer = { coroutineScope.launch { DrawerState.open() }}, navController)
            }
            composable(route = Screens.CHANGELOGIN.route) {
                ChangeLogin(authModel)
            }
            composable(route = Screens.CHANGEPASSWORD.route) {
                ChangePassword(authModel)
            }
            composable(route = Screens.STATISTICS.route) {
                Statistics()
            }
        }
    }

}


/**
 * Determine the drawer state to pass to the modal drawer.
 */
@Composable
private fun rememberDrawerState(isExpandedScreen: Boolean): DrawerState {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    return if (!isExpandedScreen) {
        // If we want to allow showing the drawer, we use a real, remembered drawer
        // state defined above
        drawerState
    } else {
        // If we don't want to allow the drawer to be shown, we provide a drawer state
        // that is locked closed. This is intentionally not remembered, because we
        // don't want to keep track of any changes and always keep it closed
        DrawerState(DrawerValue.Closed)
    }
}
