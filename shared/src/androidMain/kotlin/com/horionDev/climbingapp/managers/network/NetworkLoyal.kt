package com.horionDev.climbingapp.managers.network

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities.*
import android.net.NetworkRequest
import com.horionDev.climbingapp.managers.network.observers.ActivityLifecycleObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

internal actual class NetworkLoyal {

    private var networkIsObserved = false
    private var networkStatus: NetworkManager.NetworkStatus = NetworkManager.NetworkStatus.Unavailable
//    actual fun isNetworkObserved() = networkIsObserved

    @SuppressLint("MissingPermission")
    actual fun startNetworkObserver(): Flow<NetworkManager.NetworkStatus> {

        return callbackFlow {

            connectivityManager.activeNetwork
                ?: launch {
                    networkStatus = NetworkManager.NetworkStatus.Unavailable
                    send(NetworkManager.NetworkStatus.Unavailable)
                }

            val callBack = object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    networkIsObserved = true
                    networkStatus = NetworkManager.NetworkStatus.Available
                    launch {
                        send(NetworkManager.NetworkStatus.Available)
                    }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    networkIsObserved = true
                    networkStatus = NetworkManager.NetworkStatus.Losing
                    launch {
                        send(NetworkManager.NetworkStatus.Losing)
                    }
                }

                override fun onLost(network: Network) {
                    networkIsObserved = true
                    networkStatus = NetworkManager.NetworkStatus.Lost
                    super.onLost(network)
                    launch {
                        send(NetworkManager.NetworkStatus.Lost)
                    }
                }

                override fun onUnavailable() {
                    networkIsObserved = true
                    networkStatus = NetworkManager.NetworkStatus.Unavailable
                    super.onUnavailable()
                    launch {
                        send(NetworkManager.NetworkStatus.Unavailable)
                    }
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callBack)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callBack)
            }
        }.distinctUntilChanged()
        //        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    actual fun stopNetworkObserver() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    // a voir si c'est relevant sinon check la co avec un networkCallback
    @SuppressLint("MissingPermission")
    actual fun isNetworkAvailable(): Boolean {
        return networkStatus == NetworkManager.NetworkStatus.Available
    }

    internal var activity: WeakReference<Activity>? = null
    internal fun configure(context: Context) {
        val application = context.applicationContext as? Application
        application?.registerActivityLifecycleCallbacks(ActivityLifecycleObserver)
            ?: run {
                val activity = context.applicationContext as? Activity
                this.activity = WeakReference(activity)
            }

        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .addTransportType(TRANSPORT_CELLULAR)
            .addTransportType(TRANSPORT_WIFI)
            .addTransportType(TRANSPORT_ETHERNET)
            .build()

        networkCallback = object : NetworkCallback() {
            @SuppressLint("MissingPermission")
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                connectivityManager.getNetworkCapabilities(network)?.hasTransport(TRANSPORT_WIFI)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
            }

            override fun onUnavailable() {
                super.onUnavailable()
            }
        }

    }

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: NetworkCallback
    private lateinit var networkRequest: NetworkRequest

    private val focusedActivity: Activity?
        get() = activity?.get()?.let {
            if (it.isFinishing || it.isDestroyed) null else {
                it
            }
        }
}