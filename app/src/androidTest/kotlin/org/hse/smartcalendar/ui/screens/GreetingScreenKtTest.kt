package org.hse.smartcalendar.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hse.smartcalendar.activity.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetingFlowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testLogin() {
        composeTestRule.waitForIdle()
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
        composeTestRule.onNodeWithTag("passwordField")
            .performImeAction()
        composeTestRule.onNodeWithTag("loginSubmitButton")
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("loginField")
            .assertDoesNotExist()
    }
}