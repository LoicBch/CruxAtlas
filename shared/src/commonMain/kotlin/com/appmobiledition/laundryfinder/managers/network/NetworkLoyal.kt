package com.appmobiledition.laundryfinder.managers.network

internal expect class NetworkLoyal() {
    fun startNetworkObserver()
    fun stopNetworkObserver()
    fun isNetworkAvailable(): Boolean
}