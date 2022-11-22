package com.example.camperpro.android.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import com.example.camperpro.android.mainmap.MainMapViewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainMapViewModel)
}