package org.hse.smartcalendar.ui.elements

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.path
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.hse.smartcalendar.App
import org.hse.smartcalendar.AuthViewModel
import org.hse.smartcalendar.Screen
import org.hse.smartcalendar.data.SettingsButton
import org.hse.smartcalendar.ui.theme.SmartCalendartestTheme

@Preview
@Composable
fun NavigationScreenPreview(){
    SmartCalendartestTheme {
        App(viewModel = AuthViewModel(), startDestination = Screen.Navigation.name)
    }
}

@Composable
fun NavigationScreen(viewModel: AuthViewModel, navController: NavController){
    val items = listOf(
        SettingsButton("Statistics", Icons.Filled.Menu,
            {navController.navigate(Screen.Statistics.name)} ))
    val selectedItem = remember { mutableStateOf(items[0]) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val openDrawer: () -> Unit = { scope.launch { drawerState.open() } }
    val closeDrawer: () -> Unit = { scope.launch { drawerState.close() } }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet{
                items.forEach { item ->
                    NavigationDrawerItem(
                        label={item.DrawButton()},
                        selected = selectedItem.value==item,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem.value = item
                            item.action()
                        }
                    )
                }
            }
        },
        content={
            ActionTopAppbar(openMenu = openDrawer, elevation = 8.dp, navController = navController)
        }
    )
}
