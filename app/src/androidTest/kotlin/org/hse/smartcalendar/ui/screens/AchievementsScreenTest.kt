package org.hse.smartcalendar.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.ui.screens.model.AchievementType
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.utility.TimeUtils
import org.hse.smartcalendar.utility.fromMinutesOfDay
import org.hse.smartcalendar.utility.rememberNavigation
import org.hse.smartcalendar.view.model.AbstractListViewModel
import org.hse.smartcalendar.view.model.AbstractStatisticsViewModel
import org.hse.smartcalendar.view.model.StatisticsManager
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
@RunWith(AndroidJUnit4::class)
class AchievementsScreenTest {
    private lateinit var firstTask: DailyTask
    private lateinit var secondTask: DailyTask
    @get:Rule
    val composeTestRule = createComposeRule()
    @Before
    fun initTasks(){
        firstTask = DailyTask(
            title = "first",
            id = UUID.randomUUID(),
            isComplete = false,
            type = DailyTaskType.WORK,
            creationTime = TimeUtils.Companion.getCurrentDateTime(),
            description = "",
            start = LocalTime.Companion.fromMinutesOfDay(0),
            end = LocalTime.Companion.fromMinutesOfDay(300),
            date = TimeUtils.Companion.getCurrentDateTime().date,
        )
        secondTask = DailyTask(
            title = "first",
            id = UUID.randomUUID(),
            isComplete = false,
            type = DailyTaskType.WORK,
            creationTime = TimeUtils.Companion.getCurrentDateTime(),
            description = "",
            start = LocalTime.Companion.fromMinutesOfDay(300),
            end = LocalTime.Companion.fromMinutesOfDay(1440-1),
            date = TimeUtils.Companion.getCurrentDateTime().date,
        )
    }
    fun assertAchievementData(type: AchievementType, text: String){
        composeTestRule
            .onNodeWithTag(type.testTag)
            .onChildren()
            .filter(hasText(text))
            .onFirst()
            .assertExists()
    }
    @Test
    fun achievementsShowsStreak() {
        val statisticsViewModel = AbstractStatisticsViewModel()
        val listViewModel = AbstractListViewModel(StatisticsManager(statisticsViewModel))
//нужно потестить каждый элемент:Planning everything - без заданий 0,
// с заданием 5ч 5/10, c 24ч 24 часа
        composeTestRule.setContent {
            SmartCalendarTheme {
                AchievementsScreen(
                    navigation = rememberNavigation(),
                    openDrawer = {},
                    statisticsModel = statisticsViewModel
                )
            }
        }

        composeTestRule.onNodeWithText("Eternal Flame").assertIsDisplayed()
        listViewModel.addDailyTask(firstTask)
        composeTestRule.runOnIdle {
        }
        assertAchievementData(AchievementType.PlanToday, "5/10")
        listViewModel.addDailyTask(secondTask)
        composeTestRule.runOnIdle {
        }
        assertAchievementData(AchievementType.PlanToday, "24/24")
    }
}