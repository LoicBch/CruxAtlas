package com.example.camperproglobal.android.di

import android.app.Application
import com.example.camperproglobal.domain.model.dao.SpotDao
import com.jetbrains.kmm.shared.data.datasources.remote.SpotsService
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Provides
//    @Singleton
//    fun provideSqlDriver(app: Application): SqlDriver {
//        return DatabaseDriverFactory(app).createDriver()
//    }
//
//    @Provides
//    @Singleton
//    fun provideSpotDao(driver: SqlDriver): SpotDao {
//        return SpotDaoDelight(CamperproDatabase(driver))
//    }

    @Provides
    @Singleton
    fun provideSpotService(app: Application): SpotsService {
        return SpotsService.create()
    }
}