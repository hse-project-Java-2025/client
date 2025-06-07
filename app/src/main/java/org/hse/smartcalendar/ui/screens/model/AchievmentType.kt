package org.hse.smartcalendar.ui.screens.model

import androidx.annotation.DrawableRes
import org.hse.smartcalendar.R
import org.hse.smartcalendar.view.model.AbstractStatisticsViewModel
import kotlin.time.DurationUnit

enum class AchievementType(
    val title: String,
    @DrawableRes val iconId: Int,
    val description: (Long) -> String,
    val levels: List<Long>,
    val parameterProvider: (AbstractStatisticsViewModel) -> Long,
    val testTag: String
) {
    Fire(
        title = "Eternal Flame",
        iconId = R.drawable.fire,
        description = { i -> "Reach a $i day streak" },
        levels = listOf(5, 10, 20, 50, 100),
        parameterProvider = { stats -> stats.getRecordContinuesSuccessDays().amount.toLong() },
        testTag = "Fire"
    ),
    PlanToday(
        title = "Planning everything",
        iconId = R.drawable.writing_hand,
        description = { i -> "Plan $i hours of your time today" },
        levels = listOf(5, 10, 20, 24),
        parameterProvider = { stats -> (stats.getTodayPlannedTime().toMinutes() + 1) / 60 },
        testTag = "PlanToday"
    ),
    CommonSpend(
        title = "Types are boring",
        iconId = R.drawable.yawning_face,
        description = { i -> "Spend $i hours with common tasks" },
        levels = listOf(10, 20, 50, 100, 1000),
        parameterProvider = { stats -> stats.TotalTime.Common.time.toLong(DurationUnit.HOURS) },
        testTag = "CommonSpend"
    ),
    WorkTotal(
        title = "Hour by Hour",
        iconId = R.drawable.tasks_complete,
        description = { i -> "Work a total of $i hours" },
        levels = listOf(10, 20, 50, 100, 1000),
        parameterProvider = { stats -> stats.getTotalWorkTime().toMinutes() / 60 },
        testTag = "WorkTotal"
    ),
    WorkWeek(
        title = "Mechanical Focus",
        iconId = R.drawable.robot,
        description = { i -> "Work a total of $i hours last week" },
        levels = listOf(4, 6, 8, 10),
        parameterProvider = { stats -> stats.getWeekWorkTime().toMinutes() / 60 / 7 },
        testTag = "WorkWeek"
    ),
    TypesToday(
        title = "Totally balanced",
        iconId = R.drawable.balance_scale,
        description = { i -> "Have $i types of task in day" },
        levels = listOf(4),
        parameterProvider = { stats -> stats.getTypesInCurrentDay().toLong() },
        testTag = "TypesToday"
    );
}