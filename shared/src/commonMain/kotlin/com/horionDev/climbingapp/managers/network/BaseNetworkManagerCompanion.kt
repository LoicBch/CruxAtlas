package com.horionDev.climbingapp.managers.network

import com.horionDev.climbingapp.managers.location.LocationData
import kotlinx.coroutines.flow.Flow

typealias OnNetworkStateChangesBlock = (location: LocationData) -> Unit
typealias OnNetworkSourceChangesBlock = (location: LocationData) -> Unit
typealias OnNetworkUnavailableBlock = (location: LocationData) -> Unit
typealias OnNetworkAvailableBlock = (location: LocationData) -> Unit

interface BaseNetworkManagerCompanion {
    fun startNetworkObserver(): Flow<NetworkManager.NetworkStatus>
    fun stopNetworkObserver()
    fun onNetworkStateChanges(block: OnNetworkStateChangesBlock): BaseNetworkManagerCompanion
    fun onNetworkSourceChanges(block: OnNetworkSourceChangesBlock): BaseNetworkManagerCompanion
    fun onNetworkUnavailable(block: OnNetworkUnavailableBlock): BaseNetworkManagerCompanion
    fun onNetworkAvailable(block: OnNetworkAvailableBlock): BaseNetworkManagerCompanion
    fun isNetworkAvailable(): Boolean
//    fun isNetworkObserved(): Boolean
}