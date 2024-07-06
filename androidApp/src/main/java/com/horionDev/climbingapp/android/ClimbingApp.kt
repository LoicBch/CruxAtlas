package com.horionDev.climbingapp.android

import android.app.Application
import com.horionDev.climbingapp.android.di.platformModule
import com.horionDev.climbingapp.utils.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ClimbingApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@ClimbingApp)
            modules(sharedModule() + platformModule())
        }
    }
}