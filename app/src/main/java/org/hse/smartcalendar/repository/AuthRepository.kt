package org.hse.smartcalendar.repository

import org.hse.smartcalendar.data.User
import org.hse.smartcalendar.network.*

@Suppress("LiftReturnOrAssignment")
class AuthRepository(private val api: AuthApiInterface) {

    suspend fun loginUser(loginRequest: LoginRequest): NetworkResponse<LoginResponse> {
        return try {
            val token = api.loginUser(loginRequest) // Возвращает LoginResponse
            ApiClient.authToken = token.token
            NetworkResponse.Success(token)
        } catch (e: Exception) {
            NetworkResponse.NetworkError(e.message.toString())
        }
    }

    suspend fun registerUser(registerRequest: RegisterRequest): NetworkResponse<LoginResponse> {
        return try {
            api.register(registerRequest) // Возвращает UserInfoResponse или Unit
            loginUser(LoginRequest(registerRequest.username, registerRequest.password))
        } catch (e: Exception) {
            NetworkResponse.NetworkError(e.message.toString())
        }
    }

    suspend fun changeCredentials(request: ChangeCredentialsRequest): NetworkResponse<CredentialsResponse> {
        return try {
            val result = api.changeCredentials(request)
            NetworkResponse.Success(CredentialsResponse(result))
        } catch (e: Exception) {
            NetworkResponse.NetworkError(e.message.toString())
        }
    }

    suspend fun changePassword(username: String, password: String, newPassword1: String, newPassword2: String): NetworkResponse<CredentialsResponse> {
        if (newPassword1 != newPassword2) {
            return NetworkResponse.Error("New passwords are different")
        }
        val request = ChangeCredentialsRequest(username, password, username, newPassword1)
        return changeCredentials(request)
    }

    suspend fun changeLogin(username: String, password: String, newUsername1: String, newUsername2: String): NetworkResponse<CredentialsResponse> {
        if (newUsername1 != newUsername2) {
            return NetworkResponse.Error("New usernames are different")
        }
        val request = ChangeCredentialsRequest(username, password, newUsername1, password)
        return changeCredentials(request)
    }

    suspend fun userInfo(): NetworkResponse<UserInfoResponse> {
        return try {
            val response = api.getUserInfo()
            User.set(response.id, response.username, response.email)
            NetworkResponse.Success(response)
        } catch (e: Exception) {
            NetworkResponse.NetworkError(e.message.toString())
        }
    }
}