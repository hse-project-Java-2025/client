package org.hse.smartcalendar.data

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.LinkedList
import java.util.UUID

class DailySchedule (val date : LocalDate = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault()).date)
{
    private var dailyTasksList: LinkedList<DailyTask> = LinkedList()

    fun addDailyTask(newTask : DailyTask) : Boolean {
        val iterator = dailyTasksList.iterator()
        iterator.forEach { task ->
            if (task.isNestedTasks(newTask)) {
                throw NestedTaskException(task, newTask)
            }
        }
        dailyTasksList.add(newTask)
        dailyTasksList.sortBy {
            task ->
                task.getDailyTaskStartTime()
        }
        return true
    }

    fun setCompletionById(id: UUID, status: Boolean): Boolean {
        dailyTasksList.forEach { task ->
            if (task.getId() == id && task.isComplete()!=status) {//т.е. у нас complete поэтому не меняем
                task.setCompletion(status)
                return true
            }
        }
        return false
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

    fun removeById(id: UUID): Boolean {
        dailyTasksList.forEach { task ->
            if (task.getId() == id) {
                removeDailyTask(task)
                return true
            }
        }
        return false
    }

    fun getDailyTaskList() : List<DailyTask> {
        return dailyTasksList
    }

    class NestedTaskException(val oldTask: DailyTask, newTask: DailyTask) : IllegalArgumentException(
        "New task have conflict schedule with previous one:\n" +
                "Old task: start = " + oldTask.getDailyTaskStartTime() + "; end = " + oldTask.getDailyTaskEndTime() + "\n" +
                "New task: start = " + newTask.getDailyTaskStartTime() + "; end = " + newTask.getDailyTaskEndTime()
    )
}
