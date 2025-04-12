package org.hse.smartcalendar.view.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask

class TaskEditViewModel(
    task: DailyTask = DailyTask(
        title = "Preview title",
        description = "Preview description",
        start = LocalTime(0, 0),
        end = LocalTime(23, 59)
    ),
    val listViewModel: ListViewModel
) : ViewModel() {
    private var _task = mutableStateOf(task)
    val changes = task
    var test = task
    val task: State<DailyTask> = _task

    fun setTask(newTask: DailyTask) {
        _task = mutableStateOf(newTask)
        test = newTask
        changes.updateDailyTask(newTask)
    }

    fun updateTask() {
        if (listViewModel.isUpdatable(_task.value, changes)) {
            _task.value.updateDailyTask(changes)
        }
    }
}