package com.appmobiledition.laundryfinder.android

import android.app.Application
import com.appmobiledition.laundryfinder.android.di.platformModule
import com.appmobiledition.laundryfinder.utils.di.sharedModule
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