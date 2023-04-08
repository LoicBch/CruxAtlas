package com.example.camperpro.utils.di

import com.example.camperpro.data.datasources.remote.Api
import com.example.camperpro.data.datasources.remote.CamperProApi
import com.example.camperpro.data.repositories.*
import com.example.camperpro.domain.repositories.*
import com.example.camperpro.domain.usecases.*
import com.example.camperpro.utils.Constants
import com.example.camperpro.utils.LanguageManager
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
    singleOf(::Ads) { bind<AdRepository>() }
    singleOf(::AllNews) { bind<NewsRepository>() }
    singleOf(::Dealers) { bind<DealerRepository>() }
    singleOf(::CheckLists) { bind<CheckListRepository>() }
    singleOf(::Searches) { bind<SearchesRepository>() }
    singleOf(::Filters) { bind<FilterRepository>() }
    singleOf(::Events) { bind<EventRepository>() }
    singleOf(::Partners) { bind<PartnerRepository>() }
}

val useCasesDependencies = module {
    factoryOf(::FetchDealersAtLocationUseCase)
    factoryOf(::GetAllSearchForACategory)
    factoryOf(::FetchSuggestionsFromInput)
    factoryOf(::FetchAds)
    factoryOf(::FetchNews)
    factoryOf(::FetchCheckLists)
    factoryOf(::SetupApp)
    factoryOf(::AddSearch)
    factoryOf(::DeleteSearch)
    factoryOf(::DeleteFilter)
    factoryOf(::ApplyPlacesFilters)
    factoryOf(::FetchFilters)
    factoryOf(::GetFiltersSaved)
    factoryOf(::SortDealer)
    factoryOf(::SortEvents)
    factoryOf(::FetchPartners)
    factoryOf(::FetchEvents)
    factoryOf(::GetLocationInfos)
    factory { (context: Any?) -> LanguageManager(context) }
}

