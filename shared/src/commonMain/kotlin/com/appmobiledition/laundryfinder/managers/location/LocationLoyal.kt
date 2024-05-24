package com.appmobiledition.laundryfinder.managers.location

// should be internal make annotation
@Suppress("NO_ACTUAL_FOR_EXPECT")
internal expect class LocationLoyal() {
    fun isPermissionAllowed(): Boolean
    fun startLocationUpdating()
    fun stopLocationUpdating()
    fun getCurrentLocation()
    fun removeAllListeners()
    fun removeListeners(target: Any)
    fun requestPermission()
    fun isLocationEnabled(): Boolean
}