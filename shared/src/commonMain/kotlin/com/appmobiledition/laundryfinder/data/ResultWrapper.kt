package com.appmobiledition.laundryfinder.data

import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.utils.io.errors.*

// TODO: Should find a way to implement this for ios directly without using the flattenIos method. Because for now Sealed Class and generic of generic in interface are not 
//  handle in kmm  

typealias ApiCallBlock<T> = suspend () -> T

sealed class ResultWrapper<out T> {
    data class Success<T>(val value: T?) : ResultWrapper<T>()
    data class Failure(val code: Int, val throwable: Throwable) : ResultWrapper<Nothing>()
}

fun <T> ResultWrapper<T>.flattenIos(): T? {
    return when (this) {
        is ResultWrapper.Failure -> null
        is ResultWrapper.Success -> this.value
    }
}

@Throws(IllegalStateException::class)
suspend fun <T> safeApiCall(apiCall: ApiCallBlock<T>): ResultWrapper<T> {
    return try {
        ResultWrapper.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        println(throwable.message)
        println(throwable.cause)
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



