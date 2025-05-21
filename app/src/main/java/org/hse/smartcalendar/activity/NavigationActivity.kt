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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
import org.hse.smartcalendar.utility.rememberNavigation
import org.hse.smartcalendar.view.model.ListViewModel
import org.hse.smartcalendar.view.model.StatisticsViewModel

class NavigationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //ListViewModel have User, which connect to server
        val listModel = ListViewModel(intent.getLongExtra("id", -1))//check, it not -1. Maybe write throw exception
        val token = intent.getStringExtra("token")
        val token2 = token + token
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
    val statisticsModel = StatisticsViewModel()
    val navigation = rememberNavigation()
    val coroutineScope = rememberCoroutineScope()
    val navBackStackEntry by navigation.navController.currentBackStackEntryAsState()
    val initialRoute =
        navBackStackEntry?.destination?.route ?: Screens.CALENDAR.route
    val isExpandedScreen =false
    val DrawerState = rememberDrawerState(isExpandedScreen)
    val openDrawer: ()-> Unit = { 
        val currentRoute = navigation.navController.currentDestination?.route
        coroutineScope.launch { DrawerState.open() }
    }

    //Main Navigation element  - Drawer open from left side
    //to Navigate pass navigation to function and call navigation.navigateTo()
    //If need more Screen/add to Screen, append to AppDrawer Button with Icons
    //Icons import from https://composeicons.com/

    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                currentRoute = initialRoute,
                navigation,
                closeDrawer = { coroutineScope.launch { DrawerState.close() } }
            )
        },
        drawerState = DrawerState,
        gesturesEnabled = !isExpandedScreen
    ) {
        NavHost(
            navController = navigation.navController,
            startDestination = startDestination,
            modifier = Modifier
        ) {
            composable(route = Screens.SETTINGS.route) {
                SettingsScreen(viewModel = authModel,
                    navigation, openDrawer
                )
            }
            composable(Screens.CALENDAR.route) {
                DailyTasksList(listModel, openDrawer = openDrawer, navigation)
            }
            composable(route = Screens.CHANGE_LOGIN.route) {
                ChangeLogin(authModel, navigation)
            }
            composable(route = Screens.CHANGE_PASSWORD.route) {
                ChangePassword(authModel, navigation)
            }
            composable(route = Screens.STATISTICS.route) {
                Statistics(navigation, openDrawer, statisticsModel)
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
