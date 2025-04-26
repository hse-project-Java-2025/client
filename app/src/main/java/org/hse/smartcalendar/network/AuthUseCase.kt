package org.hse.smartcalendar.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.hse.smartcalendar.AuthType

class AuthUseCase(private val ApiInterface: ApiInterface) {
    suspend fun execute(
        authType: AuthType,
        name: String,
        email: String,
        password: String,
    ): AuthResult = withContext(Dispatchers.IO) {
        try {
            val authRequest = AuthRequest(name, password)
            val loginRequest = RegisterRequest(name, email, password)
            val response = when (authType) {
                //AuthType.Login -> ApiInterface.loginUser(authRequest)
                AuthType.Register -> ApiInterface.register(loginRequest)
                else -> throw IllegalArgumentException()
            }

            if (response.username != null && response.password != null) {
                AuthResult.Success(response.username, response.password)
            } else {
                AuthResult.Failure(500, "Unknown error")
            }
        } catch (e: Exception) {
            AuthResult.Failure(
                code = 0, // Custom code for network/unknown errors
                message = e.message ?: "Network request failed"
            )
        }
    }
}
