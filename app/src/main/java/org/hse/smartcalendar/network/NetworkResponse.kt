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
        fun errorId(): Error{
            return Error("id is null")
        }
        fun errorNullResponse(): Error{
            return Error("token is null")
        }
        fun mapFailtureToAny(): NetworkResponse<Any> = when(this){
            is Error -> Error(this.message)
            is NetworkError -> NetworkError(this.exceptionMessage)
            is Loading->Loading
            else -> throw Exception("Unexpected return type")
        }
    }
}