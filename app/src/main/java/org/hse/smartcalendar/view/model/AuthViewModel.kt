package org.hse.smartcalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    sealed class AuthState {
        data object Idle : AuthState()
        data object Loading : AuthState()
        data class Success(val token: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                // По-хорошему, тут должна быть сама авторизация.
                Thread.sleep(1000)
                if (username == "user" && password == "password") {
                    _authState.value = AuthState.Success("fake_token")
                } else {
                    _authState.value = AuthState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
            _authState.value = AuthState.Success("0")
        }
    }

    fun changePassword(username: String, password: String, newPassword1: String, newPassword2: String) {
        if (newPassword1!=newPassword2){
            _authState.value = AuthState.Error("New passwords are different")
        }
        //авторизация

    }

    fun changeLogin(username: String, password: String, newPassword1: String, newPassword2: String) {
        if (newPassword1!=newPassword2){
            _authState.value = AuthState.Error("New passwords are different")
        }
        //авторизация
    }
}