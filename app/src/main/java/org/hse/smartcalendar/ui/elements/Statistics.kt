package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.hse.smartcalendar.utility.Navigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Statistics(openMenu:()->Unit, navigation:Navigation) {
    //TODO
    Scaffold(topBar =  { TopButton(openMenu, navigation, "Statistics") }){
        paddingValues ->  Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {Text("TODO")}
    }
}