package org.hse.smartcalendar.view.model

//import dagger.hilt.android.lifecycle.HiltViewModel
//import jakarta.inject.Inject
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel  constructor(
) : ViewModel() {
    private val _isReminders: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var isReminders = _isReminders.asStateFlow()
    fun switchReminders(){
        _isReminders.value = _isReminders.value.not()
    }
    //SettingsVM 46d3d
}