package com.example.camperpro.managers.location

import com.example.camperpro.managers.location.extension.OnAlwaysAllowsPermissionRequiredBlock
import com.example.camperpro.managers.location.extension.appending
import com.example.camperpro.managers.location.extension.removed
import com.example.camperpro.managers.location.native.NativeAtomicReference
import com.example.camperpro.managers.location.utils.Version
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLLocationAccuracyBestForNavigation
import platform.UIKit.UIDevice

internal actual class LocationLoyal {

    actual fun isPermissionAllowed() =
        authorizationStatus == requiredPermission.value

    actual fun removeAllListeners() {
        onAlwaysAllowsPermissionRequiredBlockMap.value = emptyMap()
    }

    actual fun removeListeners(target: Any) =
        removeOnAlwaysAllowsPermissionRequired(target)

    actual fun requestPermission() = if (isRequiredAllowAlways) {
        iosLocationProvider.requestAlwaysAuthorization()
        iosLocationProvider.allowsBackgroundLocationUpdates = true
        iosLocationProvider.pausesLocationUpdatesAutomatically = false
    } else {
        iosLocationProvider.requestWhenInUseAuthorization()
    }

    actual fun startLocationUpdating() = when (authorizationStatus) {
        LocationAuthorizationStatus.AuthorizedAlways -> startUpdating()
        LocationAuthorizationStatus.AuthorizedWhenInUse -> if (isRequiredAllowAlways) {
            if (previousAuthorizationStatus.value == LocationAuthorizationStatus.AuthorizedWhenInUse) {
                notifyOnAlwaysAllowsPermissionRequired()
            } else {
                requestPermission()
            }
        } else {
            startUpdating()
        }
        LocationAuthorizationStatus.Denied -> LocationManager.notifyOnLocationUnavailable()
        else -> requestPermission()
    }

    actual fun stopLocationUpdating() {
        if (!iosLocationProvider.locationServicesEnabled) { return }
        iosLocationProvider.stopUpdatingHeading()
        iosLocationProvider.stopUpdatingLocation()
    }

    //same operation with startLocationUpdating()
    actual fun getCurrentLocation() = when (authorizationStatus) {
        LocationAuthorizationStatus.AuthorizedAlways -> startUpdating()
        LocationAuthorizationStatus.AuthorizedWhenInUse -> if (isRequiredAllowAlways) {
            if (previousAuthorizationStatus.value == LocationAuthorizationStatus.AuthorizedWhenInUse) {
                notifyOnAlwaysAllowsPermissionRequired()
            } else {
                requestPermission()
            }
        } else {
            startUpdating()
        }
        LocationAuthorizationStatus.Denied -> LocationManager.notifyOnLocationUnavailable()
        else -> requestPermission()
    }

    val requiredPermission = NativeAtomicReference(LocationAuthorizationStatus.AuthorizedAlways)
    val previousAuthorizationStatus = NativeAtomicReference(LocationAuthorizationStatus.NotSet)

    fun onAlwaysAllowsPermissionRequired(
        target: Any,
        block: OnAlwaysAllowsPermissionRequiredBlock
    ) {
        onAlwaysAllowsPermissionRequiredBlockMap.value =
            onAlwaysAllowsPermissionRequiredBlockMap.value.appending(target, block)
    }

    fun removeOnAlwaysAllowsPermissionRequired(target: Any) {
        onAlwaysAllowsPermissionRequiredBlockMap.value =
            onAlwaysAllowsPermissionRequiredBlockMap.value.removed(target)
    }

    // passer a AuthorizedWhenInUse si besoin
    private val isRequiredAllowAlways: Boolean
        get() = requiredPermission.value == LocationAuthorizationStatus.AuthorizedAlways

    private val authorizationStatus: LocationAuthorizationStatus
        get() = if (Version(UIDevice.currentDevice.systemVersion) >= Version("14")) {
            LocationAuthorizationStatus.fromInt(CLLocationManager().authorizationStatus)
        } else {
            LocationAuthorizationStatus.fromInt(CLLocationManager.authorizationStatus())
        }

    private val iosLocationProvider: CLLocationManager by lazy {
        val manager = CLLocationManager()
        manager.desiredAccuracy = kCLLocationAccuracyBestForNavigation
        manager.setDelegate(locationDelegate)
        manager
    }

    private val locationDelegate: CLLocationManagerDelegate by lazy {
        CLLocationManagerDelegate()
    }

    private val onAlwaysAllowsPermissionRequiredBlockMap = NativeAtomicReference(mapOf<Any, OnAlwaysAllowsPermissionRequiredBlock>())

    private fun notifyOnAlwaysAllowsPermissionRequired() {
        onAlwaysAllowsPermissionRequiredBlockMap.value.forEach { it.value() }
    }

    private fun startUpdating() {
        if (!iosLocationProvider.locationServicesEnabled) { return }
        iosLocationProvider.startUpdatingHeading()
        iosLocationProvider.startUpdatingLocation()
    }
}