package com.example.camperpro.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.example.camperpro.domain.model.Location
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*

actual class LocationClient actual constructor(private val context: Any?) {

    @SuppressLint("MissingPermission")
    actual fun startLocationObserver() {
        Globals.geoLoc.locationObserver = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                this@LocationClient.getCurrentLocation {
                    Globals.geoLoc.lastKnownLocation = it
                }
                delay(Constants.GPS_UPDATE_INTERVAL)
            }
        }
    }

    actual fun stopLocationObserver() {
        val observer = Globals.geoLoc.locationObserver
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
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            doWithLocation(Location(location.latitude, location.longitude))
        }.addOnFailureListener { }
    }

    @SuppressLint("MissingPermission")
    actual fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            if (!(context as Context).hasLocationPermission) {
                throw LocationException("Missing location permission")
            }

            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGpsEnabled && !isNetworkEnabled) {
                throw LocationException("gps or network is not enabled")
            }

            val request = com.google.android.gms.location.LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                interval
            ).setIntervalMillis(interval).build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    p0.lastLocation?.let { location ->
                        launch { send(Location(location.latitude, location.longitude)) }
                    }
                }
            }

            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }
    actual class LocationException actual constructor(message: String) : Exception()
}

val Context.hasLocationPermission
    get() = ContextCompat.checkSelfPermission(
        this, android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
        this, android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

