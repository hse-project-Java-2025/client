package org.hse.smartcalendar.network

data class RegisterResponse (
    val id: Long? = null,
    val username: String? = null,
    val email: String?  = null,
    val password: String? = null,
    val tasks: List<Any> = emptyList(),
    val enabled: Boolean? = true,
    val authorities: List<Any>? = emptyList(),
    val accountNonExpired: Boolean? = true,
    val credentialsNonExpired: Boolean? = true
)
data class LoginResponse(
    val token: String
)