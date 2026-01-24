package org.hse.smartcalendar.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hse.smartcalendar.data.WorkManagerHolder
import org.hse.smartcalendar.ui.navigation.App
import org.hse.smartcalendar.ui.theme.SmartCalendarTheme
import org.hse.smartcalendar.view.model.ListViewModel
import org.hse.smartcalendar.view.model.StatisticsManager
import org.hse.smartcalendar.view.model.StatisticsViewModel
import org.hse.smartcalendar.view.model.TaskEditViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetingFlowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testLogin() {
        WorkManagerHolder.init(composeTestRule.activity)
        val statisticsViewModel = StatisticsViewModel()
        val listViewModel = ListViewModel(StatisticsManager(statisticsViewModel))
        val taskEditViewModel = TaskEditViewModel(listViewModel)

        composeTestRule.setContent {
            SmartCalendarTheme {
                App(
                    statisticsVM = statisticsViewModel,
                    listVM = listViewModel,
                    taskEditVM = taskEditViewModel
                )
            }
        }

        composeTestRule.onNodeWithTag("authorizationButtonTest")
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("loginField")
            .assertIsDisplayed()
            .performTextInput("newuser")
        composeTestRule.onNodeWithTag("passwordField")
            .assertIsDisplayed()
            .performTextInput("superpassword")
        composeTestRule.onNodeWithTag("loginSubmitButton")
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitForIdle()
    }
}