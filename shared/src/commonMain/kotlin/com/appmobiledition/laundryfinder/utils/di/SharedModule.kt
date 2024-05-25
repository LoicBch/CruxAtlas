package com.appmobiledition.laundryfinder.utils.di

import com.appmobiledition.laundryfinder.data.datasources.remote.Api
import com.appmobiledition.laundryfinder.data.datasources.remote.CamperProApi
import com.appmobiledition.laundryfinder.utils.Constants
import com.appmobiledition.laundryfinder.utils.LanguageManager
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
        CamperProApi(HttpClient {

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

}

val useCasesDependencies = module {

}




