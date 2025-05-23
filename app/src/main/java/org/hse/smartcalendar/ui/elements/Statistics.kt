package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.utility.rememberNavigation
import org.hse.smartcalendar.view.model.StatisticsViewModel
import org.hse.smartcalendar.view.model.StatisticsViewModel.Companion.toPercent

@Composable
fun Statistics(navigation: Navigation, openMenu: () -> Unit, statisticsModel: StatisticsViewModel) {
    fun safeDelete(dividend: Long, divisor: Long): Float {
        if (divisor == (0).toLong()) {
            return (0).toFloat()
        } else {
            return (dividend).toFloat() / divisor
        }
    }
    fun safeDelete(dividend: Int, divisor: Int): Float {
        return safeDelete(dividend.toLong(), divisor.toLong())
    }

    Scaffold(topBar = { TopButton(openMenu, navigation, "Statistics") }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(){
                Box(modifier = Modifier.weight(1.0f / 3)) {
                    fun progress():Float{ return safeDelete(
                        statisticsModel.getTodayCompletedTime().toMinutes(),
                        statisticsModel.getTodayPlannedTime().toMinutes()
                    )}
                    ProgressCircleWithText(
                        progress = { progress() },
                        text = "Completed/Planned",
                        color = Color.Blue
                    )
                }
                Box(modifier = Modifier.weight(1.0f / 3)) {
                    fun progress():Float{ return safeDelete(
                        statisticsModel.getTodayCompletedTime().toMinutes(),
                        statisticsModel.getAverageDailyTime().toMinutes()
                    )}
                    ProgressCircleWithText(
                        progress = { progress() },
                        text = "Completed/Average",
                        color = Color.Green
                    )
                }
                Box(modifier = Modifier.weight(1.0f / 3)) {
                    fun progress():Float{return statisticsModel.getTodayContinusSuccessDays().getAmount().toFloat()/
                            statisticsModel.getRecordContiniusSuccessDays().getAmount()
                    }
                    ProgressCircleWithText(
                        progress = { progress() },
                        text = "Days in a row, when completed all the tasks",
                        color = Color.Red
                    )
                }
            }
            Column(verticalArrangement = Arrangement.Center) {
                Row {
                    Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.Center){
                        Column {
                            Text("Average work time:")
                            Text("Today planned work time:")
                            Text("Total work time:")
                            Text("Maximum days in a row when all tasks are completed:")
                        }
                    }
                    Box(modifier = Modifier.weight(0.5f)){
                        Column {
                            Text(statisticsModel.getAverageDailyTime().toFullString())
                            Text(statisticsModel.getTodayPlannedTime().toFullString())
                            Text(statisticsModel.getTotalWorkTime().toPrettyString())
                            Text(statisticsModel.getRecordContiniusSuccessDays().toPrettyString())
                        }
                    }
                }
            }
            ActivityTypesDataDisplay(typesModel = statisticsModel.getTotalTimeActivityTypes(),
                columnHeight = 8.dp);
        }
    }
}
@Composable
fun ActivityTypesDataDisplay(modifier: Modifier=Modifier,
                             typesModel: StatisticsViewModel.TotalTimeTaskTypes,
                             columnHeight: Dp
){
    val charts = listOf(
        ChartModel(value = typesModel.StudyPercent, color = DailyTaskType.STUDIES.color),
        ChartModel(value = typesModel.FitnessPercent, color = DailyTaskType.FITNESS.color),
        ChartModel(value = typesModel.CommonPercent, color = DailyTaskType.COMMON.color),
        ChartModel(value = typesModel.WorkPercent, color = DailyTaskType.WORK.color),
    )
    val TableData = mapOf("Work" to Pair(typesModel.WorkPercent, DailyTaskType.WORK.color),
        "Study" to
                Pair(typesModel.StudyPercent, DailyTaskType.STUDIES.color),
        "Fitness" to Pair(typesModel.FitnessPercent, DailyTaskType.FITNESS.color),
        "Common" to Pair(typesModel.CommonPercent, DailyTaskType.COMMON.color))
    Column(verticalArrangement = Arrangement.Center) {
        ChartCirclePie(modifier.align(Alignment.CenterHorizontally), charts)
        TableData.forEach {
            val (text, data) = it
            val (percent, color) = data
            Row(
                Modifier
                    .padding(columnHeight).align(Alignment.CenterHorizontally)
            ) {
                Box(modifier = modifier.weight(0.1f)) {
                    CircleColored(modifier.size(24.dp).align(Alignment.CenterStart), color)
                }
                Text(text, modifier = modifier.weight(0.4f))
                Text("$percent%", modifier = modifier.weight(0.5f))
            }
        }
    }
}
@Composable
fun ProgressCircleWithText(progress: ()->Float, text: String, color: Color){
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Box(contentAlignment = Alignment.Center) {
            Text(toPercent(progress()).toString()+"%")
            CircularProgressIndicator(
                progress = progress ,
                modifier = Modifier.then(Modifier.size(100.dp)),
                color = color,
                trackColor = Color.LightGray
            )
        }
        Text(text)
    }
}
@Preview
@Composable
fun StatisticsPreview(){
    SmartCalendarTheme {
        Statistics(rememberNavigation(), {}, StatisticsViewModel())
    }
}