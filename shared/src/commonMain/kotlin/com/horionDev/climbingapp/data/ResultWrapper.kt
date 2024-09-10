package com.horionDev.climbingapp.data

import com.horionDev.climbingapp.data.model.ErrorMessage
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.utils.io.errors.*
import kotlinx.serialization.SerializationException

sealed class ResultWrapper<out T, out E> {
    data class Success<T>(val value: T, val comment: String? = null) : ResultWrapper<T, Nothing>()
    sealed class Failure<E> : ResultWrapper<Nothing, E>() {
        data class HttpError<E>(val code: Int, val errorBody: E?) : Failure<E>()
        data class SerializationError(val message: ErrorMessage) : Failure<Nothing>()
        data class UnknownError(val message: String) : Failure<Nothing>()
        object NetworkError : Failure<Nothing>()
    }
}

suspend inline fun <reified T, reified E> HttpClient.safeGet(block: HttpRequestBuilder.() -> Unit): ResultWrapper<T, E> {
    return try {
        val response = get { block() }
        //        if (response.status.isSuccess()) {
        //            ResultWrapper.Success(response.body(), response.headers["Comment"]!!)
        ResultWrapper.Success(response.body())
        //        } else {
        //            val code = response.status.value
        //            ResultWrapper.Failure.HttpError(code, response.body())
        //        }
    } catch (e: ClientRequestException) {
        val code = e.response.status.value
        ResultWrapper.Failure.HttpError(code, e.errorBody())
    } catch (e: SerializationException) {
        ResultWrapper.Failure.SerializationError(ErrorMessage(e.message.toString()))
    } catch (e: IOException) {
        ResultWrapper.Failure.NetworkError
    } catch (e: JsonConvertException) {
        ResultWrapper.Failure.SerializationError(
            ErrorMessage(
                e.cause.toString()
                    .substringAfter("\"message\":\"")
                    .substringBefore("\"}")
            )
        )
    }
}

suspend inline fun <reified T, reified E> HttpClient.safeDelete(block: HttpRequestBuilder.() -> Unit): ResultWrapper<T, E> {
    return try {
        val response = delete { block() }
        if (response.status.isSuccess()) {
            ResultWrapper.Success(response.body())
        } else {
            val code = response.status.value
            ResultWrapper.Failure.HttpError(code, response.body())
        }
    } catch (e: ClientRequestException) {
        val code = e.response.status.value
        ResultWrapper.Failure.HttpError(code, e.errorBody())
    } catch (e: SerializationException) {
        ResultWrapper.Failure.SerializationError(ErrorMessage(e.message.toString()))
    } catch (e: IOException) {
        ResultWrapper.Failure.NetworkError
    } catch (e: JsonConvertException) {
        ResultWrapper.Failure.SerializationError(ErrorMessage(e.message.toString()))
    }
}

suspend inline fun <reified T, reified E> HttpClient.safePost(block: HttpRequestBuilder.() -> Unit): ResultWrapper<T, E> {
    return try {
        val response = post { block() }
        if (response.status.isSuccess()) {
            ResultWrapper.Success(response.body())
        } else {
            val code = response.status.value
            ResultWrapper.Failure.HttpError(code, response.body())
        }
    } catch (e: ClientRequestException) {
        val code = e.response.status.value
        ResultWrapper.Failure.HttpError(code, e.errorBody())
    } catch (e: SerializationException) {
        ResultWrapper.Failure.SerializationError(ErrorMessage(e.message.toString()))
    } catch (e: IOException) {
        ResultWrapper.Failure.NetworkError
    } catch (e: JsonConvertException) {
        ResultWrapper.Failure.SerializationError(ErrorMessage(e.message.toString()))
    }
}


suspend inline fun <reified T, reified E> HttpClient.safePatch(block: HttpRequestBuilder.() -> Unit): ResultWrapper<T, E> {
    return try {
        val response = patch { block() }
        if (response.status.isSuccess()) {
            ResultWrapper.Success(response.body())
        } else {
            val code = response.status.value
            ResultWrapper.Failure.HttpError(code, response.body())
        }
    } catch (e: ClientRequestException) {
        val code = e.response.status.value
        ResultWrapper.Failure.HttpError(code, e.errorBody())
    } catch (e: SerializationException) {
        ResultWrapper.Failure.SerializationError(ErrorMessage(e.message.toString()))
    } catch (e: IOException) {
        ResultWrapper.Failure.NetworkError
    } catch (e: JsonConvertException) {
        ResultWrapper.Failure.SerializationError(
            ErrorMessage(
                e.cause.toString()
                    .substringAfter("\"message\":\"")
                    .substringBefore("\"}")
            )
        )
    }
}

suspend inline fun <reified E> ResponseException.errorBody(): E? =
    try {
        response.body()
    } catch (e: SerializationException) {
        null
    }

fun ResultWrapper.Failure<*>.message() = when (this) {
    is ResultWrapper.Failure.HttpError<*> -> if (this.errorBody is ErrorMessage) this.errorBody.message else "An http error occurred"
    is ResultWrapper.Failure.SerializationError -> "a serialization error occurred"
    is ResultWrapper.Failure.NetworkError -> "a network error occurred"
    is ResultWrapper.Failure.UnknownError -> TODO()
}

fun ResultWrapper.Failure<*>.code() = when (this) {
    is ResultWrapper.Failure.HttpError<*> -> this.code
    is ResultWrapper.Failure.SerializationError -> 0
    is ResultWrapper.Failure.NetworkError -> 0
    is ResultWrapper.Failure.UnknownError -> TODO()
}

fun <T, E, U> ResultWrapper<T, E>.map(
    transform: (T) -> U
): ResultWrapper<U, E> {
    return when (this) {
        is ResultWrapper.Success -> {
            value?.let {
                ResultWrapper.Success(transform(it))
            } ?: ResultWrapper.Success(null as U)
        }
        is ResultWrapper.Failure -> this
    }
}

suspend fun <T, E, U> ResultWrapper<T, E>.andThen(
    transform: suspend (T) -> ResultWrapper<U, E>
): ResultWrapper<U, E> {
    return when (this) {
        is ResultWrapper.Success -> {
            value?.let {
                transform(it)
            } ?: ResultWrapper.Success(null as U)
        }
        is ResultWrapper.Failure -> this
    }
}