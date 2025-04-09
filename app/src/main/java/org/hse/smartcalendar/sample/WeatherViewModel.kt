package org.hse.smartcalendar.sample

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponseSample<WeatherModel>>()
    val weatherResult: LiveData<NetworkResponseSample<WeatherModel>> = _weatherResult
    fun getData(city: String){
        viewModelScope.launch {
            _weatherResult.value = NetworkResponseSample.Loading
            val response = weatherApi.getWeather(Constant.apiKey, city)
            try {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponseSample.Success(it)
                    }
                    Log.i("Response: ", response.body().toString())
                } else {
                    _weatherResult.value = NetworkResponseSample.Error("Unable to get data "+response.message())
                }
            } catch (e: Exception){
                _weatherResult.value = NetworkResponseSample.Error("Caught exception: "+e.message)
            }
        }
    }
}