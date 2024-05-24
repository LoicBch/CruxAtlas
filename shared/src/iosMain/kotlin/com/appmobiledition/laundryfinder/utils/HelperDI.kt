package com.appmobiledition.laundryfinder.utils

import com.appmobiledition.laundryfinder.data.datasources.local.DatabaseDriverFactory
import com.appmobiledition.laundryfinder.data.datasources.local.FilterDaoDelight
import com.appmobiledition.laundryfinder.data.datasources.local.LocationSearchDaoDelight
import com.appmobiledition.laundryfinder.data.datasources.local.SearchDaoDelight
import com.appmobiledition.laundryfinder.data.datasources.local.dao.FilterDao
import com.appmobiledition.laundryfinder.data.datasources.local.dao.LocationSearchDao
import com.appmobiledition.laundryfinder.data.datasources.local.dao.SearchDao
import com.appmobiledition.laundryfinder.database.CamperproDatabase
import com.appmobiledition.laundryfinder.utils.di.sharedModule
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class HelperDI {
    fun initKoin() {
        val koinApp = startKoin {
            modules(sharedModule().toMutableList().apply { add(iosModule) }.toList())
        }.koin
    }
}

val iosModule = module {
    singleOf<CamperproDatabase> {
        CamperproDatabase(DatabaseDriverFactory().createDriver())
    }
    single { SearchDaoDelight(get()) as SearchDao }
    single { LocationSearchDaoDelight(get()) as LocationSearchDao }
    single { FilterDaoDelight(get()) as FilterDao }
}