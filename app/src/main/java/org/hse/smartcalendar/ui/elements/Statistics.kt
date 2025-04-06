package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.view.model.StatisticsViewModel

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
            Text(
                "You work " + statisticsModel.getTodayCompletedTime()
                    .toPrettyString() + " from " + statisticsModel.getTodayWorkTime().toPrettyString() + " planned today"
            )
            LinearProgressIndicator(
                progress = {
                    safeDelete(
                        statisticsModel.getTodayCompletedTime().toMinutes(),
                        statisticsModel.getTodayWorkTime().toMinutes()
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp),
                trackColor = Color.LightGray,
                color = Color.Blue
            )
            Text(
                "You average day work time is " + statisticsModel.getDailyWorkTime()
                    .toPrettyString() + ", now you work " + statisticsModel.getTodayWorkTime()
                    .toPrettyString() + " of it"
            )
            LinearProgressIndicator(
                progress = {
                    safeDelete(
                        dividend = statisticsModel.getTodayWorkTime().toMinutes(),
                        divisor = statisticsModel.getDailyWorkTime().toMinutes()
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp),
                trackColor = Color.LightGray,
                color = Color.Blue
            )
            Text(
                "You completed all task last " + statisticsModel.getTodayContinusSuccessDays()
                    .toPrettyString() + " your record is " + statisticsModel.getRecordContiniusSuccessDays()
                    .toPrettyString()
            )
            LinearProgressIndicator(
                progress = {
                    safeDelete(
                        dividend = statisticsModel.getTodayContinusSuccessDays().getAmount(),
                        divisor = statisticsModel.getRecordContiniusSuccessDays().getAmount()
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp),
                trackColor = Color.LightGray,
                color = Color.Blue
            )
            Text("You total work time is " + statisticsModel.getTotalWorkTime().toPrettyString())
        }
    }
}