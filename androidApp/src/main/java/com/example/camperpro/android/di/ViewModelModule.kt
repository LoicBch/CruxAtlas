package com.example.camperpro.android.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import com.example.camperpro.android.mainmap.MainMapViewModel
import com.example.camperpro.android.AppViewModel
import com.example.camperpro.android.onBoarding.SplashScreenViewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::MainMapViewModel)
    viewModelOf(::SplashScreenViewModel)
}