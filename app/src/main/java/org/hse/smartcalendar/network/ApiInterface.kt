package org.hse.smartcalendar.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path//auto import not work
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import java.util.UUID

interface AuthApiInterface {
    @POST("api/auth/signup")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/auth/login")
    suspend fun loginUser(
        @Body request: LoginRequest,
    ) : Response<ResponseBody>
    @POST("api/auth/change-credentials")
    suspend fun changeCredentials(
        @Body request: ChangeCredentialsRequest,
    ) : Response<ResponseBody>
    @GET("api/users/me")
    suspend fun getUserInfo(): Response<UserInfoResponse>
}
interface TaskApiInterface {
    @GET("api/users/{userId}/events/dailytasks")
    suspend fun getDailyTasks(
        @Path("userId") userId: Long
    ): Response<List<TaskResponse>>

    @POST("api/users/{userId}/events")
    suspend fun addTask(
        @Path("userId") userId: Long,
        @Body request: AddTaskRequest
    ): Response<AddTaskResponse>

    @DELETE("api/users/events/{eventId}")
    suspend fun deleteTask(
        @Path("eventId") eventId: UUID
    ): Response<ResponseBody>

    @POST("api/users/events/{eventId}")
    suspend fun editTask(
        @Path("eventId") eventId: UUID,
        @Body request: AddTaskRequest
    ): Response<ResponseBody>


    @PATCH("api/users/events/{eventId}/status")
    suspend fun changeTaskCompletion(
        @Path("eventId") eventId: UUID,
        @Body request: CompleteStatusRequest
    ): Response<ResponseBody>
}

interface StatisticsApiInterface {

    @GET("api/users/{userId}/statistics")
    suspend fun getUserStatistics(
        @Path("userId") userId: Long
    ): Response<StatisticsDTO>

    @PUT("api/users/{userId}/statistics")
    suspend fun updateUserStatistics(
        @Path("userId") userId: Long,
        @Body request: StatisticsDTO
    ): Response<ResponseBody>

    @GET("api/statistics/total-time-task-types")
    suspend fun getGlobalTaskTypeStatistics(): Response<TotalTime>
}