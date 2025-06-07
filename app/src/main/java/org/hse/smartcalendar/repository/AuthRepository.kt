package org.hse.smartcalendar.repository

import org.hse.smartcalendar.data.User
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.AuthApiInterface
import org.hse.smartcalendar.network.ChangeCredentialsRequest
import org.hse.smartcalendar.network.CredentialsResponse
import org.hse.smartcalendar.network.LoginRequest
import org.hse.smartcalendar.network.LoginResponse
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.network.RegisterRequest
import org.hse.smartcalendar.network.UserInfoResponse

//Repository - логика работы с задачами, получает данные,
// формирует и отправляет запрос, возвращает данные/exception
@Suppress("LiftReturnOrAssignment")
class AuthRepository(private val api: AuthApiInterface) {
    suspend fun loginUser(loginRequest: LoginRequest): NetworkResponse<LoginResponse> {
        try {
            val responseLogin = api.loginUser(loginRequest)
            if (responseLogin.isSuccessful) {
                val token = responseLogin.body()?.string()
                if (token == null) {
                    return NetworkResponse.Error("token is null")
                } else {
                    ApiClient.authToken = token
                    return NetworkResponse<LoginResponse>.Success(LoginResponse(token))
                }
            }
            return NetworkResponse.fromResponse(responseLogin)
        } catch (e: Exception) {
            return NetworkResponse<LoginResponse>.NetworkError(e.message.toString())
        }
    }
    suspend fun registerUser(registerRequest: RegisterRequest): NetworkResponse<LoginResponse>{
        try {
            val registerResponse = api.register(registerRequest);
            if (!registerResponse.isSuccessful){
                return NetworkResponse.fromResponse(registerResponse)
            }
            return loginUser(LoginRequest(registerRequest.username, registerRequest.password))
        } catch (e: Exception) {
            return NetworkResponse<LoginResponse>.NetworkError(e.message.toString())
        }
    }
    suspend fun changeCredentials(credentialsRequest: ChangeCredentialsRequest): NetworkResponse<CredentialsResponse> {
        try {
            val responseCredentials = api.changeCredentials(credentialsRequest)
            if (responseCredentials.isSuccessful){
                val response = responseCredentials.body()?.string()
                if (response == null){
                    return NetworkResponse.Error("response is null")
                } else {
                    return NetworkResponse.Success(CredentialsResponse(response))
                }
            } else {
                return NetworkResponse.fromResponse(responseCredentials)
            }
        } catch (e: Exception) {
            return NetworkResponse.NetworkError(e.message.toString())
        }
    }

    suspend fun changePassword(username: String, password: String, newPassword1: String, newPassword2: String): NetworkResponse<CredentialsResponse> {
        if (newPassword1!=newPassword2){
            return NetworkResponse.Error("New passwords are different")
        }
        val request = ChangeCredentialsRequest(username, password, username, newPassword1)
        return changeCredentials(request)
    }
    suspend fun changeLogin(username: String, password: String, newUsername1: String, newUsername2: String): NetworkResponse<CredentialsResponse> {
        if (newUsername1!=newUsername2){
            return NetworkResponse.Error("New passwords are different")
        }
        val request = ChangeCredentialsRequest(username, password, newUsername1, password)
        return changeCredentials(request)
    }
    suspend fun userInfo(): NetworkResponse<UserInfoResponse> {
        try {
            val responseUserInfo = api.getUserInfo()
            if (responseUserInfo.isSuccessful){
                val responseBody = responseUserInfo.body()
                if (responseBody!=null) {
                    User.set(responseBody.id, responseBody.username, responseBody.email)
                    return NetworkResponse.Success(responseBody)
                } else {
                    return NetworkResponse.Error("response is null")
                }
            } else {
                return NetworkResponse.fromResponse(responseUserInfo)
            }
        } catch (e: Exception) {
            return NetworkResponse.NetworkError(e.message.toString())
        }
    }
}