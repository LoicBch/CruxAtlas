package com.horionDev.climbingapp.utils.di

import com.horionDev.climbingapp.data.datasources.remote.Api
import com.horionDev.climbingapp.data.datasources.remote.CruxAtlasApi
import com.horionDev.climbingapp.domain.repositories.UserRepository
import com.horionDev.climbingapp.domain.repositories.CragRepository
import com.horionDev.climbingapp.data.repositories.Users
import com.horionDev.climbingapp.data.repositories.Crags
import com.horionDev.climbingapp.domain.usecases.LoginUseCase
import com.horionDev.climbingapp.utils.Constants
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun sharedModule() = listOf(apiDependency, useCasesDependencies, repositoriesDependencies)


val apiDependency = module {
    singleOf<Api> {
        CruxAtlasApi(HttpClient {

            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.DEFAULT
            }

            install(ContentNegotiation) {
                json(DefaultJson, ContentType.Any)
            }

            defaultRequest {
                url(Constants.BASE_URL)
            }
        })
    }
}

val repositoriesDependencies = module {
    singleOf(::Users) { bind<UserRepository>() }
    singleOf(::Crags) { bind<CragRepository>() }
}

val useCasesDependencies = module {
    factoryOf(::LoginUseCase)
}




