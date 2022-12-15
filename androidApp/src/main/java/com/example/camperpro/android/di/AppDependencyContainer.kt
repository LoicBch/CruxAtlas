package com.example.camperpro.android.di

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.example.camperpro.android.AppViewModel
import com.example.camperpro.android.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppDependencyContainer(
    viewModel: AppViewModel
) {
    val appViewModel: AppViewModel = viewModel
//    val sharedPreferencesContainer: SharedPreferencesContainer = sharedPreferencesContainer

}

class SharedPreferencesContainer(var context: Context) {

}