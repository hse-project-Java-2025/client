package org.hse.smartcalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.hse.smartcalendar.ui.theme.SmartCalendartestTheme
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCalendartestTheme {
                setContent {
                    App(viewModel = AuthViewModel())
                }
            }
        }
    }
}

enum class Screen {
    Login,
    Calendar,
    Greeting,
}

@Composable
fun App(
    viewModel: AuthViewModel,
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.Greeting.name
    )
    Scaffold(
        topBar = {//for revert navigation
            //...
        }
    )
    { innerPadding->

        NavHost(
            navController = navController,
            startDestination = Screen.Greeting.name,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ){
            composable(route = Screen.Login.name){
                AuthScreen(viewModel, navController)
            }
            composable(route=Screen.Calendar.name) {
                CalendarScreen(viewModel)
            }
            composable(route=Screen.Greeting.name) {
                Greeting("user", navController)
            }
        }
    }
}

@Composable
fun Greeting(name: String, navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )

        Spacer(modifier = Modifier.
        padding(8.dp))

        Button(
            onClick = {
                navController.navigate(Screen.Login.name)
            },
            modifier = Modifier.fillMaxWidth()) {
            Text("Authorization")
        }
        Button(
            onClick = {
            },
            modifier = Modifier.fillMaxWidth()) {
            Text("Calendar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmartCalendartestTheme {
        Greeting("User", navController = rememberNavController())
    }
}

/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCalendartestTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "AuthScreen"
                ) {

                    composable("AuthScreen") {
                        AuthScreen();
                    }

                    composable("Calendar") {
                        Calendar();
                    }
                };
            }
        }
    }
}


@Composable
fun AppTheme(content: () -> Unit) {

}


@Composable
fun AuthScreen(){
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Greeting(
            name = "User",
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun Calendar(){

}
*/