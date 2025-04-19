package org.hse.smartcalendar.view.model

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.utility.editHandler

class TaskEditViewModel(
    val listViewModel: ListViewModel
) : ViewModel() {
    private var task: DailyTask = DailyTask(
        title = "Preview title",
        description = "Preview description",
        start = LocalTime(0, 0),
        end = LocalTime(23, 59)
    )
    val changes = task

    fun getTask(): DailyTask {
        return task
    }

    fun setTask(newTask: DailyTask) {
        task = newTask
        changes.updateDailyTask(newTask)
    }

    fun updateInnerTask(
        isEmptyTitle: MutableState<Boolean>,
        isConflictInTimeField: MutableState<Boolean>,
        isNestedTask: MutableState<Boolean>,
    ) {
        editHandler(
            oldTask = task,
            newTask = changes,
            viewModel = listViewModel,
            isEmptyTitle = isEmptyTitle,
            isConflictInTimeField = isConflictInTimeField,
            isNestedTask = isNestedTask
        )
    }
}