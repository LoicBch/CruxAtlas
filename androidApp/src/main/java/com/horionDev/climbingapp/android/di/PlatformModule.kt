package com.horionDev.climbingapp.android.di

import com.horionDev.climbingapp.android.AppViewModel
import com.horionDev.climbingapp.android.aroundLocation.AroundLocationViewModel
import com.horionDev.climbingapp.android.mainmap.MainMapViewModel
import com.horionDev.climbingapp.android.onBoarding.SplashScreenViewModel
import com.horionDev.climbingapp.android.newsFeed.NewsFeedViewModel
import com.horionDev.climbingapp.android.login.LoginScreenViewModel
import com.horionDev.climbingapp.android.login.SignupViewModel
import com.horionDev.climbingapp.android.profile.ProfileViewModel
import com.horionDev.climbingapp.utils.KMMPreference
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.GlobalContext.get
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


fun platformModule() = listOf(viewModelModule, persistenceModule)

val viewModelModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::MainMapViewModel)
    viewModelOf(::SplashScreenViewModel)
    viewModelOf(::AroundLocationViewModel)
    viewModelOf(::NewsFeedViewModel)
    viewModelOf(::LoginScreenViewModel)
    viewModelOf(::SignupViewModel)
    viewModelOf(::ProfileViewModel)
}

val persistenceModule = module {

    singleOf<KMMPreference> {
        KMMPreference(get().get())
    }

//    singleOf<ProductManager> ({
//        ProductManager(get().get())
//    })

}