package com.example.camperpro.managers.network

import platform.SystemConfiguration.SCNetworkReachabilityContext
import platform.SystemConfiguration.SCNetworkReachabilityCreateWithName
import kotlin.native.internal.NativePtr

internal actual class NetworkLoyal {


    actual fun startNetworkObserver() {
//        var nativePtr = NativePtr()
//        var context = SCNetworkReachabilityContext(nativePtr)
    }

    actual fun stopNetworkObserver() {
    }

    actual fun isNetworkAvailable(): Boolean {
        TODO("Not yet implemented")
    }

    // dunno si y'a une meilleur classe pour ca
    private val reachabilityProvider by lazy {
        SCNetworkReachabilityCreateWithName(
            null,
            "www.google.com"
        )
    }
}