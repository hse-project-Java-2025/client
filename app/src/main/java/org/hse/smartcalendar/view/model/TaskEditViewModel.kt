package org.hse.smartcalendar.view.model

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskAction
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.data.WorkManagerHolder
import org.hse.smartcalendar.utility.editHandler

class TaskEditViewModel(
    val listViewModel: ListViewModel
) : ViewModel() {
    private val workManager = WorkManagerHolder.getInstance()
    private var task: DailyTask = DailyTask(
        title = "Preview title",
        description = "Preview description",
        start = LocalTime(0, 0),
        end = LocalTime(23, 59),
        date = DailyTask.defaultDate
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
        reminderViewModel: ReminderViewModel
    ): Boolean {
        changes.setDate(listViewModel.getScheduleDate())
        return editHandler(
            oldTask = task,
            newTask = changes,
            viewModel = listViewModel,
            isEmptyTitle = isEmptyTitle,
            isConflictInTimeField = isConflictInTimeField,
            isNestedTask = isNestedTask,
            statsUpdateOldToNewTask = { oldTask, newTask->
                    listViewModel.statisticsManager.updateDailyTask(oldTask=oldTask, newTask = newTask)
                listViewModel.scheduleTaskRequest(newTask, DailyTaskAction.Type.EDIT)
            },
            reminderUpdate = {task ->reminderViewModel.scheduleReminder(task)}
        )
    }
}