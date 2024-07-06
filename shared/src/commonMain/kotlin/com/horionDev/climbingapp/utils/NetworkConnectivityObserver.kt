package com.horionDev.climbingapp.utils

import kotlinx.coroutines.flow.Flow

expect class NetworkConnectivityObserver(context: Any?) : ConnectivityObserver {
    override fun observe(): Flow<ConnectivityObserver.NetworkStatus>
}
