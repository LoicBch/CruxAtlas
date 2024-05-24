package com.appmobiledition.laundryfinder.managers.network

import com.appmobiledition.laundryfinder.managers.location.LocationData

typealias OnNetworkStateChangesBlock = (location: LocationData) -> Unit
typealias OnNetworkSourceChangesBlock = (location: LocationData) -> Unit
typealias OnNetworkUnavailableBlock = (location: LocationData) -> Unit
typealias OnNetworkAvailableBlock = (location: LocationData) -> Unit

interface BaseNetworkManagerCompanion {
    fun startNetworkObserver()
    fun stopNetworkObserver()
    fun onNetworkStateChanges(block: OnNetworkStateChangesBlock): BaseNetworkManagerCompanion
    fun onNetworkSourceChanges(block: OnNetworkSourceChangesBlock): BaseNetworkManagerCompanion
    fun onNetworkUnavailable(block: OnNetworkUnavailableBlock): BaseNetworkManagerCompanion
    fun onNetworkAvailable(block: OnNetworkAvailableBlock): BaseNetworkManagerCompanion
    fun networkIsAvailable(): Boolean

    //ajouter les removers si besoins
}