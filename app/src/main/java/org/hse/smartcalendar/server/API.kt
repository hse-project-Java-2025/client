import android.credentials.GetCredentialResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class Response(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val tasks: Any,
    val enabled: Boolean,
    val accountNonExpired: Boolean,
    val credentialsNonExpired: Boolean,
    val accountNonLocked: Boolean,
    val authorities: List<Any>
){}
data class AuthRequest(
    val username: String,
    val email: String,
    val password: String,
){}
interface ApiService {
    @POST("api/auth/signup")
    suspend fun authUser(@Body authRequest: AuthRequest):Response
}
object RetrofitInstance{
    val api: ApiService by lazy{
        Retrofit.Builder()
            .baseUrl("http://localhost:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}