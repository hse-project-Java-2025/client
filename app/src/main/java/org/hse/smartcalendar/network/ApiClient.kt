package org.hse.smartcalendar.network

import DailyTaskTypeAdapter
import InstantTimeAdapter
import LocalDateAdapter
import LocalDateTimeAdapter
import LocalTimeAdapter
import OffsetDateTimeAdapter
import com.google.gson.GsonBuilder
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.hse.smartcalendar.data.DailyTaskType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.OffsetDateTime

object ApiClient {
    private const val SERVER_BASE_URL = "http://10.0.2.2:8080/"
    var authToken: String? = null
    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor { authToken })
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()
    val gson = GsonBuilder()
        .registerTypeAdapter(kotlinx.datetime.LocalDate::class.java, LocalDateAdapter())
        .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
        .registerTypeAdapter(kotlinx.datetime.LocalDateTime::class.java, LocalDateTimeAdapter())
        .registerTypeAdapter(DailyTaskType::class.java, DailyTaskTypeAdapter())
        .registerTypeAdapter(OffsetDateTime::class.java, OffsetDateTimeAdapter())
        .registerTypeAdapter(Instant::class.java, InstantTimeAdapter())
        .create()
    private val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    val authApiService: AuthApiInterface by lazy {
        retrofit.create(AuthApiInterface::class.java)
    }
    val taskApiService: TaskApiInterface by lazy {
        retrofit.create(TaskApiInterface::class.java)
    }
    val statisticsApiService: StatisticsApiInterface by lazy {
        retrofit.create(StatisticsApiInterface::class.java)
    }
    val audioApiService : AudioApiInterface by lazy {
        retrofit.create(AudioApiInterface::class.java)
    }
    val inviteApiService: InviteApiInterface by lazy {
        retrofit.create(InviteApiInterface::class.java)
    }
}
class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider()
        val requestBuilder = chain.request().newBuilder()
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}