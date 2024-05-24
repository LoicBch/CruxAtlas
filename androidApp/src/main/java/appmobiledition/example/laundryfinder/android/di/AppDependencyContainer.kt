package com.example.camperpro.android.di

import com.example.camperpro.android.AppViewModel

class AppDependencyContainer(
    viewModel: AppViewModel
) {
    val appViewModel: AppViewModel = viewModel
}