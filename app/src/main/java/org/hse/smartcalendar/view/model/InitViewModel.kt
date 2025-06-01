package org.hse.smartcalendar.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.network.UserInfoResponse
import org.hse.smartcalendar.repository.AuthRepository
import org.hse.smartcalendar.repository.TaskRepository

class InitViewModel:ViewModel() {
    private val authRepository: AuthRepository = AuthRepository(ApiClient.authApiService)
    private val taskRepository: TaskRepository = TaskRepository(ApiClient.taskApiService)
    var _userInfoResult = MutableLiveData<NetworkResponse<UserInfoResponse>>()
    val userInfoResult = _userInfoResult
    var _initResult = MutableLiveData<NetworkResponse<Any>>()
    val initResult:LiveData<NetworkResponse<Any>> = _initResult
    fun initUser(){
        viewModelScope.launch {
            _initResult.value = NetworkResponse.Loading
            userInfoResult.value = NetworkResponse.Loading
            userInfoResult.value =
                authRepository.userInfo()
            if (userInfoResult.value !is NetworkResponse.Success){
                _initResult.value = userInfoResult.value?.mapFailureToAny()
                return@launch
            }
            _initResult.value = taskRepository.initUserTasks()
            if (initResult.value !is NetworkResponse.Success){
                return@launch
            }
            //statistics&others
        }
    }
    fun initUserTasks(){
        viewModelScope.launch {
            _initResult.value = NetworkResponse.Loading
            _initResult.value = taskRepository.initUserTasks()
        }
    }
}