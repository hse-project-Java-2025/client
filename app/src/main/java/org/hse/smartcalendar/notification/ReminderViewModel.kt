package org.hse.smartcalendar.notification

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.utility.prettyPrint
import org.hse.smartcalendar.utility.toEpochMilliseconds
import java.util.concurrent.TimeUnit

class ReminderViewModel(application: Application): ViewModel() {

    private val workManager = WorkManager.getInstance(application)

    internal fun scheduleReminder(
        task: DailyTask,
        minutesBefore: Int,
    ) {
        val myWorkRequestBuilder = OneTimeWorkRequestBuilder<ReminderWorker>()
        myWorkRequestBuilder.setInputData(
            workDataOf(
                ReminderWorker.TYPE_KEY to task.getDailyTaskType().toString().lowercase(),
                ReminderWorker.BEFORE_KEY to minutesBefore,
                ReminderWorker.TITLE_KEY to task.getDailyTaskTitle(),
                ReminderWorker.MESSAGE_KEY to task.getDailyTaskDescription(),
                ReminderWorker.START_KEY to LocalTime.prettyPrint(task.getDailyTaskStartTime()),
                ReminderWorker.END_KEY to LocalTime.prettyPrint(task.getDailyTaskEndTime()),
            )
        )
        val millisecondsReminder = LocalDate.toEpochMilliseconds(task.getTaskDate()) +
                task.getDailyTaskStartTime().toMillisecondOfDay()- minutesBefore*60*1000
        val millisecondsNow = Clock.System.now().toEpochMilliseconds()
        val millisecondsDelay = millisecondsReminder-millisecondsNow
        myWorkRequestBuilder.setInitialDelay(millisecondsDelay, TimeUnit.MILLISECONDS)
        workManager.enqueue(myWorkRequestBuilder.build())
    }
}

class ReminderViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ReminderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            ReminderViewModel(application) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}