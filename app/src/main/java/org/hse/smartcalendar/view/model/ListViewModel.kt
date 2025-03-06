package org.hse.smartcalendar.view.model

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import org.hse.smartcalendar.data.DailySchedule
import org.hse.smartcalendar.data.DailyTask

class ListViewModel : ViewModel() {
    private var dailyTaskSchedule : DailySchedule = DailySchedule()
    var dailyTaskList = SnapshotStateList<DailyTask>()

    fun addDailyTask(newTask : DailyTask) {
        if (!dailyTaskSchedule.addDailyTask(newTask)) {
            // TODO
        } else {
            dailyTaskList.add(newTask)
            dailyTaskList.sortBy {
                    task ->
                task.getDailyTaskStartTime()
            }
        }
    }

    fun removeDailyTask(task : DailyTask) {
        if (!dailyTaskSchedule.removeDailyTask(task)) {
            // TODO
        } else {
            dailyTaskList.remove(task)
        }
    }
}