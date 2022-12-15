package com.example.camperpro.utils.di

import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.data.datasources.remote.SpotApi
import com.example.camperpro.data.repositories.*
import com.example.camperpro.domain.repositories.*
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import com.example.camperpro.domain.usecases.*
import io.ktor.http.*
import org.koin.core.module.dsl.bind
import org.koin.dsl.module

fun sharedModule() = listOf(apiDependency, useCasesDependencies, repositoriesDependencies)


val apiDependency = module {
    singleOf<Api> {
        SpotApi(HttpClient {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(DefaultJson, ContentType.Text.Html)
//                json()
            }
        })
    }
}

val repositoriesDependencies = module {
    singleOf(::Ads) { bind<AdRepository>() }
    singleOf(::AllNews) { bind<NewsRepository>() }
    singleOf(::Spots) { bind<SpotRepository>() }
    singleOf(::Services) { bind<ServiceRepository>() }
    singleOf(::CheckLists) { bind<CheckListRepository>() }
    singleOf(::Brands) { bind<BrandRepository>() }
}

val useCasesDependencies = module {
    factoryOf(::FetchSpotAtLocationUseCase)
    factoryOf(::FetchAds)
    factoryOf(::FetchBrands)
    factoryOf(::FetchNews)
    factoryOf(::FetchCheckLists)
    factoryOf(::FetchServices)
}

