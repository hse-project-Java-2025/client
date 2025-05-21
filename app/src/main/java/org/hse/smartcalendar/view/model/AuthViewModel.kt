package org.hse.smartcalendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.AuthRequest
import org.hse.smartcalendar.network.ChangeCredentialsRequest
import org.hse.smartcalendar.network.CredentialsResponse
import org.hse.smartcalendar.network.LoginResponse
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.network.RegisterRequest

class AuthViewModel : ViewModel() {
    private val api = ApiClient.authApiService
    private val _loginResult = MutableLiveData<NetworkResponse<LoginResponse>>()
    val loginResult: LiveData<NetworkResponse<LoginResponse>> = _loginResult

    val _changeCredentialsResult = MutableLiveData<NetworkResponse<CredentialsResponse>>()
    val changeCredentialsResult = _changeCredentialsResult
     fun signup(username: String, email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = NetworkResponse.Loading
            try {
                val registerResponse = api.register(RegisterRequest(username, email, password));
                if (!registerResponse.isSuccessful){
                    _loginResult.value = NetworkResponse.Error("register failed with Code: "+registerResponse.code())
                    return@launch
                }
                login(username, password)
            } catch (e: Exception) {
                _loginResult.value = NetworkResponse.Error(e.message ?: "exception")
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = NetworkResponse.Loading
            try {
                val responseLogin = api.loginUser(AuthRequest(username, password))
                if (responseLogin.isSuccessful){
                    val token = responseLogin.body()?.string()
                    if (token == null){
                        _loginResult.value = NetworkResponse.Error("token is null")
                    } else {
                        ApiClient.authToken = token
                        _loginResult.value = NetworkResponse.Success(LoginResponse(token))
                    }
                } else {
                    _loginResult.value = NetworkResponse.Error("Code: "+responseLogin.code())
                }
            } catch (e: Exception) {
                _loginResult.value = NetworkResponse.Error(e.message ?: "exception")
            }
        }
    }

    fun changeCredentials(credentialsRequest: ChangeCredentialsRequest){
        viewModelScope.launch {
            _changeCredentialsResult.value = NetworkResponse.Loading
            try {
                val responseCredentials = api.changeCredentials(credentialsRequest)
                if (responseCredentials.isSuccessful){
                    val response = responseCredentials.body()?.string()
                    if (response == null){
                        _changeCredentialsResult.value = NetworkResponse.Error("response is null")
                    } else {
                        _changeCredentialsResult.value = NetworkResponse.Success(CredentialsResponse(response))
                    }
                } else {
                    _changeCredentialsResult.value = NetworkResponse.Error("Code: "+responseCredentials.code())
                }
            } catch (e: Exception) {
                _changeCredentialsResult.value = NetworkResponse.Error(e.message ?: "exception")
            }
        }
    }
    fun changePassword(username: String, password: String, newPassword1: String, newPassword2: String) {
        if (newPassword1!=newPassword2){
            _changeCredentialsResult.value = NetworkResponse.Error("New passwords are different")
            return
        }
        val request = ChangeCredentialsRequest(username, password, username, newPassword1)
        changeCredentials(request)

    }

    fun changeLogin(username: String, password: String, newUsername1: String, newUsername2: String) {
        if (newUsername1!=newUsername2){
            changeCredentialsResult.value = NetworkResponse.Error("New passwords are different")
        }
        val request = ChangeCredentialsRequest(username, password, newUsername1, password)
        changeCredentials(request)
    }
}