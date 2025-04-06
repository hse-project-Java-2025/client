package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.hse.smartcalendar.utility.Navigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Statistics(openMenu:()->Unit, navigation:Navigation) {
    //TODO: need data from server done/failed tasks today/all time
    Scaffold(topBar =  { TopButton(openMenu, navigation, "Statistics") }){
        paddingValues ->  Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {Text("TODO")
        Row(modifier = Modifier.fillMaxWidth()) {
            Column {
                Text("You work X hours from Y planned today:")
                LinearProgressIndicator(
                    progress = { 10f / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    trackColor = Color.Blue,
                    color = Color.LightGray
                )
            }
            Column {
                Text("You average day work time is:")
                LinearProgressIndicator(
                    progress = { 10f / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    trackColor = Color.Blue,
                    color = Color.LightGray
                )
            }
            Column {
                Text("You completed all tasks last X days, your record is Y")
                LinearProgressIndicator(
                    progress = { 10f / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    trackColor = Color.Blue,
                    color = Color.LightGray
                )
            }
        }
    }
    }
}
