package com.example.camperpro.data

import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.utils.io.errors.*

typealias ApiCallBlock<T> = suspend () -> T

//covar != Swift
sealed class ResultWrapper<out T> {
    data class Success<T>(val value: T?) : ResultWrapper<T>()
    data class Failure(val code: Int, val throwable: Throwable) : ResultWrapper<Nothing>()
}


@Throws(IllegalStateException::class)
suspend fun <T> safeApiCall(apiCall: ApiCallBlock<T>): ResultWrapper<T> {
    return try {
        ResultWrapper.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        when (throwable) {
            is IOException -> ResultWrapper.Failure(-1, throwable)
            is ClientRequestException -> {
                val code = throwable.response.status.value
                ResultWrapper.Failure(code, throwable)
            }
            else -> ResultWrapper.Failure(0, throwable)
        }
    }
}

