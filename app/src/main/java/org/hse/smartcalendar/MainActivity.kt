package org.hse.smartcalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.hse.smartcalendar.ui.elements.CalendarScreen
import org.hse.smartcalendar.ui.elements.ChangeLogin
import org.hse.smartcalendar.ui.elements.ChangePassword
import org.hse.smartcalendar.ui.elements.DailyTasksList
import org.hse.smartcalendar.ui.elements.SettingsScreen
import org.hse.smartcalendar.ui.elements.Statistics
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.AppDrawer
import org.hse.smartcalendar.utility.Destinations
import org.hse.smartcalendar.utility.NavigationActions
import org.hse.smartcalendar.view.model.ListViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authModel = ListViewModel(intent.getLongExtra("id", -1))
        setContent {
            SmartCalendarTheme {
                App(AuthViewModel(), authModel)
            }
        }
    }
}

enum class Screen {
    Login,
    Calendar,
    Greeting,
    Settings,
    ChangePassword,
    ChangeLogin,
    Statistics,
    Rating,
    Achievements,
    MyCalendars,
    AIAssistant
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    authModel: AuthViewModel,
    listModel: ListViewModel,
    startDestination: String = Destinations.CALENDAR_ROUTE
) {
    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        NavigationActions(navController)
    }

    val coroutineScope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        navBackStackEntry?.destination?.route ?: Destinations.CALENDAR_ROUTE
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val openDrawer: () -> Unit = { coroutineScope.launch { drawerState.open() } }
    val closeDrawer: () -> Unit = { coroutineScope.launch { drawerState.close() } }
    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                navigateToHome = navigationActions.navigateToCalendar,
                navigateToInterests = navigationActions.navigateToSettings,
                closeDrawer = closeDrawer
            )
        },
        drawerState = drawerState,
    ) {

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
        ) {
            composable(route = Destinations.CALENDAR_ROUTE){
                CalendarScreen(openDrawer, navController)
            }
        }
    }
}


/**
 * Determine the drawer state to pass to the modal drawer.
 */
@Composable
private fun rememberSizeAwareDrawerState(isExpandedScreen: Boolean): DrawerState {
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
