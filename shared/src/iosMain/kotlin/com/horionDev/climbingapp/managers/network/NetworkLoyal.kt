package com.horionDev.climbingapp.managers.network

import kotlinx.coroutines.flow.Flow
import platform.SystemConfiguration.SCNetworkReachabilityCreateWithName

internal actual class NetworkLoyal {


    actual fun startNetworkObserver(): Flow<NetworkManager.NetworkStatus> {
        TODO("Not yet implemented")
    }

    actual fun stopNetworkObserver() {
    }

    actual fun isNetworkAvailable(): Boolean {
        TODO("Not yet implemented")
    }

    // dunno si y'a une meilleur classe pour ca
//    private val reachabilityProvider by lazy {
//        SCNetworkReachabilityCreateWithName(
//            null,
//            "www.google.com"
//        )
//    }
}