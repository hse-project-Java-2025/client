package org.hse.smartcalendar.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val SERVER_BASE_URL = "http://10.0.2.2:8080"
    private val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val authApiService: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }

}