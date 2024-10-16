package com.horionDev.climbingapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

actual class NetworkConnectivityObserver actual constructor(private val context: Any?) : ConnectivityObserver {
    @SuppressLint("MissingPermission")
    actual override fun observe(): Flow<ConnectivityObserver.NetworkStatus> {

        val connectivityManager =
            (context as Context).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return callbackFlow {
            val callBack = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch {
                        send(ConnectivityObserver.NetworkStatus.Available)
                    }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch {
                        send(ConnectivityObserver.NetworkStatus.Losing)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch {
                        send(ConnectivityObserver.NetworkStatus.Lost)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch {
                        send(ConnectivityObserver.NetworkStatus.Unavailable)
                    }
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callBack)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callBack)
            }
        }.distinctUntilChanged()
    }
}