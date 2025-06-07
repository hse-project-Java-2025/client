package org.hse.smartcalendar.view.model

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.data.User
import org.hse.smartcalendar.data.WorkManagerHolder
import org.hse.smartcalendar.utility.TimeUtils
import org.hse.smartcalendar.utility.fromMinutesOfDay
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.UUID
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
class StatisticsTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var statisticsViewModel: AbstractStatisticsViewModel
    private lateinit var listViewModel: AbstractListViewModel
    private lateinit var firstTask: DailyTask
    private lateinit var secondTask: DailyTask
    private lateinit var tomorrowTask: DailyTask
    private lateinit var weekFitnessTask: DailyTask

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
    @BeforeAll
    fun setUp(){
        Dispatchers.setMain(testDispatcher)
        statisticsViewModel = AbstractStatisticsViewModel()
        listViewModel = AbstractListViewModel(StatisticsManager(statisticsViewModel))
        firstTask = DailyTask(
            title = "first",
            id = UUID.randomUUID(),
            isComplete = false,
            type = DailyTaskType.WORK,
            creationTime = TimeUtils.Companion.getCurrentDateTime(),
            description = "",
            start = LocalTime.Companion.fromMinutesOfDay(10),
            end = LocalTime.Companion.fromMinutesOfDay(30),
            date = TimeUtils.Companion.getCurrentDateTime().date,
        )
        secondTask = DailyTask.fromTime(
            start = LocalTime.Companion.fromMinutesOfDay(30),
            end = LocalTime.Companion.fromMinutesOfDay(50),
            date = TimeUtils.Companion.getCurrentDateTime().date)
        tomorrowTask = DailyTask.fromTime(
            start = LocalTime.Companion.fromMinutesOfDay(30),
            end = LocalTime.Companion.fromMinutesOfDay(50),
            date = TimeUtils.addDaysToNowDate(1)
        )
        weekFitnessTask = DailyTask.fromTimeAndType(
            start = LocalTime.Companion.fromMinutesOfDay(30),
            end = LocalTime.Companion.fromMinutesOfDay(50),
            date = TimeUtils.addDaysToNowDate(-6),
            type = DailyTaskType.FITNESS
        )
    }

    @AfterAll
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Nested
    inner class EmptyState {
        @Test
        fun addTaskTest(){
            listViewModel.addDailyTask(firstTask)
            assertEquals<Long>(
                firstTask.getMinutesLength().toLong(),
                statisticsViewModel.getTodayPlannedTime().time.inWholeMinutes,
            )
            listViewModel.addDailyTask(secondTask)
            assertEquals<Long>(
                (firstTask.getMinutesLength() + secondTask.getMinutesLength()).toLong(),
                statisticsViewModel.getTodayPlannedTime().time.inWholeMinutes,
            )
            addTaskInDay(tomorrowTask)
            assertEquals<Long>(
                (firstTask.getMinutesLength() + secondTask.getMinutesLength()).toLong(),
                statisticsViewModel.getTodayPlannedTime().time.inWholeMinutes,
            )
            addTaskInDay(weekFitnessTask)
            assertEquals<Long>(
                (firstTask.getMinutesLength() + secondTask.getMinutesLength()).toLong(),
                statisticsViewModel.getTodayPlannedTime().time.inWholeMinutes,
            )
        }
    }
    @Nested
    inner class WithPreAddedTasks{
        @BeforeEach
        fun addTasks(){
            //User - синглтон
            User.clearSchedule()
            //нам нужен чистый listViewModel перед каждым тестом
            statisticsViewModel = AbstractStatisticsViewModel()
            listViewModel = AbstractListViewModel(StatisticsManager(statisticsViewModel))
            listViewModel.addDailyTask(firstTask)
            listViewModel.addDailyTask(secondTask)
            addTaskInDay(tomorrowTask)
            addTaskInDay(weekFitnessTask)
        }
        @Test
        fun deleteTaskTest(){
            listViewModel.removeDailyTask(firstTask)
            assertEquals<Long>(
                (secondTask.getMinutesLength()).toLong(),
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
            listViewModel.changeTaskCompletion(firstTask, true)
            assertTaskTimeEquals(
                firstTask,
                listOf(statisticsViewModel.getWeekWorkTime().time.inWholeMinutes,
                    statisticsViewModel.getTodayCompletedTime().time.inWholeMinutes,
                    statisticsViewModel.getTotalWorkTime().time.inWholeMinutes,
                    statisticsViewModel.getTotalTimeActivityTypes().Work.time.inWholeMinutes)
            )
            assertEquals(
                100.0f,
                statisticsViewModel.getTotalTimeActivityTypes().WorkPercent
            )
            listViewModel.changeTaskCompletion(firstTask, true)
            assertTaskTimeEquals(
                firstTask,
                listOf(statisticsViewModel.getWeekWorkTime().time.inWholeMinutes,
                    statisticsViewModel.getTodayCompletedTime().time.inWholeMinutes,
                    statisticsViewModel.getTotalWorkTime().time.inWholeMinutes,
                    statisticsViewModel.getTotalTimeActivityTypes().Work.time.inWholeMinutes)
            )
            assertEquals(
                100.0f,
                statisticsViewModel.getTotalTimeActivityTypes().WorkPercent
            )
            listViewModel.changeTaskCompletion(secondTask, true)
            assertTaskTimeEquals(
                listOf(firstTask, secondTask),
                listOf(statisticsViewModel.getWeekWorkTime().time.inWholeMinutes,
                    statisticsViewModel.getTodayCompletedTime().time.inWholeMinutes,
                    statisticsViewModel.getTotalWorkTime().time.inWholeMinutes)
            )
            assertTaskTimeEquals(
                firstTask,
                statisticsViewModel.getTotalTimeActivityTypes().Work.time.inWholeMinutes
            )
            assertTaskTimeEquals(
                secondTask,
                statisticsViewModel.getTotalTimeActivityTypes().Common.time.inWholeMinutes
            )
            listViewModel.changeTaskCompletion(secondTask, false)
            assertTaskTimeEquals(
                firstTask,
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
            changeTaskInDay(firstTask, true)
            changeTaskInDay(weekFitnessTask, false)
            changeTaskInDay(weekFitnessTask, true)
            assertTaskTimeEquals(
                listOf(weekFitnessTask, firstTask),
                listOf(statisticsViewModel.getWeekWorkTime().time.inWholeMinutes)
            )
            assertEquals(100.0f/3,
                statisticsViewModel.getTotalTimeActivityTypes().FitnessPercent,
                absoluteTolerance = 0.1f)
            listViewModel.removeDailyTask(firstTask)
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
    }
}

