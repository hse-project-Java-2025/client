package org.hse.smartcalendar.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val SERVER_BASE_URL = "http://localhost:8080/"
    private fun getInstance():ApiInterface{
        return Retrofit.Builder()
            .baseUrl(SERVER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
    val api = getInstance()

}