package com.example.camperpro.utils

import com.example.camperpro.data.datasources.local.DatabaseDriverFactory
import com.example.camperpro.data.datasources.local.FilterDaoDelight
import com.example.camperpro.data.datasources.local.LocationSearchDaoDelight
import com.example.camperpro.data.datasources.local.SearchDaoDelight
import com.example.camperpro.data.datasources.local.dao.FilterDao
import com.example.camperpro.data.datasources.local.dao.LocationSearchDao
import com.example.camperpro.data.datasources.local.dao.SearchDao
import com.example.camperpro.database.CamperproDatabase
import com.example.camperpro.utils.di.sharedModule
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