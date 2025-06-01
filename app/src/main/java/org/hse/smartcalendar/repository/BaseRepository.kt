package org.hse.smartcalendar.repository

import org.hse.smartcalendar.data.User
import org.hse.smartcalendar.network.NetworkResponse
import retrofit2.Response

open class BaseRepository {
    suspend fun<T> withIdRequest( supplier:suspend (Long)->Response<T>): NetworkResponse<T> {
        try {
            val id = User.id
            if (id==null){
                return NetworkResponse.errorId()
            }
            val response = supplier.invoke(id)
            if (response.isSuccessful){
                val responseBody = response.body()
                if (response.code()==200){
                    responseBody?.let{
                        return NetworkResponse.Success(responseBody)
                    }
                }
                return NetworkResponse.fromResponse(response)
            } else {
                return NetworkResponse.fromResponse(response)
            }
        } catch (e: Exception) {
            return NetworkResponse.NetworkError(e.message.toString())
        }
    }
}