package org.hse.smartcalendar.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @POST("api/auth/signup")
    suspend fun registerUser(
        @Query("username") username: String,
        @Query("email") email: String,
        @Query("password") password: String,
    ) : Response<RegisterResponse>
    @POST("api/auth/signup")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("api/auth/login")
    suspend fun loginUser(
        @Query("username") username: String,
        @Query("password") password: String,
    ) : Response<String>
}