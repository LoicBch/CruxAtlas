package com.example.camperpro.android

import android.app.Application
import com.example.camperpro.android.di.platformModule
import com.example.camperpro.utils.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CamperProApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@CamperProApp)
            modules(sharedModule() + platformModule())
        }
    }
}