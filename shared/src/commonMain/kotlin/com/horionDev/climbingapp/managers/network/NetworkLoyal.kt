package com.horionDev.climbingapp.managers.network

import kotlinx.coroutines.flow.Flow

internal expect class NetworkLoyal() {
    fun startNetworkObserver(): Flow<NetworkManager.NetworkStatus>
    fun stopNetworkObserver()
    fun isNetworkAvailable(): Boolean
//    fun isNetworkObserved(): Boolean
}