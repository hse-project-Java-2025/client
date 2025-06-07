package org.hse.smartcalendar.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import org.hse.smartcalendar.ui.screens.model.AchievementType
import org.junit.Rule
import org.junit.Test

class AchievementCardTest {
    val type = AchievementType.PlanToday
    val tag = type.testTag
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun nullProgress() {
        composeTestRule.setContent {//cannot setContent twice
            AchievementCard(data = type, parameter = 0L)
        }
        composeTestRule.onNode(
            hasText("Plan ${type.levels[0]} hours of your time today")
                .and(hasParent(hasTestTag(tag)))
        ).assertExists()
    }
    @Test fun mediumProgress() {
        composeTestRule.setContent {
            AchievementCard(data = type, parameter = 6L)
        }
        composeTestRule.onNodeWithTag("${tag}_progress")
            .assertIsDisplayed()
            .assertRangeInfoEquals(ProgressBarRangeInfo(current = 0.6f, range = 0f..1f))
        composeTestRule.onNode(
            hasText("Plan ${type.levels[1]} hours of your time today")
                .and(hasParent(hasTestTag(tag)))
        ).assertExists()
    }
    @Test
    fun maxProgress(){
        val max = type.levels.last()
        composeTestRule.setContent {
            AchievementCard(data = type, parameter = max)
        }
        composeTestRule.onNodeWithTag("${tag}_progress")
            .assertIsDisplayed()
            .assertRangeInfoEquals(ProgressBarRangeInfo(current = 1.0f, range = 0f..1f))
        composeTestRule.onNode(
            hasText("You have max level, you achieve:${type.description(max)}")
                .and(hasParent(hasTestTag(tag)))
        ).assertExists()
    }
}