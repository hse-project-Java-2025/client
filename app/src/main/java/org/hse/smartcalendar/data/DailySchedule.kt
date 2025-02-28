package org.hse.smartcalendar.data

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.LinkedList

class DailySchedule (val date : LocalDate = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault()).date)
{
    private var dailyTasksList: LinkedList<DailyTask> = LinkedList()

    fun addDailyTask(newTask : DailyTask) : Boolean {
        val iterator = dailyTasksList.iterator()
        iterator.forEach { task ->
            if (task.isNestedTasks(newTask)) {
                return false
            }
        }
        dailyTasksList.add(newTask)
        dailyTasksList.sortBy {
            task ->
                task.getDailyTaskStartTime()
        }
        return true
    }

    fun removeDailyTask(newTask : DailyTask) : Boolean {
        val iterator = dailyTasksList.iterator()
        iterator.forEach { task ->
            if (task == newTask) {
                iterator.remove()
                return true
            }
        }
        return false
    }

    fun getDailyTaskList() : List<DailyTask> {
        return dailyTasksList
    }
}
