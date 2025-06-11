package org.hse.smartcalendar.view.model.state

import org.hse.smartcalendar.data.DailySchedule
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.MainSchedule
import org.hse.smartcalendar.data.User
import org.hse.smartcalendar.view.model.TaskProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions

data class CalcState(val types: Int, val currentStreak: Int, val maxStreak: Int)

class StatisticsCalculatorTest {
    private lateinit var firstTodayTask: DailyTask
    private lateinit var secondTask: DailyTask
    private lateinit var yesterdayTask: DailyTask
    private lateinit var twoDaysAgoTask: DailyTask
    private lateinit var tomorrowTask: DailyTask
    private lateinit var statisticsCalculator: StatisticsCalculator
    private lateinit var mainSchedule: MainSchedule
    @Before
    fun initTask(){
        firstTodayTask = TaskProvider.TodayWorkTask.provide()
        secondTask = TaskProvider.TodayCommonTask.provide()
        yesterdayTask = TaskProvider.YesterdayTask.provide()
        twoDaysAgoTask = TaskProvider.TwoDaysAgoTask.provide()
        tomorrowTask = TaskProvider.TomorrowTask.provide()
        statisticsCalculator = StatisticsCalculator()
        mainSchedule = User.getSchedule()
    }
    @After
    fun clearTask(){
        User.clearSchedule()
    }
    fun assertCalculatorState(calcState: CalcState){
        Assertions.assertEquals(calcState.currentStreak, statisticsCalculator.stats.value.continuesCurrent)
        Assertions.assertEquals(calcState.maxStreak, statisticsCalculator.stats.value.continuesTotal)
        Assertions.assertEquals(calcState.types, statisticsCalculator.stats.value.typesInDay)
    }
    fun addOrDeleteTask(dailySchedule: DailySchedule, task: DailyTask){
        statisticsCalculator.addOrDeleteTask(
            StatisticsCalculator.AddOrDeleteRequest
            (date = task.getTaskDate(), dateTasks = dailySchedule.getDailyTaskList()))
    }
    /**
     * test types & streak in day
     */
    @Test
    fun oneDay(){
        val dailySchedule= mainSchedule.getOrCreateDailySchedule(firstTodayTask.getTaskDate())
        dailySchedule.addDailyTask(firstTodayTask)
        addOrDeleteTask(dailySchedule, firstTodayTask)
        assertCalculatorState(CalcState(types = 1, currentStreak = 0, maxStreak = 0))
        firstTodayTask.setCompletion(true)
        statisticsCalculator.changeTaskCompletion(firstTodayTask)
        assertCalculatorState(CalcState(types = 1, currentStreak = 1, maxStreak = 1))
        dailySchedule.addDailyTask(secondTask)
        addOrDeleteTask(dailySchedule, secondTask)
        assertCalculatorState(CalcState(types = 2, currentStreak = 0, maxStreak = 1))
        secondTask.setCompletion(true)
        statisticsCalculator.changeTaskCompletion(secondTask)
        assertCalculatorState(CalcState(types = 2, currentStreak = 1, maxStreak = 1))
        secondTask.setCompletion(false)
        statisticsCalculator.changeTaskCompletion(secondTask)
        assertCalculatorState(CalcState(types = 2, currentStreak = 0, maxStreak = 1))
        dailySchedule.removeDailyTask(firstTodayTask)
        addOrDeleteTask(dailySchedule, firstTodayTask)
        assertCalculatorState(CalcState(types = 1, currentStreak = 0, maxStreak = 1))
    }

    fun addTask(dailySchedule: DailySchedule, task: DailyTask){
        dailySchedule.addDailyTask(task)
        addOrDeleteTask(dailySchedule, task)
    }
    fun setCompletion(task: DailyTask, isComplete: Boolean){
        task.setCompletion(isComplete)
        statisticsCalculator.changeTaskCompletion(task)
    }
    /**
     * test streak in period twoDaysAgo-tomorrow
     */
    @Test
    fun manyDay(){
        val todaySchedule= mainSchedule.getOrCreateDailySchedule(firstTodayTask.getTaskDate())
        val yesterdaySchedule = mainSchedule.getOrCreateDailySchedule(yesterdayTask.getTaskDate())
        val twoDaysAgoSchedule = mainSchedule.getOrCreateDailySchedule(twoDaysAgoTask.getTaskDate())
        val tomorrowSchedule = mainSchedule.getOrCreateDailySchedule(tomorrowTask.getTaskDate())
        //add task for every schedule
        addTask(todaySchedule, firstTodayTask)
        setCompletion(firstTodayTask, true)
        addTask(twoDaysAgoSchedule, twoDaysAgoTask)
        setCompletion(twoDaysAgoTask, true)
        assertCalculatorState(CalcState(types = 1, currentStreak = 1, maxStreak = 1))
        addTask(yesterdaySchedule, yesterdayTask)
        setCompletion(yesterdayTask, true)
        assertCalculatorState(CalcState(types = 1, currentStreak = 3, maxStreak = 3))
        addTask(tomorrowSchedule, tomorrowTask)
        setCompletion(tomorrowTask, true)
        assertCalculatorState(CalcState(types = 1, currentStreak = 3, maxStreak = 3))
        //next set some to false
        setCompletion(yesterdayTask, false)
        assertCalculatorState(CalcState(types = 1, currentStreak = 1, maxStreak = 3))
        setCompletion(firstTodayTask, true)
        assertCalculatorState(CalcState(types = 1, currentStreak = 1, maxStreak = 3))
        todaySchedule.removeDailyTask(firstTodayTask)
        addOrDeleteTask(todaySchedule, firstTodayTask)
        assertCalculatorState(CalcState(types = 0, currentStreak = 0, maxStreak = 3))
    }
}