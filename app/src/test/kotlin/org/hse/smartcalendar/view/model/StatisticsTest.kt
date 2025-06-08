package org.hse.smartcalendar.view.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.User
import org.hse.smartcalendar.utility.TimeUtils
import org.hse.smartcalendar.view.model.state.CalcState
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
class StatisticsTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var statisticsViewModel: AbstractStatisticsViewModel
    private lateinit var listViewModel: AbstractListViewModel
    private lateinit var todayWorkTask: DailyTask
    private lateinit var todayCommonTask: DailyTask
    private lateinit var tomorrowTask: DailyTask
    private lateinit var weekFitnessTask: DailyTask
    private lateinit var yesterdayTask: DailyTask

    fun addTaskInDay(task: DailyTask){
        listViewModel.changeDailyTaskSchedule(task.getTaskDate())
        listViewModel.addDailyTask(task)
        listViewModel.changeDailyTaskSchedule(TimeUtils.getCurrentDateTime().date)
    }
    fun changeTaskInDay(task: DailyTask, status: Boolean){
        listViewModel.changeDailyTaskSchedule(task.getTaskDate())
        listViewModel.changeTaskCompletion(task, status)
        listViewModel.changeDailyTaskSchedule(TimeUtils.getCurrentDateTime().date)
    }
    fun removeTaskInDay(task: DailyTask){
        listViewModel.changeDailyTaskSchedule(task.getTaskDate())
        listViewModel.removeDailyTask(task)
        listViewModel.changeDailyTaskSchedule(TimeUtils.getCurrentDateTime().date)
    }
    fun assertCalculatorState(calcState: CalcState){
        Assertions.assertEquals(calcState.currentStreak, statisticsViewModel.statisticsCalculator.stats.value.continuesCurrent)
        Assertions.assertEquals(calcState.maxStreak, statisticsViewModel.statisticsCalculator.stats.value.continuesTotal)
        Assertions.assertEquals(calcState.types, statisticsViewModel.statisticsCalculator.stats.value.typesInDay)
    }
    fun setTasks(){
        todayWorkTask = TaskProvider.TodayWorkTask.provide()
        todayCommonTask = TaskProvider.TodayCommonTask.provide()
        tomorrowTask = TaskProvider.TomorrowTask.provide()
        weekFitnessTask = TaskProvider.WeekFitnessTask.provide()
        yesterdayTask = TaskProvider.YesterdayTask.provide()
    }
    @BeforeAll
    fun setUp(){//Global Init, ONE call before ALL tests
        Dispatchers.setMain(testDispatcher)
    }

    @AfterAll//Global Clear, ONE call before ALL tests
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @BeforeEach//Init, call before EACH test
    fun init(){
        statisticsViewModel = AbstractStatisticsViewModel()
        listViewModel = AbstractListViewModel(StatisticsManager(statisticsViewModel))
        setTasks()
    }
    @AfterEach//Clear, call after EACH test
    fun clear(){
        User.clearSchedule()
    }
    /**
     * check state in BeforeAll in WithPreAddedTasks: Add 4 tasks
     */
    @Nested
    inner class EmptyState {
        @Test
        fun addTaskTest(){
            listViewModel.addDailyTask(todayWorkTask)
            assertEquals<Long>(
                this@StatisticsTest.todayWorkTask.getMinutesLength().toLong(),
                statisticsViewModel.getTodayPlannedTime().time.inWholeMinutes,
            )
            listViewModel.addDailyTask(todayCommonTask)
            assertEquals<Long>(
                (todayWorkTask.getMinutesLength() + todayCommonTask.getMinutesLength()).toLong(),
                statisticsViewModel.getTodayPlannedTime().time.inWholeMinutes,
            )
            addTaskInDay(tomorrowTask)
            assertEquals<Long>(
                (todayWorkTask.getMinutesLength() + todayCommonTask.getMinutesLength()).toLong(),
                statisticsViewModel.getTodayPlannedTime().time.inWholeMinutes,
            )
            addTaskInDay(weekFitnessTask)
            assertEquals<Long>(
                (todayWorkTask.getMinutesLength() + todayCommonTask.getMinutesLength()).toLong(),
                statisticsViewModel.getTodayPlannedTime().time.inWholeMinutes,
            )
        }
        @Test
        fun calculatorAddTest(){
            listViewModel.addDailyTask(todayWorkTask)
            assertCalculatorState(CalcState(types = 1, currentStreak = 0, maxStreak = 0))
            listViewModel.addDailyTask(todayCommonTask)
            assertCalculatorState(CalcState(types = 2, currentStreak = 0, maxStreak = 0))
            addTaskInDay(tomorrowTask)
            assertCalculatorState(CalcState(types = 2, currentStreak = 0, maxStreak = 0))
            addTaskInDay(weekFitnessTask)
            assertCalculatorState(CalcState(types = 2, currentStreak = 0, maxStreak = 0))
            listViewModel.removeDailyTask(todayCommonTask)
            assertCalculatorState(CalcState(types = 1, currentStreak = 0, maxStreak = 0))
        }
    }
    @Nested
    inner class WithPreAddedTasks{
        @BeforeEach
        fun addTasks(){
            listViewModel.addDailyTask(todayWorkTask)
            listViewModel.addDailyTask(todayCommonTask)
            addTaskInDay(tomorrowTask)
            addTaskInDay(weekFitnessTask)
        }
        @Test
        fun deleteTaskTest(){
            listViewModel.removeDailyTask(todayWorkTask)
            assertEquals<Long>(
                (todayCommonTask.getMinutesLength()).toLong(),
                statisticsViewModel.getTodayPlannedTime().time.inWholeMinutes
            )
        }
        fun assertTaskTimeEquals(task: DailyTask, parameter: Long){
            assertEquals<Long>(
                task.getMinutesLength().toLong(),
                parameter
            )
        }
        fun assertTaskTimeEquals(task: DailyTask, parameters: List<Long>){
            for (parameter in parameters){
                assertTaskTimeEquals(task, parameter)
            }
        }
        fun assertTaskTimeEquals(tasks:List<DailyTask>, parameters: List<Long>){
            var sumTime: Long= 0
            for (task in tasks){
                sumTime+=task.getMinutesLength()
            }
            for (parameter in parameters){
                assertEquals<Long>(sumTime, parameter)
            }
        }
        @Test
        fun completeTaskTest(){
            listViewModel.changeTaskCompletion(todayWorkTask, true)
            assertTaskTimeEquals(
                todayWorkTask,
                listOf(statisticsViewModel.getWeekWorkTime().time.inWholeMinutes,
                    statisticsViewModel.getTodayCompletedTime().time.inWholeMinutes,
                    statisticsViewModel.getTotalWorkTime().time.inWholeMinutes,
                    statisticsViewModel.getTotalTimeActivityTypes().Work.time.inWholeMinutes)
            )
            assertEquals(
                100.0f,
                statisticsViewModel.getTotalTimeActivityTypes().WorkPercent
            )
            listViewModel.changeTaskCompletion(todayWorkTask, true)
            assertTaskTimeEquals(
                todayWorkTask,
                listOf(statisticsViewModel.getWeekWorkTime().time.inWholeMinutes,
                    statisticsViewModel.getTodayCompletedTime().time.inWholeMinutes,
                    statisticsViewModel.getTotalWorkTime().time.inWholeMinutes,
                    statisticsViewModel.getTotalTimeActivityTypes().Work.time.inWholeMinutes)
            )
            assertEquals(
                100.0f,
                statisticsViewModel.getTotalTimeActivityTypes().WorkPercent
            )
            listViewModel.changeTaskCompletion(todayCommonTask, true)
            assertTaskTimeEquals(
                listOf(todayWorkTask, todayCommonTask),
                listOf(statisticsViewModel.getWeekWorkTime().time.inWholeMinutes,
                    statisticsViewModel.getTodayCompletedTime().time.inWholeMinutes,
                    statisticsViewModel.getTotalWorkTime().time.inWholeMinutes)
            )
            assertTaskTimeEquals(
                todayWorkTask,
                statisticsViewModel.getTotalTimeActivityTypes().Work.time.inWholeMinutes
            )
            assertTaskTimeEquals(
                todayCommonTask,
                statisticsViewModel.getTotalTimeActivityTypes().Common.time.inWholeMinutes
            )
            listViewModel.changeTaskCompletion(todayCommonTask, false)
            assertTaskTimeEquals(
                todayWorkTask,
                listOf(statisticsViewModel.getWeekWorkTime().time.inWholeMinutes,
                    statisticsViewModel.getTodayCompletedTime().time.inWholeMinutes,
                    statisticsViewModel.getTotalWorkTime().time.inWholeMinutes,
                    statisticsViewModel.getTotalTimeActivityTypes().Work.time.inWholeMinutes)
            )
        }
        @Test
        fun completeAnotherDayTaskTest(){
            changeTaskInDay(tomorrowTask, true)
            assertTaskTimeEquals(
                listOf(),
                listOf(statisticsViewModel.getWeekWorkTime().time.inWholeMinutes,
                    statisticsViewModel.getTodayCompletedTime().time.inWholeMinutes,
                    statisticsViewModel.getTotalTimeActivityTypes().Work.time.inWholeMinutes)
            )
            assertTaskTimeEquals(
                listOf(tomorrowTask),
                listOf(statisticsViewModel.getTotalWorkTime().time.inWholeMinutes,
                    statisticsViewModel.getTotalTimeActivityTypes().Common.time.inWholeMinutes)
            )
            changeTaskInDay(weekFitnessTask, true)
            assertTaskTimeEquals(
                weekFitnessTask,
                listOf(statisticsViewModel.getWeekWorkTime().time.inWholeMinutes,
                    statisticsViewModel.getTotalTimeActivityTypes().Fitness.time.inWholeMinutes)
            )
            assertTaskTimeEquals(
                listOf(tomorrowTask, weekFitnessTask),
                listOf(statisticsViewModel.getTotalWorkTime().time.inWholeMinutes)
            )
            assertEquals(50.0f,
                statisticsViewModel.getTotalTimeActivityTypes().FitnessPercent)
        }

        /**
         * удаление таски помеченной/не помеченной завершённой сегодня/ завтра
         */
        @Test
        fun integrityTest(){
            changeTaskInDay(tomorrowTask, true)
            changeTaskInDay(weekFitnessTask, true)
            changeTaskInDay(todayWorkTask, true)
            changeTaskInDay(weekFitnessTask, false)
            changeTaskInDay(weekFitnessTask, true)
            assertTaskTimeEquals(
                listOf(weekFitnessTask, todayWorkTask),
                listOf(statisticsViewModel.getWeekWorkTime().time.inWholeMinutes)
            )
            assertEquals(100.0f/3,
                statisticsViewModel.getTotalTimeActivityTypes().FitnessPercent,
                absoluteTolerance = 0.1f)
            listViewModel.removeDailyTask(todayWorkTask)
            assertTaskTimeEquals(
                listOf(),
                listOf(statisticsViewModel.getTodayCompletedTime().time.inWholeMinutes)
            )
            assertTaskTimeEquals(
                listOf(weekFitnessTask),
                listOf(statisticsViewModel.getWeekWorkTime().time.inWholeMinutes)
            )
            assertTaskTimeEquals(
                listOf(weekFitnessTask, tomorrowTask),
                listOf(statisticsViewModel.getTotalWorkTime().time.inWholeMinutes)
            )
            changeTaskInDay(tomorrowTask, false)
            assertTaskTimeEquals(
                listOf(weekFitnessTask),
                listOf(statisticsViewModel.getTotalWorkTime().time.inWholeMinutes)
            )
            removeTaskInDay(tomorrowTask)
            assertTaskTimeEquals(
                listOf(weekFitnessTask),
                listOf(statisticsViewModel.getTotalWorkTime().time.inWholeMinutes)
            )
            assertEquals(100.0f,
                statisticsViewModel.getTotalTimeActivityTypes().FitnessPercent,
                absoluteTolerance = 0.1f)
        }

        /**
         * check what listVM changes apply to statsCalculator
         */
        @Test
        fun calculateTest(){
            assertCalculatorState(CalcState(types = 2, currentStreak = 0, maxStreak = 0))
            changeTaskInDay(tomorrowTask, true)
            assertEquals(0, statisticsViewModel.statisticsCalculator.stats.value.continuesCurrent)
            changeTaskInDay(todayWorkTask, true)
            assertEquals(0, statisticsViewModel.statisticsCalculator.stats.value.continuesCurrent)
            changeTaskInDay(todayCommonTask, true)
            assertCalculatorState(CalcState(types = 2, currentStreak = 1, maxStreak = 1))
            addTaskInDay(yesterdayTask)
            changeTaskInDay(yesterdayTask, true)
            assertCalculatorState(CalcState(types = 2, currentStreak = 2, maxStreak = 2))
            removeTaskInDay(todayWorkTask)
            removeTaskInDay(todayCommonTask)
            assertCalculatorState(CalcState(types = 0, currentStreak = 0, maxStreak = 2))
        }
    }
}

