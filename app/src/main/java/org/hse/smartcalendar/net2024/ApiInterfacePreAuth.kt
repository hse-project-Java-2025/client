package org.hse.smartcalendar.net2024

import org.hse.smartcalendar.data.User
import org.hse.smartcalendar.network.RegisterResponse

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterfacePreAuth {
    @Headers("Content-Type:application/json")
    @POST("/auth/sign-up")
    fun registerUser(@Body info: SignUpManager.UserSignUpBody):
            Call<RegisterResponse>

    @POST("/auth/sign-in")
    fun loginUser(@Body info: SignInManager.UserSignInBody):
            Call<String>
    @Headers("Content-Type:application/json")
    @GET("/getTags")
    fun getTags(): Call<ArrayList<String>>

    @Headers("Content-Type:application/json")
    @GET("/users/getUser/{userId}")
    fun getUser(@Header("Authorization") token: String, @Path("userId") userId: Int): Call<User>
}
