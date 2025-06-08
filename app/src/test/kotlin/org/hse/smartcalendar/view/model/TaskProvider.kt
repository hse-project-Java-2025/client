package org.hse.smartcalendar.view.model

import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.utility.TimeUtils
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.utility.fromMinutesOfDay
import java.util.UUID

/**
 * enum - singleton, if instead of provide store values like TaskProvider(task:DailyTask)
 * after it change in tests(task = TaskProvider.TodayWorkTask; task.setCompletion(true))
 * it change in enum
 */
enum class TaskProvider(val provide:()-> DailyTask) {
    TodayWorkTask({
        DailyTask(
            title = "first",
            id = UUID.randomUUID(),
            isComplete = false,
            type = DailyTaskType.WORK,
            creationTime = TimeUtils.getCurrentDateTime(),
            description = "",
            start = LocalTime.fromMinutesOfDay(10),
            end = LocalTime.fromMinutesOfDay(30),
            date = TimeUtils.getCurrentDateTime().date
        )
    }),

    TodayCommonTask({
        DailyTask.fromTime(
            start = LocalTime.fromMinutesOfDay(30),
            end = LocalTime.fromMinutesOfDay(50),
            date = TimeUtils.getCurrentDateTime().date
        )
    }),

    TomorrowTask({
        DailyTask.fromTime(
            start = LocalTime.fromMinutesOfDay(30),
            end = LocalTime.fromMinutesOfDay(50),
            date = TimeUtils.addDaysToNowDate(1)
        )
    }),
    YesterdayTask({
        DailyTask.fromTime(
            start = LocalTime.fromMinutesOfDay(30),
            end = LocalTime.fromMinutesOfDay(50),
            date = TimeUtils.addDaysToNowDate(-1)
        )
    }),
    TwoDaysAgoTask({
        DailyTask.fromTime(
            start = LocalTime.fromMinutesOfDay(30),
            end = LocalTime.fromMinutesOfDay(50),
            date = TimeUtils.addDaysToNowDate(-2)
        )
    }),
    WeekFitnessTask({
        DailyTask.fromTimeAndType(
            start = LocalTime.fromMinutesOfDay(30),
            end = LocalTime.fromMinutesOfDay(50),
            date = TimeUtils.addDaysToNowDate(-6),
            type = DailyTaskType.FITNESS
        )
    });
}