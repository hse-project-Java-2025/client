package org.hse.smartcalendar.utility

import androidx.compose.runtime.MutableState
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.view.model.ListViewModel

fun editHandler(
    oldTask: DailyTask,
    newTask: DailyTask,
    viewModel: ListViewModel,
    isEmptyTitle: MutableState<Boolean>,
    isConflictInTimeField: MutableState<Boolean>,
    isNestedTask: MutableState<Boolean>,
    statsUpdateOldToNewTask: (DailyTask, DailyTask)-> Unit,
    reminderUpdate: (DailyTask)->Unit
): Boolean {
    isEmptyTitle.value = false
    isNestedTask.value = false
    isConflictInTimeField.value = false

    isEmptyTitle.value = newTask.getDailyTaskTitle().isEmpty()
    isConflictInTimeField.value = newTask.getDailyTaskStartTime() > newTask.getDailyTaskEndTime()
    isNestedTask.value = !viewModel.isUpdatable(oldTask, newTask)
    newTask.setId(oldTask.getId())
    if (!isEmptyTitle.value && !isConflictInTimeField.value && !isNestedTask.value) {
        oldTask.updateDailyTask(newTask)
        statsUpdateOldToNewTask(oldTask, newTask)
        reminderUpdate(newTask)
        return true
    }
    return false
}
