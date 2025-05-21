package org.hse.smartcalendar.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("api/auth/signup")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/auth/login")
    suspend fun loginUser(
        @Body request: AuthRequest,
    ) : Response<ResponseBody>
    @POST("api/auth/change-credentials")
    suspend fun changeCredentials(
        @Body request: ChangeCredentialsRequest,
    ) : Response<ResponseBody>
}