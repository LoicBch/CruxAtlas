package com.example.camperpro.utils

import com.example.camperpro.domain.model.Location
import kotlinx.coroutines.flow.Flow

actual class LocationClient actual constructor(context: Any?) {

    actual fun startLocationObserver() {
    }

    actual fun stopLocationObserver() {
    }

    actual fun getCurrentLocation(doWithLocation: (Location) -> Unit) {
    }

    actual fun getLocationUpdates(interval: Long): Flow<Location> {
        TODO("Not yet implemented")
    }

    actual class LocationException actual constructor(message: String) : Exception()
}