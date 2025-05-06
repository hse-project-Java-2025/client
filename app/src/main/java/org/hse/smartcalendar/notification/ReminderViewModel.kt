package org.hse.smartcalendar.notification

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.activity.BaseApplication
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.utility.prettyPrint
import org.hse.smartcalendar.utility.toEpochMilliseconds
import org.hse.smartcalendar.view.model.SettingsViewModel
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
        task: DailyTask,
        minutesBefore: Int,
        settingsModel: SettingsViewModel
    ): Boolean {
        //ReminderVm ff62295
        //разные ссылки на VM
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
        if (minutesBefore.toDuration(DurationUnit.MINUTES).inWholeMilliseconds>millisecondsDelay){
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
                ReminderWorker.TYPE_KEY to task.getDailyTaskType().toString().lowercase(),
                ReminderWorker.BEFORE_KEY to realMinutesBefore,
                ReminderWorker.TITLE_KEY to task.getDailyTaskTitle(),
                ReminderWorker.MESSAGE_KEY to task.getDailyTaskDescription(),
                ReminderWorker.START_KEY to LocalTime.prettyPrint(task.getDailyTaskStartTime()),
                ReminderWorker.END_KEY to LocalTime.prettyPrint(task.getDailyTaskEndTime()),
            )
        )
        workManager.enqueue(myWorkRequestBuilder.build())
        return true
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