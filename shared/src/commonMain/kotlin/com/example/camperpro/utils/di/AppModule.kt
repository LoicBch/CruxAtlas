package com.example.camperpro.utils.di

import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.data.datasources.remote.CamperProApi
import com.example.camperpro.data.repositories.*
import com.example.camperpro.domain.repositories.*
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import com.example.camperpro.domain.usecases.*
import com.example.camperpro.utils.KMMCalendar
import com.example.camperpro.utils.KMMPreference
import com.example.camperpro.utils.KMMContext
import com.example.camperpro.utils.Constants
import io.ktor.client.plugins.*
import io.ktor.http.*
import org.koin.core.module.dsl.bind
import org.koin.dsl.module

fun sharedModule() = listOf(apiDependency, useCasesDependencies, repositoriesDependencies, utilityDependencies)


val apiDependency = module {
    singleOf<Api> {
        CamperProApi(HttpClient {

            install(Logging) {
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(DefaultJson, ContentType.Text.Html)
//                json()
            }

            defaultRequest {
                url(Constants.BASE_URL)
            }
        })
    }
}

val repositoriesDependencies = module {
    singleOf(::Ads) { bind<AdRepository>() }
    singleOf(::AllNews) { bind<NewsRepository>() }
    singleOf(::Spots) { bind<SpotRepository>() }
    singleOf(::CheckLists) { bind<CheckListRepository>() }
}

val useCasesDependencies = module {
    factoryOf(::FetchSpotAtLocationUseCase)
    factoryOf(::FetchAds)
    factoryOf(::FetchNews)
    factoryOf(::FetchCheckLists)
    factoryOf(::SetupApp)
}

val utilityDependencies = module {
    factoryOf(::KMMCalendar)
    factoryOf(::KMMPreference)
}



