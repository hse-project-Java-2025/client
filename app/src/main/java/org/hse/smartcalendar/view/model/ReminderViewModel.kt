package org.hse.smartcalendar.view.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.utility.prettyPrint
import org.hse.smartcalendar.utility.toEpochMilliseconds
import org.hse.smartcalendar.work.ReminderWorker
import java.util.concurrent.TimeUnit
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class ReminderViewModel(application: Application): ViewModel() {
    private val _isReminders: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _minutesDelay: MutableStateFlow<Int> = MutableStateFlow(10)
    var minutesDelay = _minutesDelay.asStateFlow()
    var isReminders = _isReminders.asStateFlow()
    fun switchReminders(){
        _isReminders.value = _isReminders.value.not()
    }
    fun setDelay(delay: Int){
        if (delay>=0) {
            _minutesDelay.value = delay
        }
    }
    private val workManager = WorkManager.getInstance(application)

    internal fun scheduleReminder(
        task: DailyTask): Boolean {
        if (!isReminders.value){
            return false
        }
        val myWorkRequestBuilder = OneTimeWorkRequestBuilder<ReminderWorker>()
        val millisecondsReminder = LocalDate.toEpochMilliseconds(task.getTaskDate()) +
                task.getDailyTaskStartTime().toMillisecondOfDay()
        val millisecondsNow = Clock.System.now().toEpochMilliseconds()
        val millisecondsDelay = millisecondsReminder-millisecondsNow
        if (millisecondsDelay<0){
            return false
        }
        var realMinutesBefore = minutesDelay.value//= minutesBefore
        if (minutesDelay.value.toDuration(DurationUnit.MINUTES).inWholeMilliseconds>millisecondsDelay){
            realMinutesBefore = millisecondsDelay.toDuration(DurationUnit.MILLISECONDS).inWholeMinutes.toInt()
            myWorkRequestBuilder.setInitialDelay(0, TimeUnit.MILLISECONDS)
        }else {
            myWorkRequestBuilder.setInitialDelay(
                millisecondsDelay - realMinutesBefore * 60 * 1000,
                TimeUnit.MILLISECONDS
            )
        }
        myWorkRequestBuilder.setInputData(
            workDataOf(
                ReminderWorker.Companion.TYPE_KEY to task.getDailyTaskType().toString().lowercase(),
                ReminderWorker.Companion.BEFORE_KEY to realMinutesBefore,
                ReminderWorker.Companion.TITLE_KEY to task.getDailyTaskTitle(),
                ReminderWorker.Companion.MESSAGE_KEY to task.getDailyTaskDescription(),
                ReminderWorker.Companion.START_KEY to LocalTime.prettyPrint(task.getDailyTaskStartTime()),
                ReminderWorker.Companion.END_KEY to LocalTime.prettyPrint(task.getDailyTaskEndTime()),
            )
        )
        workManager.enqueueUniqueWork(
            "reminder${task.getId()}",
            ExistingWorkPolicy.REPLACE,
            myWorkRequestBuilder.build())
        return true
    }
    internal fun cancelReminder(task: DailyTask) {
        val workName = "reminder${task.getId()}"
        workManager.cancelUniqueWork(workName)
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