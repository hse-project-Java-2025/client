package org.hse.smartcalendar.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.utility.Screens
import org.hse.smartcalendar.utility.rememberNavigation


@Composable
fun GreetingScreen(navigation: Navigation, modifier: Modifier = Modifier, name: String = "User") {
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
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
                navigation.navigateTo(Screens.LOGIN.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("authorizationButtonTest")) {
            Text("Authorization")
        }
        Button(
            onClick = {
                navigation.navigateTo(Screens.REGISTER.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("registerButtonTest")) {
            Text("Signup")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmartCalendarTheme {
        GreetingScreen(rememberNavigation(), name ="User")
    }
}