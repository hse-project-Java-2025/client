package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hse.smartcalendar.AuthViewModel
import org.hse.smartcalendar.R
import org.hse.smartcalendar.activity.App
import org.hse.smartcalendar.ui.theme.*
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.utility.Screens
import org.hse.smartcalendar.view.model.ListViewModel
import org.hse.smartcalendar.view.model.StatisticsViewModel
import org.hse.smartcalendar.view.model.TaskEditViewModel

@Composable
fun AchievementsScreen(navigation: Navigation,
                       openDrawer: (()->Unit)?=null,
                       statisticsModel: StatisticsViewModel) {
    val fire = AchievementData(
        "Eternal Flame",
        R.drawable.fire,
        { i -> "Reach a $i day streak" },
        statisticsModel.getRecordContiniusSuccessDays().getAmount().toLong(),
        listOf(5, 10, 20, 50, 100)
    )
    val plan = AchievementData(
        "Planning everything",
        R.drawable.writing_hand,
        { i -> "Plan $i hours of your time today" },
        statisticsModel.getTodayPlannedTime().toMinutes() / 60,
        listOf(5, 10, 20, 24)
    )
    val common = AchievementData(
        "Types are boring",
        R.drawable.yawning_face,
        { i -> "Spend $i hours with common tasks" },
        statisticsModel.getTotalTimeActivityTypes().common,
        listOf(10, 20, 50, 100, 1000)
    )
    val taskByTask = AchievementData(
        "Hour by Hour",
        R.drawable.tasks_complete,
        { i -> "Work a total of $i hours" },
        statisticsModel.getTotalWorkTime().toMinutes()/60,
        listOf(10, 20, 50, 100, 1000)
    )
    val automatic = AchievementData(
        "Mechanical Focus",
        R.drawable.robot,
        { i -> "Work a total of $i hours last week" },
        statisticsModel.getWeekWorkTime().toMinutes()/60/7,
        listOf(4, 6, 8, 10)
    )
    val totallyBalanced = AchievementData(
        "Totally balanced",
        R.drawable.balance_scale,
        { i -> "Have $i types of task in day" },
        statisticsModel.getTypesInDay(),
        listOf(4)
    )
    val itemsData =
        listOf(fire, plan, common, taskByTask, automatic, totallyBalanced)
    Scaffold(
        topBar = { TopButton(openDrawer, navigation, "Achievements") }
    ) {paddingValues->
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
            content = {
                items(itemsData.size) { index ->
                    AchievementCard(itemsData[index])
                }
            }
        )
    }

}

@Composable
fun AchievementCard(data: AchievementData) {
    val colors: List<Color> = listOf(colorResource(R.color.bronze), Color.LightGray, Color.Yellow, colorResource(R.color.emerald), Color.Cyan, Color.Cyan)
    val backColors: List<Color> = listOf(DarkBlue, Graphite, Purple, Color.DarkGray, DarkRed, DarkRed)
    var level = 0
    var description = data.description
    val lastLevel = data.levels.takeLast(1)[0]
    if (lastLevel <= data.parameter) {
        description = { long -> "You have max level, you achieve:" + data.description(lastLevel) }
        level = data.levels.size-1
    } else {
        while (data.levels[level] <= data.parameter) {
            level++
        }
    }
    Row(
        Modifier
            .padding(32.dp)
    ) {
        Box(modifier = Modifier.weight(0.3f)
            .wrapContentSize(align = Alignment.Center)
            .clip(CircleShape)
            .background(backColors[level])) {
            Row {
                Box(modifier = Modifier.weight(0.1f))
                val painter = painterResource(data.iconId)
                Box(modifier = Modifier.weight(0.8f)) {
                    Image(
                        modifier = Modifier
                            .aspectRatio(painter.intrinsicSize.width / painter.intrinsicSize.height)
                            //.fillMaxSize()
                            .clip(CircleShape),
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(color = colors[level])
                    )
                }
                Box(modifier = Modifier.weight(0.1f))
            }
        }
        Box(modifier = Modifier.weight(0.05f)){

        }
        Column(modifier = Modifier.weight(0.65f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    data.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    data.parameter.toString() + "/" + data.levels[level].toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )

            }
            LinearProgressIndicator(
                progress = { data.parameter.toFloat() / data.levels[level] },
                color = Color.Yellow,
                trackColor = Color.LightGray,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Start)
            )
            Text(description(data.levels.get(level)))
        }
    }
}

data class AchievementData(
    val title: String,
    val iconId: Int, val description: (Long) -> String,
    val parameter: Long,
    val levels: List<Long>
)

@Preview
@Composable
fun AchievementsScreenPreview() {
    SmartCalendarTheme {
        App(
            startDestination = Screens.ACHIEVEMENTS.route
        )
    }
}


@Preview
@Composable
fun AchievementElementPreview() {
    SmartCalendarTheme {
        AchievementCard(
            AchievementData(
                title = "Eternal Flame",
                iconId = R.drawable.fire,
                description = { i -> "Reach a $i day streak" },
                parameter = 5,
                levels = listOf(5, 10, 20, 50, 100)
            )
        )
    }
}