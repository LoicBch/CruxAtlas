package com.example.camperpro.managers.network

internal expect class NetworkLoyal() {
    fun startNetworkObserver()
    fun stopNetworkObserver()
    fun isNetworkAvailable(): Boolean
}