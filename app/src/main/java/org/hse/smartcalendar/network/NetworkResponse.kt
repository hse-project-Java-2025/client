package org.hse.smartcalendar.network
import retrofit2.Response
sealed class NetworkResponse<out T> {
    data class Success <out T>(val data: T): NetworkResponse<T>()
    data class Error(val message: String) : NetworkResponse<Nothing>()
    data class NetworkError(val exceptionMessage: String) : NetworkResponse<Nothing>()
    data object Loading: NetworkResponse<Nothing>()
    companion object{
        fun fromResponse(response: Response<*>): NetworkResponse.Error{
            return Error("Server response " + response.message() + "code " + response.code()
                .toString())
        }
    }
}