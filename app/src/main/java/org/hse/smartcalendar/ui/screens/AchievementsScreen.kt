package org.hse.smartcalendar.ui.screens

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.hse.smartcalendar.R
import org.hse.smartcalendar.ui.navigation.App
import org.hse.smartcalendar.ui.navigation.TopButton
import org.hse.smartcalendar.ui.screens.model.AchievementType
import org.hse.smartcalendar.ui.theme.DarkBlue
import org.hse.smartcalendar.ui.theme.DarkRed
import org.hse.smartcalendar.ui.theme.Graphite
import org.hse.smartcalendar.ui.theme.Purple
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.Navigation
import org.hse.smartcalendar.utility.Screens
import org.hse.smartcalendar.view.model.AbstractStatisticsViewModel
import org.hse.smartcalendar.view.model.ListViewModel
import org.hse.smartcalendar.view.model.StatisticsManager
import org.hse.smartcalendar.view.model.StatisticsViewModel
import org.hse.smartcalendar.view.model.TaskEditViewModel

@Composable
fun AchievementsScreen(navigation: Navigation,
                       openDrawer: (()->Unit)?=null,
                       statisticsModel: AbstractStatisticsViewModel) {
    val itemsData = AchievementType.entries.toTypedArray()
    Scaffold(
        topBar = { TopButton(openDrawer, navigation, "Achievements") }
    ) {paddingValues->
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
            content = {
                items(itemsData.size) { index ->
                        val parameterProvider = itemsData[index].parameterProvider
                        AchievementCard(
                            itemsData[index],
                            parameterProvider.invoke(statisticsModel)
                        )
                }
            }
        )
    }

}

@Composable
fun AchievementCard(data: AchievementType,
                    parameter: Long) {
    val colors: List<Color> = listOf(colorResource(R.color.bronze), Color.LightGray, Color.Yellow, colorResource(R.color.emerald), Color.Cyan, Color.Cyan)
    val backColors: List<Color> = listOf(DarkBlue, Graphite, Purple, Color.DarkGray, DarkRed, DarkRed)
    var level = 0
    var description = data.description
    val lastLevel = data.levels.takeLast(1)[0]
    if (lastLevel <= parameter) {
        description = { long -> "You have max level, you achieve:" + data.description(lastLevel) }
        level = data.levels.size-1
    } else {
        while (data.levels[level] <= parameter) {
            level++
        }
    }
    Row(
        Modifier
            .padding(32.dp)
            .testTag(data.testTag)
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
                    parameter.toString() + "/" + data.levels[level].toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )

            }
            LinearProgressIndicator(
                progress = {parameter.toFloat() / data.levels[level] },
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


@Preview
@Composable
fun AchievementsScreenPreview() {
    val statisticsViewModel: StatisticsViewModel = viewModel()
    val listViewModel = ListViewModel(StatisticsManager(statisticsViewModel))
    val editViewModel = TaskEditViewModel(listViewModel)
    SmartCalendarTheme {
        App(
            startDestination = Screens.ACHIEVEMENTS.route,
            statisticsVM = statisticsViewModel,
            listVM = listViewModel,
            taskEditVM = editViewModel
        )
    }
}


@Preview
@Composable
fun AchievementElementPreview() {
    SmartCalendarTheme {
        AchievementCard(
            AchievementType.Fire,
            parameter = 5
        )
    }
}