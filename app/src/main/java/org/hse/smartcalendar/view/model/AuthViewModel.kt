package org.hse.smartcalendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.CredentialsResponse
import org.hse.smartcalendar.network.LoginRequest
import org.hse.smartcalendar.network.LoginResponse
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.network.RegisterRequest
import org.hse.smartcalendar.repository.AuthRepository

class AuthViewModel : ViewModel() {
    private val api = ApiClient.authApiService
    private val authRepository: AuthRepository = AuthRepository(api)
    private val _loginResult = MutableLiveData<NetworkResponse<LoginResponse>>()
    val loginResult: LiveData<NetworkResponse<LoginResponse>> = _loginResult

    val _changeCredentialsResult = MutableLiveData<NetworkResponse<CredentialsResponse>>()
    val changeCredentialsResult = _changeCredentialsResult
    fun signup(username: String, email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = NetworkResponse.Loading
            _loginResult.value = authRepository.registerUser(RegisterRequest(username, email, password))
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = NetworkResponse.Loading
            _loginResult.value = authRepository.loginUser(LoginRequest(username, password))
        }
    }
    fun changePassword(username: String, password: String, newPassword1: String, newPassword2: String) {
        viewModelScope.launch {
            _changeCredentialsResult.value = NetworkResponse.Loading
            _changeCredentialsResult.value =
                authRepository.changePassword(username, password, newPassword1, newPassword2)
        }
    }

    fun changeLogin(username: String, password: String, newUsername1: String, newUsername2: String) {
        viewModelScope.launch {
            _changeCredentialsResult.value = NetworkResponse.Loading
            _changeCredentialsResult.value =
                authRepository.changeLogin(username, password, newUsername1, newUsername2)
        }
    }
}