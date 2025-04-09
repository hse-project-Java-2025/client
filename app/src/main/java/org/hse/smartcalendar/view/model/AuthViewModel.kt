package org.hse.smartcalendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.network.RegisterResponse
import org.hse.smartcalendar.sample.Constant
import org.hse.smartcalendar.sample.NetworkResponseSample
import org.hse.smartcalendar.sample.RetrofitInstance
import org.hse.smartcalendar.sample.WeatherModel

class AuthViewModel : ViewModel() {
    private val api = ApiClient.api
    private val _registerResult = MutableLiveData<NetworkResponse<RegisterResponse>>()
    val registerResult: LiveData<NetworkResponse<RegisterResponse>> = _registerResult

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponseSample<WeatherModel>>()
    val weatherResult: LiveData<NetworkResponseSample<WeatherModel>> = _weatherResult

    fun signup(username: String, email: String, password: String) {
        viewModelScope.launch {
            _registerResult.value = NetworkResponse.Loading
            val weatherResponse = weatherApi.getWeather(Constant.apiKey, "London")
            try {
                val response = api.registerUser(username, email, password)
                if (response.isSuccessful){
                    response.body()?.let{
                        _registerResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _registerResult.value = NetworkResponse.Error(response.message())
                }
            } catch (e: Exception) {
                _registerResult.value = NetworkResponse.Error(e.message ?: "exception")
            }
        }
    }

    fun changePassword(username: String, password: String, newPassword1: String, newPassword2: String) {
        if (newPassword1!=newPassword2){
            _registerResult.value = NetworkResponse.Error("New passwords are different")
        }
        //авторизация

    }

    fun changeLogin(username: String, password: String, newPassword1: String, newPassword2: String) {
        if (newPassword1!=newPassword2){
            _registerResult.value = NetworkResponse.Error("New passwords are different")
        }
        //авторизация
    }

    fun login(username: String, password: String) {

    }
}