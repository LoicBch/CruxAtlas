package com.example.camperpro.utils

import com.example.camperpro.utils.di.sharedModule
import org.koin.core.context.startKoin

class HelperDI {
    fun initKoin() {
        val koinApp = startKoin {
            modules(sharedModule())
        }.koin
    }
}