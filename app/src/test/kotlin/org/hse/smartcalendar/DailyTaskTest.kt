package org.hse.smartcalendar

import junit.framework.TestCase.assertEquals
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import org.junit.Assert.assertThrows
import org.junit.Test

class DailyTaskTest {
    private val title = "Task title"
    private val description = "Task description"
    private val start = LocalTime(10, 0)
    private val end = LocalTime(11, 0)

    @Test
    fun testConstructorOnSimpleParams() {
        val task1 = DailyTask(
            title = title,
            description = description,
            start = start,
            end = end,
        )
        assertEquals(title, task1.getDailyTaskTitle())
        assertEquals(false, task1.isComplete())
        assertEquals(DailyTaskType.COMMON, task1.getDailyTaskType())
        assertEquals(description, task1.getDailyTaskDescription())
        assertEquals(start, task1.getDailyTaskStartTime())
        assertEquals(end, task1.getDailyTaskEndTime())

        val task2 = DailyTask(
            title = title,
            isComplete = true,
            description = description,
            start = start,
            end = end,
        )
        assertEquals(title, task2.getDailyTaskTitle())
        assertEquals(true, task2.isComplete())
        assertEquals(DailyTaskType.COMMON, task2.getDailyTaskType())
        assertEquals(description, task2.getDailyTaskDescription())
        assertEquals(start, task2.getDailyTaskStartTime())
        assertEquals(end, task2.getDailyTaskEndTime())

        val task3 = DailyTask(
            title = title,
            type = DailyTaskType.WORK,
            description = description,
            start = start,
            end = end,
        )
        assertEquals(title, task3.getDailyTaskTitle())
        assertEquals(false, task3.isComplete())
        assertEquals(DailyTaskType.WORK, task3.getDailyTaskType())
        assertEquals(description, task3.getDailyTaskDescription())
        assertEquals(start, task3.getDailyTaskStartTime())
        assertEquals(end, task3.getDailyTaskEndTime())
    }

    @Test
    fun testConstructFailureWithTimeConflict() {
        assertThrows(
            DailyTask.TimeConflictException::class.java
        ) {
            val task = DailyTask(
                title = title,
                description = description,
                start = end,
                end = start,
            )
        }
    }

    @Test
    fun testConstructorFailureWithEmptyTitle() {
        assertThrows(
            DailyTask.EmptyTitleException::class.java
        ) {
            val task = DailyTask(
                title = "",
                description = description,
                start = start,
                end = end,
            )
        }
    }

    @Test
    fun testCompletionChange() {
        val task = DailyTask(
            title = title,
            description = description,
            start = start,
            end = end,
        )

        assertEquals(false, task.isComplete())
        task.setCompletion(false)
        assertEquals(false, task.isComplete())
        task.setCompletion(true)
        assertEquals(true, task.isComplete())
    }

    @Test
    fun testNesting() {
        val task1 = DailyTask(
            title = title,
            description = description,
            start = LocalTime(10, 0),
            end = LocalTime(11, 0),
        )

        val task2 = DailyTask(
            title = title,
            description = description,
            start = LocalTime(11, 0),
            end = LocalTime(12, 0),
        )

        val task3 = DailyTask(
            title = title,
            description = description,
            start = LocalTime(10, 59),
            end = LocalTime(12, 0),
        )

        assertEquals(false, task1.isNestedTasks(task2))
        assertEquals(false, task2.isNestedTasks(task1))
        assertEquals(true, task1.isNestedTasks(task3))
        assertEquals(true, task3.isNestedTasks(task1))
        assertEquals(true, task2.isNestedTasks(task3))
        assertEquals(true, task3.isNestedTasks(task2))
    }

    @Test
    fun testDailyTestFiledChanges() {

    }
}