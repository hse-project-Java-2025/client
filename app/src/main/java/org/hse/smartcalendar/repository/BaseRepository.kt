package org.hse.smartcalendar.repository

import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.User
import org.hse.smartcalendar.network.NetworkResponse
import retrofit2.Response
import java.util.UUID

open class BaseRepository {
    suspend fun<T> withIdRequest( supplier:suspend (Long)->Response<T>): NetworkResponse<T> {
        try {
            val id = User.id
            if (id==null){
                return NetworkResponse.errorId()
            }
            return withSupplierRequest { supplier.invoke(id) }
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
    suspend fun<T> withSupplierRequest(supplier:suspend ()->Response<T>): NetworkResponse<T> {
        try {
            val response = supplier.invoke()
            if (response.isSuccessful){
                val responseBody = response.body()
                responseBody?.let{
                        return NetworkResponse.Success(responseBody)}
                return NetworkResponse.fromResponse(response)
            } else {
                return NetworkResponse.fromResponse(response)
            }
        } catch (e: Exception) {
            return NetworkResponse.NetworkError(e.message.toString())
        }
    }
}