package org.hse.smartcalendar.network

data class AuthRequest(
    val name: String,
    val password: String
)
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
sealed class AuthResult {
    data class Success(val username: String, val password: String) : AuthResult()
    data class Failure(val code: Int, val message: String? = null) : AuthResult()
}