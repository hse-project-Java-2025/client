package org.hse.smartcalendar.view.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
//import dagger.hilt.android.lifecycle.HiltViewModel
//import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.hse.smartcalendar.notification.ReminderViewModel

class SettingsViewModel  constructor(
) : ViewModel() {
    private val _isReminders: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var isReminders = _isReminders.asStateFlow()
    fun switchReminders(){
        _isReminders.value = _isReminders.value.not()
    }
    //SettingsVM 46d3d
}