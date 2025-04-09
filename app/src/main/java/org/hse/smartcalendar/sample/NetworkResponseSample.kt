package org.hse.smartcalendar.sample

sealed class NetworkResponseSample<out T> {
    data class Success <out T>(val data: T): NetworkResponseSample<T>()
    data class Error(val message: String) : NetworkResponseSample<Nothing>()
    data object Loading: NetworkResponseSample<Nothing>()
}