package com.example.camperpro.utils.di

import com.example.camperpro.data.datasources.remote.SpotsService
import com.example.camperpro.data.datasources.remote.SpotsServiceImpl
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import com.example.camperpro.domain.usecases.FetchSpotAtLocationUseCase
import org.koin.dsl.module

fun sharedModule() = listOf(networkDependency, useCaseDependency)

val networkDependency = module {
    singleOf<SpotsService> {
        SpotsServiceImpl(HttpClient {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json()
            }
        })
    }
}

val useCaseDependency = module {
    factoryOf(::FetchSpotAtLocationUseCase)
}