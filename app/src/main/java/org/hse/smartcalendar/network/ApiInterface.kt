package org.hse.smartcalendar.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path//auto import not work
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiInterface {
    @POST("api/auth/signup")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("api/auth/login")
    suspend fun loginUser(@Body request: LoginRequest): LoginResponse

    @POST("api/auth/change-credentials")
    suspend fun changeCredentials(@Body request: ChangeCredentialsRequest): String

    @GET("api/users/me")
    suspend fun getUserInfo(): UserInfoResponse
}
interface TaskApiInterface {

    @GET("api/users/{userId}/events/dailytasks")
    suspend fun getDailyTasks(
        @Path("userId") userId: Long
    ): List<TaskResponse>

    @POST("api/users/{userId}/tasks")
    suspend fun addTask(
        @Path("userId") userId: Long,
        @Body request: AddTaskRequest
    ): AddTaskResponse

    @DELETE("api/users/tasks/{taskId}")
    suspend fun deleteTask(
        @Path("taskId") taskId: Int
    ): String
}