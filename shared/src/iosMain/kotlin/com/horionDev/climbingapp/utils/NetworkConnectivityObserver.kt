package com.horionDev.climbingapp.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual class NetworkConnectivityObserver actual constructor(private val context: Any?): ConnectivityObserver {
    actual override fun observe(): Flow<ConnectivityObserver.NetworkStatus> {
        return callbackFlow {

        }
    }
}