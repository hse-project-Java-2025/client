package org.hse.smartcalendar.sample//
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.Body
//import retrofit2.http.Field
//import retrofit2.http.FormUrlEncoded
//import retrofit2.http.POST
//
//data class RegisterResponse(
//    val id: Int,
//    val username: String,
//    val email: String,
//    val password: String,
//    val tasks: Any,
//    val enabled: Boolean,
//    val accountNonExpired: Boolean,
//    val credentialsNonExpired: Boolean,
//    val accountNonLocked: Boolean,
//    val authorities: List<Any>
//)
//data class AuthRequest(
//    val username: String,
//    val email: String,
//    val password: String,
//){}
//data class AuthResponse(
//    val Token: String
//)
//interface ApiService {
//    @FormUrlEncoded
//    @POST("api/auth/signup")
//    suspend fun registerUser(@Field("username") username: String,
//                             @Field("email") email: String,
//                             @Field("password") password: String)
//    :RegisterResponse
//    @POST("api/auth/login")
//    suspend fun loginUser(@Body authRequest: AuthRequest):AuthResponse
//}
//object RetrofitInstance{
//    val api: ApiService by lazy{
//        Retrofit.Builder()
//            .baseUrl("http://localhost:8080")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(ApiService::class.java)
//    }
//}