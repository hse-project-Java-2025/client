package org.hse.smartcalendar.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WeatherPage(viewModel: WeatherViewModel){
    var city by remember{
        mutableStateOf("")
    }
    val weatherResult = viewModel.weatherResult.observeAsState()
    Column (
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        ->
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = city, onValueChange = {
                city = it
            },
                label = {
                    Text(text = "Search for any location")
                }
                )
        }
        IconButton(onClick = {
            viewModel.getData(city)
        }) {
            Icon(imageVector = Icons.Default.Search,
                contentDescription = "Search for any location")
        }
        when (val result = weatherResult.value){
            is NetworkResponseSample.Error -> {
                Text(text = result.message)
            }
            NetworkResponseSample.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponseSample.Success ->{
                Text(text = result.data.toString())
            }
            null -> {}
        }
    }
}