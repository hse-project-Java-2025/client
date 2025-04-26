package org.hse.smartcalendar

import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.hse.smartcalendar.activity.NavigationActivity
import org.hse.smartcalendar.net2024.SignInManager
import org.hse.smartcalendar.net2024.SignUpManager
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.AuthResult
import org.hse.smartcalendar.network.AuthUseCase
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.network.RegisterRequest
import org.hse.smartcalendar.network.RegisterResponse
import org.hse.smartcalendar.sample.Constant
import org.hse.smartcalendar.sample.NetworkResponseSample
import org.hse.smartcalendar.sample.RetrofitInstance
import org.hse.smartcalendar.sample.WeatherModel

class AuthViewModel : ViewModel() {
    var dummy = 0;
    private var authUseCase: AuthUseCase = AuthUseCase(ApiClient.authApiService)
    private val api = ApiClient.authApiService
    private val _registerResult = MutableLiveData<NetworkResponse<RegisterResponse>>()
    val registerResult: LiveData<NetworkResponse<RegisterResponse>> = _registerResult
    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponseSample<WeatherModel>>()
    val weatherResult: LiveData<NetworkResponseSample<WeatherModel>> = _weatherResult
    suspend fun handleAuth(
        authType: AuthType,
        username: String,
        email: String,
        password: String
    ){
        when (val result = authUseCase.execute(authType, username, email, password)) {
            is AuthResult.Success -> {
                signup(username, email, password)
            }
            is AuthResult.Failure -> {}
        }
    }
     fun signup(username: String, email: String, password: String) {
         val manager = SignUpManager(email)
         manager.initData(username,password)
         manager.signUp {
             this.dummy = 1
         }
         val signInManager=SignInManager("kek", "lol")
         signInManager.signIn {
             this.dummy = 2;
         }
        viewModelScope.launch {
            _registerResult.value = NetworkResponse.Loading
            //val weatherResponse = weatherApi.getWeather(Constant.apiKey, "London")
            try {
                //val weatherResponse = weatherApi.getWeather(Constant.apiKey, "London")
                //val basicResponse = api.register(RegisterRequest(username, email, password))
                val response = api.registerUser(username, email, password);
                val responseLogin = api.loginUser(username, password)
                if (response.isSuccessful || responseLogin.isSuccessful){
                    response.body()?.let{
                        _registerResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _registerResult.value = NetworkResponse.Error("Code: "+response.code()+" message "+response.message())
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