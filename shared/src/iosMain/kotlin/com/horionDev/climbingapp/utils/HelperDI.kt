package com.horionDev.climbingapp.utils

//import com.horionDev.climbingapp.data.datasources.local.DatabaseDriverFactory
import com.horionDev.climbingapp.utils.di.sharedModule
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
//    singleOf<Database> {
//        Database(DatabaseDriverFactory().createDriver())
//    }
}