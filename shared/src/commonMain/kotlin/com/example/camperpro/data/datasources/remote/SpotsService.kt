package com.jetbrains.kmm.shared.data.datasources.remote

import com.jetbrains.kmm.shared.data.ResultWrapper
import com.jetbrains.kmm.shared.domain.model.Spot
import com.jetbrains.kmm.shared.domain.model.User
import com.jetbrains.kmm.shared.domain.model.Location
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*


interface SpotsService {


    suspend fun getSpotsAround(location: Location): ResultWrapper<List<Spot>>

    suspend fun getUsers(): List<User>

    companion object {


        fun create(): SpotsService {
            return SpotsServiceImpl(HttpClient {
                install(Logging) {
                    level = LogLevel.ALL
                }
                install(ContentNegotiation) {
                    json()
                }
            })
        }
    }
}