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
    )
) : ViewModel() {
    private val _task = mutableStateOf(task)
    val task: State<DailyTask> = _task

    fun updateTitle(newTitle: String) {
        _task.value.setDailyTaskTitle(newTitle)
    }

    fun updateDescription(newDescription: String) {
        _task.value.setDailyTaskDescription(newDescription)
    }
}