package com.example.camperpro.managers.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.example.camperpro.managers.location.LocationManager.Companion.notifyOnLocationUnavailable
import com.example.camperpro.managers.location.extension.toLocationData
import com.example.camperpro.managers.location.utils.LocationUtil
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import java.lang.ref.WeakReference

internal actual class LocationLoyal {

    actual fun isPermissionAllowed() = focusedActivity?.let {
        ActivityCompat.checkSelfPermission(
            it,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    } ?: false

    actual fun removeAllListeners() {}

    actual fun removeListeners(target: Any) {}

    actual fun requestPermission() {
        val activity = focusedActivity ?: run {
            notifyOnLocationUnavailable()
            return
        }

        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            requestPermissionsRequestCode
        )
    }

    @SuppressLint("MissingPermission")
    actual fun startLocationUpdating() {
        val activity = focusedActivity ?: run {
            notifyOnLocationUnavailable()
            return
        }

        if (!isPermissionAllowed()) {
            requestPermission()
            notifyOnLocationUnavailable()
        } else if (!LocationUtil.checkLocationEnable(activity)) {
            notifyOnLocationUnavailable()
        } else {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

    }

    actual fun stopLocationUpdating() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    //https://developer.android.com/training/location/retrieve-current?hl=ko#BestEstimate
    @SuppressLint("MissingPermission")
    actual fun getCurrentLocation() {
        val activity = focusedActivity ?: run {
            notifyOnLocationUnavailable()
            return
        }

        var isLocationNotified = false

        if (!isPermissionAllowed()) {
            requestPermission()
            notifyOnLocationUnavailable()
        } else if (!LocationUtil.checkLocationEnable(activity)) {
            notifyOnLocationUnavailable()
        } else {

            val cts = CancellationTokenSource()

            fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                cts.token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    LocationManager.notifyOnLocationUpdated(location.toLocationData())
                    isLocationNotified = true

                    // For update latest location
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }
            }.addOnFailureListener {}

            //clean le retry

            //Retry
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    if (!isLocationNotified) {
                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                            if (location != null) {
                                LocationManager.notifyOnLocationUpdated(
                                    location.toLocationData()
                                )
                                isLocationNotified = true

                                // For update latest location
                                fusedLocationClient.requestLocationUpdates(
                                    locationRequest,
                                    locationCallback,
                                    Looper.getMainLooper()
                                )
                            }
                        }.addOnFailureListener {}
                    }
                }, (5 * 1000).toLong()
            )


            //Retry and unavailable
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    if (!isLocationNotified) {
                        notifyOnLocationUnavailable()
                    }
                }, (10 * 1000).toLong()
            )
        }
    }

    internal var activity: WeakReference<Activity>? = null

    internal fun configure(context: Context) {
        val application = context.applicationContext as? Application
        application?.registerActivityLifecycleCallbacks(com.example.camperpro.managers.location.observers.ActivityLifecycleObserver)
            ?: run {
                val activity = context.applicationContext as? Activity
                this.activity = WeakReference(activity)
            }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.locations.last()
                val data = LocationData(
                    location.accuracy.toDouble(),
                    location.altitude,
                    0.0,
                    location.bearing.toDouble(),
                    location.speed.toDouble(),
                    Coordinates(location.latitude, location.longitude)
                )
                LocationManager.notifyOnLocationUpdated(data)
            }
        }

        val settings = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        LocationServices
            .getSettingsClient(context)
            .checkLocationSettings(settings)
    }

    internal fun processRequestPermissionsResult(
        requestCode: Int,
        @Suppress("UNUSED_PARAMETER")
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            requestPermissionsRequestCode -> {
                if (grantResults.isEmpty()) {
                    return
                }
                when (grantResults[0]) {
                    PackageManager.PERMISSION_GRANTED -> {
                        startLocationUpdating()
                        LocationManager.notifyOnPermissionUpdated(true)
                    }
                    PackageManager.PERMISSION_DENIED ->
                        LocationManager.notifyOnPermissionUpdated(false)
                    else -> Unit
                }
            }
        }
    }

    private val requestPermissionsRequestCode = 4885

    private val focusedActivity: Activity?
        get() = activity?.get()?.let {
            if (it.isFinishing || it.isDestroyed) null else {
                it
            }
        }


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationCallback: LocationCallback
    private var locationRequest: LocationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        fastestInterval = 1 * 1000L
        interval = 10 * 1000L
    }

    // si on veut modifier la request depuis android
    fun setLocationRequest(androidLocationRequest: AndroidLocationRequest) {
        locationRequest = LocationRequest.create().apply {
            priority = androidLocationRequest.priority.value
            androidLocationRequest.fastestInterval?.let { fastestInterval = it }
            androidLocationRequest.interval?.let { interval = it }
            androidLocationRequest.maxWaitTime?.let { maxWaitTime = it }
            androidLocationRequest.smallestDisplacement?.let { smallestDisplacement = it }
            androidLocationRequest.isWaitForAccurateLocation?.let { isWaitForAccurateLocation = it }
            androidLocationRequest.numUpdates?.let { numUpdates = it }
            androidLocationRequest.expirationTime?.let { expirationTime = it }
        }
    }
}