package com.example.camperpro.utils

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.example.camperpro.domain.model.Location
import kotlinx.coroutines.*
import java.util.*

actual class LocationManager actual constructor(private val context: Any?) {

    @SuppressLint("MissingPermission")
    actual fun startLocationObserver() {
        Globals.Location.locationObserver = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                this@LocationManager.getCurrentLocation {
                    Globals.Location.lastKnownLocation = it
                }
                delay(Constants.LOCATION_UPDATE_RATE)
            }
        }
    }

    actual fun stopLocationObserver() {
        val observer = Globals.Location.locationObserver
        if (observer?.isActive == true) {
            observer.cancel()
        }
    }

    @SuppressLint("MissingPermission")
    actual fun getCurrentLocation(
        doWithLocation: (Location) -> Unit
    ) {
        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context as Context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                doWithLocation(Location(location.latitude, location.longitude))
            }.addOnFailureListener { }
    }
}