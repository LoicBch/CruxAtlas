package com.horionDev.climbingapp.managers.location

import com.horionDev.climbingapp.managers.location.extension.*
import com.horionDev.climbingapp.managers.location.extension.previousAuthorizationStatus
import kotlinx.cinterop.useContents
import platform.CoreLocation.*
import platform.darwin.NSObject

internal class CLLocationManagerDelegate: NSObject(), CLLocationManagerDelegateProtocol {

    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) =
        notifyDidChangeAuthorization(
            LocationAuthorizationStatus.fromInt(manager.authorizationStatus)
        )

    override fun locationManager(manager: CLLocationManager, didChangeAuthorizationStatus: CLAuthorizationStatus) =
        notifyDidChangeAuthorization(
            LocationAuthorizationStatus.fromInt(didChangeAuthorizationStatus)
        )

    private fun notifyDidChangeAuthorization(status: LocationAuthorizationStatus) {
        val isGranted = LocationManager.requiredPermission == status ||
                LocationAuthorizationStatus.AuthorizedAlways == status
        LocationManager.notifyOnPermissionUpdated(isGranted)
        LocationManager.previousAuthorizationStatus = status
        LocationManager.startLocationUpdating()
    }

    @Suppress("CONFLICTING_OVERLOADS")
    override fun locationManager(manager: CLLocationManager, didStartMonitoringForRegion: CLRegion) { }

    @Suppress("CONFLICTING_OVERLOADS")
    override fun locationManager(manager: CLLocationManager, didEnterRegion: CLRegion) { }

    @Suppress("CONFLICTING_OVERLOADS")
    override fun locationManager(manager: CLLocationManager, didExitRegion: CLRegion) { }

    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        notify(didUpdateLocations.last() as? CLLocation, manager.heading)
    }

    override fun locationManager(manager: CLLocationManager, didUpdateHeading: CLHeading) =
        notify(manager.location, didUpdateHeading)

    private fun notify(lastLocation: CLLocation?, lastHeading: CLHeading?) {
        val location = lastLocation ?: return
        val heading = lastHeading?.trueHeading ?: 0.0
        location.coordinate.useContents {
            val coordinates = Coordinates(
                latitude,
                longitude
            )
            val data = LocationData(
                location.horizontalAccuracy,
                location.altitude,
                location.verticalAccuracy,
                heading,
                location.speed,
                coordinates
            )
            LocationManager.notifyOnLocationUpdated(data)
        }
    }
}