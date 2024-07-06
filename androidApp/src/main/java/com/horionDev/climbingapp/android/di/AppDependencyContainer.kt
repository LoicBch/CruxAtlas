package com.horionDev.climbingapp.android.di

import com.horionDev.climbingapp.android.AppViewModel

class AppDependencyContainer(
    viewModel: AppViewModel
) {
    val appViewModel: AppViewModel = viewModel
}