package com.appmobiledition.laundryfinder.android.di

import com.appmobiledition.laundryfinder.android.AppViewModel

class AppDependencyContainer(
    viewModel: AppViewModel
) {
    val appViewModel: AppViewModel = viewModel
}