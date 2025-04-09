package org.hse.smartcalendar.network

data class RegisterResponse (
    val id: Long = 0,
    val username: String = "",
    val email: String  = "",
    val password: String = "",
    val tasks: Any? = null,
    val enabled: Boolean = true,
    val authorities: List<Any?> = emptyList(),
    val accountNonExpired: Boolean = true,
    val credentialsNonExpired: Boolean = true,
    val accountNonLocked: Boolean = true
)
data class LoginResponse(
    val token: Any
)