package com.appmobiledition.laundryfinder.android.di

import com.appmobiledition.laundryfinder.android.AppViewModel
import com.appmobiledition.laundryfinder.android.aroundLocation.AroundLocationViewModel
import com.appmobiledition.laundryfinder.android.mainmap.MainMapViewModel
import com.appmobiledition.laundryfinder.android.onBoarding.SplashScreenViewModel
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
//    singleOf<CamperproDatabase> {
//        CamperproDatabase(DatabaseDriverFactory(get().get()).createDriver())
//    }
//    singleOf<SearchDao> { SearchDaoDelight(get().get()) }
//    singleOf<LocationSearchDao> { LocationSearchDaoDelight(get().get()) }
//    singleOf<FilterDao> { FilterDaoDelight(get().get()) }
}