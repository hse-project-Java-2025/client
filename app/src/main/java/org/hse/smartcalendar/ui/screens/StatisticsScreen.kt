package org.hse.smartcalendar.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import org.hse.smartcalendar.data.TotalTimeTaskTypes
import org.hse.smartcalendar.ui.elements.ChartCirclePie
import org.hse.smartcalendar.ui.elements.ChartModel
import org.hse.smartcalendar.ui.elements.CircleColored
import org.hse.smartcalendar.ui.navigation.TopButton
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.utility.rememberNavigation
import org.hse.smartcalendar.view.model.StatisticsViewModel
import org.hse.smartcalendar.view.model.StatisticsViewModel.Companion.toPercent

@Composable
fun StatisticsScreen(navigation: Navigation, openMenu: () -> Unit, statisticsModel: StatisticsViewModel) {
    fun safeDelete(dividend: Long, divisor: Long): Float {
        return if (divisor == 0L || dividend == 0L) 0f else dividend.toFloat() / divisor
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
            Row {
                SafeProgressBox(
                    dividend = statisticsModel.getTodayCompletedTime().time.inWholeMinutes,
                    divisor = statisticsModel.getTodayPlannedTime().time.inWholeMinutes,
                    label = "Completed/Planned",
                    color = Color.Blue,
                    modifier = Modifier.weight(1f / 3f)
                )
                SafeProgressBox(
                    dividend = statisticsModel.getTodayCompletedTime().time.inWholeMinutes,
                    divisor = statisticsModel.getAverageDailyTime().time.inWholeMinutes,
                    label = "Completed/Average",
                    color = Color.Green,
                    modifier = Modifier.weight(1f / 3f)
                )
                SafeProgressBox(
                    dividend = statisticsModel.getTodayContinusSuccessDays().amount.toLong(),
                    divisor = statisticsModel.getRecordContiniusSuccessDays().amount.toLong(),
                    label = "Days in a row, when completed all the tasks",
                    color = Color.Red,
                    modifier = Modifier.weight(1f / 3f)
                )
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
                             typesModel: TotalTimeTaskTypes,
                             columnHeight: Dp
){
    val charts = listOf(
        ChartModel(value = typesModel.StudyPercent, color = DailyTaskType.STUDIES.color),
        ChartModel(value = typesModel.FitnessPercent, color = DailyTaskType.FITNESS.color),
        ChartModel(value = typesModel.CommonPercent, color = DailyTaskType.COMMON.color),
        ChartModel(value = typesModel.WorkPercent, color = DailyTaskType.WORK.color),
    )
    val tableData = mapOf("Work" to Pair(typesModel.WorkPercent, DailyTaskType.WORK.color),
        "Study" to
                Pair(typesModel.StudyPercent, DailyTaskType.STUDIES.color),
        "Fitness" to Pair(typesModel.FitnessPercent, DailyTaskType.FITNESS.color),
        "Common" to Pair(typesModel.CommonPercent, DailyTaskType.COMMON.color))
    Column(verticalArrangement = Arrangement.Center) {
        ChartCirclePie(modifier.align(Alignment.CenterHorizontally), charts)
        tableData.forEach {
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
fun SafeProgressBox(
    dividend: Long,
    divisor: Long,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (dividend == 0L) {
            Box(modifier = modifier.then(Modifier.size(100.dp)), contentAlignment = Alignment.Center) {
                Text("No data")
            }
        } else {
            fun progress(): Float = dividend.toFloat() / divisor
            ProgressCircleWithText(
                progress = { progress() },
                text = label,
                color = color
            )
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
        StatisticsScreen(rememberNavigation(), {}, StatisticsViewModel())
    }
}
