package org.hse.smartcalendar.network

data class LoginRequest(
    val username: String,
    val password: String
)
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)
data class ChangeCredentialsRequest(
    val currentUsername: String,
    val currentPassword: String,
    val newUsername: String,
    val newPassword: String
)
sealed class AuthResult {
    data class Success(val username: String, val password: String) : AuthResult()
    data class Failure(val code: Int, val message: String? = null) : AuthResult()
}
sealed class TaskAddRequest(
)