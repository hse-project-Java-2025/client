package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme

@Composable
fun AchievmentsScreen(){

}
class AchievementElement(val title: String, val icon: ImageVector,
                         val description: (Long)->String,
                         val parameter: Long,
                         val levels: List<Long>,
                         val modifier: Modifier=Modifier){
    //Можно менять иконку с уровнем
    @Composable
    fun DrawButton(){
        var level=0
        while (levels.get(level)>parameter){
            level++
        }
        Row(
            Modifier
                .padding(24.dp)
        ) {
            Box(modifier = modifier.weight(0.3f)) {
                Icon(icon, null, modifier = Modifier.fillMaxWidth())
            }
            Column(modifier = modifier.weight(0.7f)){
                Text(title)
                LinearProgressIndicator(
                    progress = { parameter.toFloat()/ levels[level] },
                    color = Color.Yellow,
                    trackColor = Color.LightGray,
                    modifier = Modifier.padding(8.dp)
                )
                Text(description(levels[level]))
            }
        }
    }
}
@Preview
@Composable
fun AchievementElementPreview(){
    SmartCalendarTheme {
        AchievementElement(
            title = "Eternal Flame",
            icon = Icons.Filled.Info,
            description = {i->"Reach a $i day streak"},
            parameter = 5,
            levels = listOf(5, 10, 20,50, 100),
            modifier = Modifier
        ).DrawButton()
    }
}