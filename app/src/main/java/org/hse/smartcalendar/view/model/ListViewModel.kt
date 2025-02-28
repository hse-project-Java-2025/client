package org.hse.smartcalendar.view.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.hse.smartcalendar.data.DailySchedule
import org.hse.smartcalendar.data.DailyTask

class ListViewModel : ViewModel() {
    private var dailyTaskSchedule : DailySchedule = DailySchedule()
    private val _dailyTaskListFlow = MutableStateFlow(dailyTaskSchedule.getDailyTaskList())
    val dailyTaskListFlow: StateFlow<List<DailyTask>> get() = _dailyTaskListFlow

    fun addDailyTask(task : DailyTask) {
        if (!dailyTaskSchedule.addDailyTask(task)) {
            // TODO
        }
    }

    fun removeDailyTask(task : DailyTask) {
        if (!dailyTaskSchedule.removeDailyTask(task)) {
            // TODO
        }
    }
}