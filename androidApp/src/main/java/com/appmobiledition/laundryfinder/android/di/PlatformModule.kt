package com.appmobiledition.laundryfinder.android.di

import com.appmobiledition.laundryfinder.android.AppViewModel
import com.appmobiledition.laundryfinder.android.aroundLocation.AroundLocationViewModel
import com.appmobiledition.laundryfinder.android.checklists.CheckListDetailsViewModel
import com.appmobiledition.laundryfinder.android.checklists.CheckListsViewModel
import com.appmobiledition.laundryfinder.android.mainmap.MainMapViewModel
import com.appmobiledition.laundryfinder.android.myLocation.MyLocationViewModel
import com.appmobiledition.laundryfinder.android.onBoarding.SplashScreenViewModel
import com.appmobiledition.laundryfinder.android.partners.PartnersViewModel
import com.appmobiledition.laundryfinder.data.datasources.local.DatabaseDriverFactory
import com.appmobiledition.laundryfinder.data.datasources.local.FilterDaoDelight
import com.appmobiledition.laundryfinder.data.datasources.local.LocationSearchDaoDelight
import com.appmobiledition.laundryfinder.data.datasources.local.SearchDaoDelight
import com.appmobiledition.laundryfinder.data.datasources.local.dao.FilterDao
import com.appmobiledition.laundryfinder.data.datasources.local.dao.LocationSearchDao
import com.appmobiledition.laundryfinder.data.datasources.local.dao.SearchDao
import com.appmobiledition.laundryfinder.database.CamperproDatabase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.GlobalContext.get
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


fun platformModule() = listOf(viewModelModule, persistenceModule)

val viewModelModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::MainMapViewModel)
    viewModelOf(::SplashScreenViewModel)
    viewModelOf(::AroundLocationViewModel)
    viewModelOf(::MyLocationViewModel)
    viewModelOf(::PartnersViewModel)
    viewModelOf(::CheckListsViewModel)
    viewModelOf(::CheckListDetailsViewModel)
}

val persistenceModule = module {
    singleOf<CamperproDatabase> {
        CamperproDatabase(DatabaseDriverFactory(get().get()).createDriver())
    }
    singleOf<SearchDao> { SearchDaoDelight(get().get()) }
    singleOf<LocationSearchDao> { LocationSearchDaoDelight(get().get()) }
    singleOf<FilterDao> { FilterDaoDelight(get().get()) }
}