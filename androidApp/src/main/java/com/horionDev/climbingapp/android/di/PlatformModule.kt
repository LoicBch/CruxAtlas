package com.horionDev.climbingapp.android.di

import com.horionDev.climbingapp.android.AppViewModel
import com.horionDev.climbingapp.android.aroundLocation.AroundLocationViewModel
import com.horionDev.climbingapp.android.mainmap.MainMapViewModel
import com.horionDev.climbingapp.android.onBoarding.SplashScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


fun platformModule() = listOf(viewModelModule, persistenceModule)

val viewModelModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::MainMapViewModel)
    viewModelOf(::SplashScreenViewModel)
    viewModelOf(::AroundLocationViewModel)
}

val persistenceModule = module {
//    singleOf<Database> {
//        Database(DatabaseDriverFactory(get().get()).createDriver())
//    }
//    singleOf<SearchDao> { SearchDaoDelight(get().get()) }
//    singleOf<LocationSearchDao> { LocationSearchDaoDelight(get().get()) }
//    singleOf<FilterDao> { FilterDaoDelight(get().get()) }
}